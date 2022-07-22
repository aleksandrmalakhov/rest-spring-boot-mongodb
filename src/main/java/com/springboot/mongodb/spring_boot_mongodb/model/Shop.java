package com.springboot.mongodb.spring_boot_mongodb.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "shops")
public class Shop {
    @Id
    private long id;

    @Indexed(unique = true)
    private String name;

    @Field(value = "products")
    private List<String> listProducts;

    public Shop() {
        listProducts = new ArrayList<>();
    }

    public Shop(String name) {
        this.name = name;
        listProducts = new ArrayList<>();
    }

    public boolean addProduct(String productName) {
        if (!listProducts.contains(productName)) {
            listProducts.add(productName);
            return true;
        }
        return false;
    }
}