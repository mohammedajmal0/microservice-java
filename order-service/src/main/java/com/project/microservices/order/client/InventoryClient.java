package com.project.microservices.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "inventory",url = "${inventory.service.url}")
public interface InventoryClient {
//    Logger log= LoggerFactory.getLogger(InventoryClient.class);

    @RequestMapping(method = RequestMethod.GET,value = "/api/inventory")
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
    @Retry(name = "inventory")
    boolean isInStock(@RequestParam("skuCode") String skuCode,@RequestParam("quantity") Integer quantity);

    default boolean fallbackMethod(String code, Integer quantity,Throwable throwable){
    return false;
    }
}
