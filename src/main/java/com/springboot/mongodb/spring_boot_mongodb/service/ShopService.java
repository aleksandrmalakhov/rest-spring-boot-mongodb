package com.springboot.mongodb.spring_boot_mongodb.service;

import com.springboot.mongodb.spring_boot_mongodb.model.Statistics;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShopService {
    ResponseEntity<String> addProductToShop(long productId, long shopId);

    List<Statistics> getStatisticByAllShop();
}