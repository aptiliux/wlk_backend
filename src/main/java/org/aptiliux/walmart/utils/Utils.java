/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aptiliux.walmart.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 *
 * @author aptiliux
 */
public class Utils {

    private Utils() {
    }

    public static JsonMapper getJsonMapper() {
        JsonMapper mapper = new JsonMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

    public static Long tryParseLong(String stringValue) {
        Long longValue = null;
        try {
            longValue = Long.valueOf(stringValue);
        } catch (Exception e) {
        }
        return longValue;
    }
}
