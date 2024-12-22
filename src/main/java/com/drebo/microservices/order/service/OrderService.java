package com.drebo.microservices.order.service;

import com.drebo.microservices.order.client.InventoryClient;
import com.drebo.microservices.order.domain.dto.OrderDto;
import com.drebo.microservices.order.domain.dto.OrderListDto;
import com.drebo.microservices.order.domain.entity.Order;
import com.drebo.microservices.order.event.OrderNotificationEvent;
import com.drebo.microservices.order.mapper.OrderMapper;
import com.drebo.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryClient inventoryClient;
    //KafkaTemplate <k, v> -> <topic name, value sent to topic>
    private final KafkaTemplate<String, OrderNotificationEvent> kafkaTemplate;

    public OrderDto placeOrder(OrderDto orderDto) {
        log.info("Received order request: {}", orderDto);
        Order order = orderMapper.mapFrom(orderDto);
        order.setOrderNumber(UUID.randomUUID().toString());
        log.info("Converted order DTO: {}", order);

        //call inventory service
        var inStock = inventoryClient.inStock(order.getSku(), order.getQuantity());
        log.info("Inventory check result for SKU {}: {}", order.getSku(), inStock);

        if(inStock){
            Order savedOrder = orderRepository.save(order);
            log.info("Order number: {} placed successfully", savedOrder.getOrderNumber());
            log.info("Order details: {}", savedOrder);
            OrderDto orderResponse = orderMapper.mapTo(savedOrder);

            //send message to kafka topic
            OrderNotificationEvent orderNotificationEvent = new OrderNotificationEvent();
            orderNotificationEvent.setOrderNumber(orderResponse.getOrderNumber());
            orderNotificationEvent.setEmail(orderResponse.getUserDetails().getEmail());
            log.info("Sending -> orderNotification: {} to Kafka topic: order-notification", orderNotificationEvent);
            kafkaTemplate.send("order-notification", orderNotificationEvent);
            log.info("Sent -> orderNotification: {} to Kafka topic order-notification", orderNotificationEvent);
            return orderResponse;

        } else {
            log.warn("Product with SKU code {} is not in stock.", order.getSku());
            throw new RuntimeException("Product with sku code " + order.getSku() + " not in stock.");
        }
    }

    public OrderListDto getAllOrders(){
        List<Order> allOrders = orderRepository.findAll();
        List<OrderDto> allOrdersDto = allOrders.stream().map(order -> orderMapper.mapTo(order)).toList();

        return new OrderListDto(allOrdersDto);
    }

    public void deleteAllOrders(){
        orderRepository.deleteAll();
        log.info("All orders deleted.");
    }
}
