package com.springboot.mongodb.spring_boot_mongodb.controller;

import com.springboot.mongodb.spring_boot_mongodb.model.Product;
import com.springboot.mongodb.spring_boot_mongodb.service.MongodbService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductController {
    private final MongodbService<Product> mongodbService;

    @PostMapping("/products")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        return mongodbService.save(product) ?
                ResponseEntity.ok(product) :
                ResponseEntity.unprocessableEntity().build();
    }

    @PutMapping("/products")
    public ResponseEntity<Product> update(@RequestBody Product product) {
        Product upProduct = mongodbService.update(product);
        return ResponseEntity.ok().body(upProduct);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> findById(@PathVariable long id) {
        Product product = mongodbService.findById(id);
        return product == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(product);
    }

    @RequestMapping(value = "/products/", method = RequestMethod.GET)
    public ResponseEntity<Product> findByName(@RequestParam(value = "name") String name) {
        Product product = mongodbService.findByName(name);
        return product == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(product);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> findAll() {
        List<Product> listProducts = mongodbService.findAll();

        return (listProducts == null || listProducts.isEmpty()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(listProducts);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable long id) {
        return mongodbService.deleteById(id) ?
                ResponseEntity.ok(id) :
                ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/products/", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteByName(@RequestParam(value = "name") String name) {
        return mongodbService.deleteByName(name) ?
                ResponseEntity.ok().body(name + " - delete") :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/products")
    public ResponseEntity<Void> deleteAllProducts() {
        List<Product> listProducts = mongodbService.findAll();
        if (listProducts == null || listProducts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        mongodbService.deleteAll();
        return ResponseEntity.ok().build();
    }
}