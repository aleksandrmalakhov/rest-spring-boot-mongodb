package com.springboot.mongodb.spring_boot_mongodb.service;

import com.springboot.mongodb.spring_boot_mongodb.model.Product;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImp implements MongodbService<Product> {
    private final MongoTemplate mongoTemplate;

    @Override
    public boolean save(@NonNull Product product) {
        Product prod = this.findById(product.getId());
        if (prod == null) {
            mongoTemplate.save(product);
            return true;
        }
        return false;
    }

    @Override
    public Product update(Product product) {
        return mongoTemplate.save(product);
    }

    @Override
    public Product findById(long id) {
        return mongoTemplate.findById(id, Product.class);
    }

    @Override
    public Product findByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, Product.class);
    }

    @Override
    public List<Product> findAll() {
        return mongoTemplate.findAll(Product.class);
    }

    @Override
    public boolean deleteById(long id) {
        Product product = this.findById(id);
        if (product != null) {
            Query query = new Query(Criteria.where("id").is(id));
            mongoTemplate.remove(query, Product.class);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        Product product = mongoTemplate.findOne(query, Product.class);
        if (product != null) {
            mongoTemplate.remove(query, Product.class);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAll() {
        mongoTemplate.dropCollection(Product.class);
    }
}