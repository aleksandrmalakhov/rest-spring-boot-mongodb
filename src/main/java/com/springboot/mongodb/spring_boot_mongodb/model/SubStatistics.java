package com.springboot.mongodb.spring_boot_mongodb.model;

import lombok.Data;

@Data
public class SubStatistics {
    private long id;
    private double avgPriceProduct;
    private int cheapestProduct;
    private int expensiveProduct;
}