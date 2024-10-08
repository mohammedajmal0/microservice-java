package com.project.microservices.order.service;

import com.project.microservices.order.client.InventoryClient;
import com.project.microservices.order.dto.OrderRequest;

import com.project.microservices.order.event.OrderPlacedEvent;
import com.project.microservices.order.model.Order;
import com.project.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest){
        //map order request to order object
        // save to repository

        // we can use mock data for testing purpose instead of calling inventory service
        // sometimes paid APIs we have to use and if it's not free or not available we can use mock data
        /**
          option 1 -> mocikto
          option 2 -> wiremock
         */
        var isProductInStock=inventoryClient.isInStock(orderRequest.skuCode(),orderRequest.quantity());
        if(!isProductInStock){
            throw new RuntimeException("product with skucode : "+orderRequest.skuCode()+" is not in stock");
        }
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());

        orderRepository.save(order);

        //send the message to kafka topic
        OrderPlacedEvent orderPlacedEvent=new OrderPlacedEvent();
        orderPlacedEvent.setEmail(orderRequest.userDetails().email());
        orderPlacedEvent.setOrderNumber(order.getOrderNumber());
        orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
        orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());
        log.info("Start - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
        kafkaTemplate.send("order-placed", orderPlacedEvent);
        log.info("End - Sending OrderPlacedEvent {} to Kafka topic order-placed", orderPlacedEvent);
    }
}
