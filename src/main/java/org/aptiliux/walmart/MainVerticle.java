package org.aptiliux.walmart;

import io.vertx.core.Vertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.aptiliux.walmart.db.MongoConnection;
import org.aptiliux.walmart.repository.impl.mongo.ProductRepositoryImpl;
import org.aptiliux.walmart.route.Route;
import org.aptiliux.walmart.service.ProductService;
import org.aptiliux.walmart.service.discount.DiscountRule;
import org.aptiliux.walmart.service.discount.PalindromeRule;
import org.aptiliux.walmart.service.impl.DiscountService;
import org.aptiliux.walmart.service.impl.ProductServiceImpl;

public class MainVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        ProductService productService
            = new ProductServiceImpl(
                new ProductRepositoryImpl(
                    MongoConnection.getConnection(vertx)));

        DiscountRule palindromeDiscountRule = new PalindromeRule(0.5f);
        ProductService productWithDiscountService 
            = new DiscountService(productService, palindromeDiscountRule);

        vertx.createHttpServer()
            .requestHandler(Route.getRouter(vertx, productWithDiscountService))
            .listen(8888, http -> {
                if (http.succeeded()) {
                    startPromise.complete();
                    System.out.println("HTTP server started on port 8888");
                } else {
                    startPromise.fail(http.cause());
                }
            });
    }
}
