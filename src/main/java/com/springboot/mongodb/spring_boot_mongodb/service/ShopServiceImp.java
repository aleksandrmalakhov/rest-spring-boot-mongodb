package com.springboot.mongodb.spring_boot_mongodb.service;

import com.google.gson.Gson;
import com.springboot.mongodb.spring_boot_mongodb.model.Product;
import com.springboot.mongodb.spring_boot_mongodb.model.Shop;
import com.springboot.mongodb.spring_boot_mongodb.model.Statistics;
import com.springboot.mongodb.spring_boot_mongodb.model.SubStatistics;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

@Service
@AllArgsConstructor
public class ShopServiceImp implements MongodbService<Shop>, ShopService {
    private final MongoTemplate mongoTemplate;

    @Override
    public boolean save(@NonNull Shop shop) {
        Shop shp = this.findById(shop.getId());
        if (shp == null) {
            mongoTemplate.save(shop);
            return true;
        }
        return false;
    }

    @Override
    public Shop update(Shop shop) {
        return mongoTemplate.save(shop);
    }

    @Override
    public Shop findById(long id) {
        return mongoTemplate.findById(id, Shop.class);
    }

    @Override
    public Shop findByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, Shop.class);
    }

    @Override
    public List<Shop> findAll() {
        return mongoTemplate.findAll(Shop.class);
    }

    @Override
    public ResponseEntity<String> addProductToShop(long productId, long shopId) {
        Shop shop = this.findById(shopId);
        Product product = mongoTemplate.findById(productId, Product.class);

        if (shop != null && product != null) {
            if (shop.addProduct(product.getName())) {
                this.update(shop);
                return ResponseEntity.ok(product.getName() + " add to the shop " + shop.getName());
            }
            return new ResponseEntity<>("Product " + product.getName() + " is already available in the shop.",
                    HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("Shop with id " + shopId + " or product with id " + productId + " not found.",
                HttpStatus.NOT_ACCEPTABLE);
    }

    @Override
    public boolean deleteById(long id) {
        Shop shop = this.findById(id);
        if (shop != null) {
            Query query = new Query(Criteria.where("id").is(id));
            mongoTemplate.remove(query, Shop.class);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        Shop shop = mongoTemplate.findOne(query, Shop.class);
        if (shop != null) {
            mongoTemplate.remove(query, Shop.class);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAll() {
        mongoTemplate.dropCollection(Shop.class);
    }

    @Override
    public List<Statistics> getStatisticByAllShop() {
        List<Statistics> statisticsList = new ArrayList<>();
        Gson gson = new Gson();

        mongoTemplate.getCollection("shops")
                .aggregate(List.of(
                        lookup("products", "products", "name", "result"),
                        unwind("$result"),
                        group("$_id",
                                avg("avg", "$result.price"),
                                min("min", "$result.price"),
                                max("max", "$result.price")),
                        project(new Document("id", "$_id")
                                .append("avgPriceProduct", "$avg")
                                .append("cheapestProduct", "$min")
                                .append("expensiveProduct", "$max")
                        )))
                .forEach(doc -> {
                    String result = doc.toJson();
                    SubStatistics sub = gson.fromJson(result, SubStatistics.class);

                    statisticsList.add(getStatistics(sub));
                });
        return statisticsList;
    }

    private Product getProduct(int value) {
        Query query = new Query(Criteria.where("price").is(value));
        return mongoTemplate.findOne(query, Product.class);
    }

    private int getCountProducts(String shopName) {
        return Objects.requireNonNull(mongoTemplate.getCollection("shops")
                .aggregate(List.of(
                        match(eq("name", shopName)),
                        lookup("products", "products", "name", "result"),
                        unwind("$result"),
                        count()
                ))
                .first()).getInteger("count");
    }

    private int getCountProductsLess100(String shopName) {
        return Objects.requireNonNull(mongoTemplate.getCollection("shops")
                .aggregate(List.of(
                        match(eq("name", shopName)),
                        lookup("products", "products", "name", "result"),
                        unwind("$result"),
                        match(gt("result.price", 100)),
                        count()
                ))
                .first()).getInteger("count");
    }

    private @NonNull Statistics getStatistics(@NonNull SubStatistics sub) {
        Shop shop = this.findById(sub.getId());
        Product cheapestProduct = getProduct(sub.getCheapestProduct());
        Product expensiveProduct = getProduct(sub.getExpensiveProduct());
        double avgPrice = sub.getAvgPriceProduct();
        int countProducts = getCountProducts(shop.getName());
        int countProductsLess100 = getCountProductsLess100(shop.getName());

        return new Statistics(shop, countProducts, avgPrice, expensiveProduct, cheapestProduct, countProductsLess100);
    }
}