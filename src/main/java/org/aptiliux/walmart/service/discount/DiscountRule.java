/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aptiliux.walmart.service.discount;

import org.aptiliux.walmart.model.Product;

/**
 *
 * @author aptiliux
 */
public interface DiscountRule {
    void keywordBaseDiscount(String keyword);
    void productBaseDiscount(Product product);
    float getDiscount();
}
