/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aptiliux.walmart.repository;

import io.vertx.core.Future;
import java.util.List;
import java.util.Optional;
import org.aptiliux.walmart.model.Product;

/**
 *
 * @author aptiliux
 */
public interface ProductRepository {

    Future<Optional<List<Product>>> getProducts();

    Future<Optional<List<Product>>> findProductByBrandOrDescription(String keyword);

    Future<Optional<Product>> getProductById(Long id);
}
