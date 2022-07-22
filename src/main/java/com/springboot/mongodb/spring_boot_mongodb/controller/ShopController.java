package com.springboot.mongodb.spring_boot_mongodb.controller;

import com.springboot.mongodb.spring_boot_mongodb.model.Shop;
import com.springboot.mongodb.spring_boot_mongodb.model.Statistics;
import com.springboot.mongodb.spring_boot_mongodb.service.MongodbService;
import com.springboot.mongodb.spring_boot_mongodb.service.ShopService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ShopController {
    private final ShopService shopService;
    private final MongodbService<Shop> mongodbService;

    @PostMapping("/shops")
    public ResponseEntity<Shop> save(@RequestBody Shop shop) {
        return mongodbService.save(shop) ?
                ResponseEntity.ok(shop) :
                ResponseEntity.unprocessableEntity().build();
    }

    @PutMapping("/shops")
    public ResponseEntity<Shop> update(@RequestBody Shop shop) {
        Shop upShop = mongodbService.update(shop);
        return ResponseEntity.ok().body(upShop);
    }

    @PutMapping("/shops/{id}")
    public ResponseEntity<String> addProductToShop(@PathVariable long id,
                                                   @RequestParam(value = "productId") long productId) {
        return shopService.addProductToShop(productId, id);
    }

    @GetMapping("/shops/{id}")
    public ResponseEntity<Shop> findById(@PathVariable long id) {
        Shop shop = mongodbService.findById(id);
        return shop == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(shop);
    }

    @RequestMapping(value = "/shops/", method = RequestMethod.GET)
    public ResponseEntity<Shop> findByName(@RequestParam(value = "name") String name) {
        Shop shop = mongodbService.findByName(name);
        return shop == null ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(shop);
    }

    @GetMapping("/shops")
    public ResponseEntity<List<Shop>> findAll() {
        List<Shop> listShops = mongodbService.findAll();
        return (listShops == null || listShops.isEmpty()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(listShops);
    }

    @DeleteMapping("/shops/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable long id) {
        return mongodbService.deleteById(id) ?
                ResponseEntity.ok(id) :
                ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/shops/", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteByName(@RequestParam(value = "name") String name) {
        return mongodbService.deleteByName(name) ?
                ResponseEntity.ok(name + " - delete") :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/shops")
    public ResponseEntity<Void> deleteAll() {
        List<Shop> listShops = mongodbService.findAll();
        if (listShops == null || listShops.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        mongodbService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shops/statistics")
    public ResponseEntity<List<Statistics>> getStatisticByAllShop() {
        List<Statistics> statisticsList = shopService.getStatisticByAllShop();
        return (statisticsList == null || statisticsList.isEmpty()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(statisticsList);
    }
}