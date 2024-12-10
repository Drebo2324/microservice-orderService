package com.drebo.microservices.order.controller;

import com.drebo.microservices.order.domain.dto.OrderDto;
import com.drebo.microservices.order.domain.dto.OrderListDto;
import com.drebo.microservices.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    //TODO: RETURN RESPONSE ENTITY
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto placeOrder(@RequestBody OrderDto orderDto){
        log.info("Placing Order: {}", orderDto);
        OrderDto orderResponse = orderService.placeOrder(orderDto);
        return orderResponse;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderListDto getAllOrders(){
        return orderService.getAllOrders();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllOrders(){
        orderService.deleteAllOrders();
    }
}
