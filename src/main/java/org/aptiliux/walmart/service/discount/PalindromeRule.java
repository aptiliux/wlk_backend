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
public class PalindromeRule implements DiscountRule {

    private final float baseDiscount;
    private boolean applicableDiscount;

    public PalindromeRule(float discount) {
        if (discount < 0 || discount >= 1) {
            throw new RuntimeException("invalid discount value");
        }
        baseDiscount = discount;
    }

    public void keywordBaseDiscount(String keyword) {
        applicableDiscount = false;
        boolean invalidKeyword = keyword == null || keyword.isEmpty() || keyword.length() < 3;
        if (!invalidKeyword) {
            StringBuilder sbKeyword = new StringBuilder(keyword);
            boolean isPalindrome = keyword.equalsIgnoreCase(sbKeyword.reverse().toString());
            if (isPalindrome) {
                applicableDiscount = true;
            }
        }
    }

    public void productBaseDiscount(Product product) {
    }

    public float getDiscount() {
        return applicableDiscount ? baseDiscount : 0;
    }

}
