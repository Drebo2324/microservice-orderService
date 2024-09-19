package com.drebo.microservices.order.controller;

import com.drebo.microservices.order.domain.dto.OrderDto;
import com.drebo.microservices.order.domain.dto.OrderListDto;
import com.drebo.microservices.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderDto orderDto){
        orderService.placeOrder(orderDto);
        return "Order placed successfully";
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
