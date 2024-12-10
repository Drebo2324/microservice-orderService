package com.drebo.microservices.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.concurrent.CompletableFuture;


public interface InventoryClient {

    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    //Use for HttpServiceProxy
    @GetExchange("/api/inventory")
    //name = application.properties cb instance name
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    boolean inStock(@RequestParam String sku, @RequestParam Integer quantity);

    //executed when cb is open
    default boolean fallbackMethod(String sku, Integer quantity, Throwable throwable){
        log.warn("Fallback: Unable to retrieve inventory for SKU {}, failure reason: {}", sku, throwable.getMessage());
        return false;
    }
}
