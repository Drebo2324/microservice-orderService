package com.drebo.microservices.order.service;

import com.drebo.microservices.order.domain.dto.OrderDto;
import com.drebo.microservices.order.domain.dto.OrderListDto;
import com.drebo.microservices.order.domain.entity.Order;
import com.drebo.microservices.order.mapper.OrderMapper;
import com.drebo.microservices.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper ){
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderDto placeOrder(OrderDto orderDto){

        Order order = orderMapper.mapFrom(orderDto);
        Order savedOrder = orderRepository.save(order);
        log.info("Order placed");

        return orderMapper.mapTo(savedOrder);
    }

    public OrderListDto getAllOrders(){
        List<Order> allOrders = orderRepository.findAll();
        List<OrderDto> allOrdersDto = allOrders.stream().map(order -> orderMapper.mapTo(order)).toList();

        return new OrderListDto(allOrdersDto);
    }

    public void deleteAllOrders(){
        orderRepository.deleteAll();
    }
}
