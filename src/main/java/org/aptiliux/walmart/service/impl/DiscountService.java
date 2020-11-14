/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aptiliux.walmart.service.impl;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.aptiliux.walmart.model.Product;
import org.aptiliux.walmart.service.ProductService;
import org.aptiliux.walmart.service.discount.DiscountRule;

/**
 *
 * @author aptiliux
 */
public class DiscountService implements ProductService {

    private ProductService productServiceRef;
    private DiscountRule discountRule;

    public DiscountService(ProductService productService, DiscountRule discountRule) {
        this.productServiceRef = productService;
        this.discountRule = discountRule;
    }

    public Future<Optional<List<Product>>> searchProduct(String keyword) {
        discountRule.keywordBaseDiscount(keyword);

        return productServiceRef.searchProduct(keyword).compose(optionalProducts -> {
            Promise<Optional<List<Product>>> promise = Promise.promise();
            try {
                Optional<List<Product>> productsWithDiscount = optionalProducts
                    .map(products
                        -> Optional.of(products
                        .stream()
                        .map(product -> {
                            discountRule.productBaseDiscount(product);
                            return applyDiscount(product, discountRule.getDiscount());
                        })
                        .collect(Collectors.toList())))
                    .orElse(Optional.empty());
                promise.complete(productsWithDiscount);
            } catch (Exception e) {
                promise.fail(e);
            }
            return promise.future();
        });
    }

    public Future<Optional<List<Product>>> getProducts() {
        return productServiceRef.getProducts();
    }

    private Product applyDiscount(Product product, float discount) {
        if (discount == 0) {
            return product;
        }
        return new Product(
            product.id(),
            product.brand(),
            product.description(),
            product.image(),
            calculateDiscount(
                product.price(),
                product.referencePrice(),
                discount),
            product.referencePrice());
    }

    private Integer calculateDiscount(Integer price, Integer referencePrice, float discount) {
        if (referencePrice == null) {
            return price;
        }
        float discountOperator = 1 - discount;
        return Math.round(referencePrice * discountOperator);
    }
}
