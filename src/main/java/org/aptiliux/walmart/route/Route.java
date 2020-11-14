/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aptiliux.walmart.route;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import java.util.ArrayList;
import java.util.List;
import org.aptiliux.walmart.model.Product;
import org.aptiliux.walmart.service.ProductService;
import org.aptiliux.walmart.utils.Utils;

/**
 *
 * @author aptiliux
 */
public class Route {

    private static JsonMapper jsonMapper = Utils.getJsonMapper();
    private ProductService productService;

    private Route(ProductService productService) {
        this.productService = productService;
    }

    public static Router getRouter(Vertx vertx, ProductService productoService) {
        Route route = new Route(productoService);
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(CorsHandler.create("*")
            .allowedMethod(io.vertx.core.http.HttpMethod.GET)
            .allowedMethod(io.vertx.core.http.HttpMethod.POST)
            .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
            .allowedHeader("Access-Control-Request-Method")
            .allowedHeader("Access-Control-Allow-Credentials")
            .allowedHeader("Access-Control-Allow-Origin")
            .allowedHeader("Access-Control-Allow-Headers")
            .allowedHeader("Content-Type"));

        router.get("/products/search").handler(route::handleSearchProduct);
        router.get("/products").handler(route::handleGetProduct);
        return router;
    }

    private void handleSearchProduct(RoutingContext routingContext) {
        String query = routingContext.request().getParam("query");

        HttpServerResponse response = routingContext.response();
        if (query == null) {
            response.setStatusCode(400).end();
        } else {
            try {
                productService.searchProduct(query).onComplete(optionalProducts -> {
                    if (optionalProducts.succeeded()) {
                        try {
                            List<Product> products = optionalProducts.result().orElse(new ArrayList());
                            String jsonProducts = jsonMapper.writeValueAsString(products);
                            response.putHeader("content-type", "application/json").end(jsonProducts);
                        } catch (Exception e) {
                            response.setStatusCode(500).end();
                        }
                    } else {
                        response.setStatusCode(500).end();
                    }
                });
            } catch (Exception e) {
                response.setStatusCode(500).end();
            }
        }
    }

    private void handleGetProduct(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        productService.getProducts().onComplete(optionalProducts -> {
            if (optionalProducts.succeeded()) {
                try {
                    List<Product> products = optionalProducts.result().orElse(new ArrayList());
                    String jsonProducts = jsonMapper.writeValueAsString(products);
                    response.putHeader("content-type", "application/json").end(jsonProducts);
                } catch (Exception e) {
                    response.setStatusCode(500).end();
                }
            } else {
                response.setStatusCode(500).end();
            }
        });
    }
}
