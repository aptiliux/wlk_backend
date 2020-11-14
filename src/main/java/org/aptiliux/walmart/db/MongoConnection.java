/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aptiliux.walmart.db;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aptiliux
 */
public class MongoConnection {

    public static MongoClient getConnection(Vertx vertx) {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("host", "localhost");
        configMap.put("port", 27017);
        configMap.put("username", "productListUser");
        configMap.put("password", "productListPassword");
        configMap.put("db_name", "promotions");
        configMap.put("authSource", "admin");

        JsonObject configJson = new JsonObject(configMap);
        MongoClient mongoDBClient = MongoClient.createShared(vertx, configJson);
        return mongoDBClient;
    }
}
