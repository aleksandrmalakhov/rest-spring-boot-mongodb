package com.springboot.mongodb.spring_boot_mongodb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private long id;

    @Indexed(unique = true)
    private String name;

    @Indexed
    private int price;

    public Product(String name, int price){
        this.name = name;
        this.price = price;
    }
}