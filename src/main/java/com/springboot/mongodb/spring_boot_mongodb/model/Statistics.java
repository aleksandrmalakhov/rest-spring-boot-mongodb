package com.springboot.mongodb.spring_boot_mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private Shop shop;
    private int countProducts;
    private double avgPriceProduct;
    private Product expensiveProduct;
    private Product cheapestProduct;
    private int countProductsLess100;
}