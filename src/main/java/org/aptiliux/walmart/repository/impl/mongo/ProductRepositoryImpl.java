/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aptiliux.walmart.repository.impl.mongo;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.aptiliux.walmart.model.Product;
import org.aptiliux.walmart.repository.ProductRepository;

/**
 *
 * @author aptiliux
 */
public class ProductRepositoryImpl implements ProductRepository {

    private MongoClient client;

    public ProductRepositoryImpl(MongoClient client) {
        this.client = client;
    }

    @Override
    public Future<Optional<List<Product>>> getProducts() {
        Promise<Optional<List<Product>>> promise = Promise.promise();
        client.find("products", new JsonObject(), res -> {
            if (res.succeeded()) {
                List<Product> products = res
                    .result()
                    .stream()
                    .map(json -> jsonToProduct(json))
                    .collect(Collectors.toList());
                promise.complete(Optional.ofNullable(products));
            } else {
                promise.fail(res.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<Optional<List<Product>>> findProductByBrandOrDescription(String keyword) {
        Promise<Optional<List<Product>>> promise = Promise.promise();

        JsonObject regexKeyword = new JsonObject().put("$regex", keyword);
        JsonObject brandQuery = new JsonObject().put("brand", regexKeyword);
        JsonObject descriptionQuery = new JsonObject().put("description", regexKeyword);
        JsonArray orQuery = new JsonArray().add(brandQuery).add(descriptionQuery);

        client.find("products", new JsonObject().put("$or", orQuery), res -> {
            if (res.succeeded()) {
                List<Product> products = res
                    .result()
                    .stream()
                    .map(json -> jsonToProduct(json))
                    .collect(Collectors.toList());
                promise.complete(Optional.ofNullable(products));
            } else {
                promise.fail(res.cause());
            }
        });
        return promise.future();
    }

    @Override
    public Future<Optional<Product>> getProductById(Long id) {
        Promise<Optional<Product>> promise = Promise.promise();
        client.findOne("products", new JsonObject().put("id", id), null, res -> {
            if (res.succeeded()) {
                Optional<Product> optionaProduct = Optional
                    .ofNullable(res.result())
                    .map(value -> jsonToProduct(value));
                promise.complete(optionaProduct);
            } else {
                promise.fail(res.cause());
            }
        });
        return promise.future();
    }

    private Product jsonToProduct(JsonObject jsonProduct) {

        return new Product(
            jsonProduct.getLong("id"),
            jsonProduct.getString("brand"),
            jsonProduct.getString("description"),
            jsonProduct.getString("image"),
            jsonProduct.getInteger("price"),
            jsonProduct.getInteger("price"));
    }
}
