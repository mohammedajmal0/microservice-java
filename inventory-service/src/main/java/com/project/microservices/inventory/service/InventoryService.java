package com.project.microservices.inventory.service;

import com.project.microservices.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private  final InventoryRepository inventoryRepository;
    public boolean isInStock(String skuCode,Integer quantity){
        //find an inventory with the given skuCode with quantity > 0 
        return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode,quantity);
    }
}
