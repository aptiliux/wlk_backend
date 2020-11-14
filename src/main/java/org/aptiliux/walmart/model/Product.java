/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aptiliux.walmart.model;

import java.util.Objects;

/**
 *
 * @author aptiliux
 */
public record Product(
    Long id,
    String brand,
    String description,
    String image,
    Integer price,
    Integer referencePrice) {

    public Product      {
        Objects.requireNonNull(id);
        Objects.requireNonNull(brand);
        Objects.requireNonNull(description);
        Objects.requireNonNull(image);
        Objects.requireNonNull(price);
        Objects.requireNonNull(referencePrice);
    }
}
