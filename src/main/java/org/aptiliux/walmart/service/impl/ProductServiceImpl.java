/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aptiliux.walmart.service.impl;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.aptiliux.walmart.model.Product;
import org.aptiliux.walmart.repository.ProductRepository;
import org.aptiliux.walmart.service.ProductService;
import org.aptiliux.walmart.utils.Utils;

/**
 *
 * @author aptiliux
 */
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Future<Optional<List<Product>>> searchProduct(String keyword) {
        Long productId = Utils.tryParseLong(keyword);

        Future<Optional<List<Product>>> searchMethod
            = productId != null
                ? findProductById(productId)
                : findProductByBrandOrDescription(keyword);

        return searchMethod.compose(optionalProducts -> {
            Promise<Optional<List<Product>>> promise = Promise.promise();
            try {
                promise.complete(optionalProducts);
            } catch (Exception e) {
                promise.fail(e);
            }
            return promise.future();
        });
    }

    public Future<Optional<List<Product>>> getProducts() {
        return productRepository.getProducts().compose(optionalProducts -> {
            Promise<Optional<List<Product>>> promise = Promise.promise();
            try {
                promise.complete(optionalProducts);
            } catch (Exception e) {
                promise.fail(e);
            }
            return promise.future();
        });
    }

    private Future<Optional<List<Product>>> findProductByBrandOrDescription(String keyword) {
        return productRepository.findProductByBrandOrDescription(keyword).compose(optionalProducts -> {
            Promise<Optional<List<Product>>> promise = Promise.promise();
            try {
                promise.complete(optionalProducts);
            } catch (Exception e) {
                promise.fail(e);
            }
            return promise.future();
        });
    }

    private Future<Optional<List<Product>>> findProductById(Long id) {
        return productRepository.getProductById(id).compose(optionalProduct -> {
            Promise<Optional<List<Product>>> promise = Promise.promise();
            try {
                promise.complete(optionalProduct
                    .map(value -> Optional.of(Collections.singletonList(value)))
                    .orElse(Optional.empty()));
            } catch (Exception e) {
                promise.fail(e);
            }
            return promise.future();
        });
    }

}
