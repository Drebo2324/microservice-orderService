package com.drebo.microservices.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;


//@FeignClient(value = "inventory", url = "${inventory.url}")
public interface InventoryClient {

    Logger log = LoggerFactory.getLogger(InventoryClient.class);

//  @RequestMapping(method = RequestMethod.GET, value = "/api/inventory")
    //Use for HttpServiceProxy
    @GetExchange("/api/inventory")
    //name match application.properties cb instance name
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    @TimeLimiter(name = "inventory")
    boolean inStock(@RequestParam String sku, @RequestParam Integer quantity);

    //executed when cb is open
    default boolean fallbackMethod(String sku, Integer quantity, Throwable throwable){
        log.info("Unable to retrieve inventory for SKU {}, failure reason: {}", sku, throwable.getMessage());
        return false;
    }
}
