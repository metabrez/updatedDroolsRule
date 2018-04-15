package com.app.drools.api.service;

import java.util.List;

import com.app.drools.api.model.Product;

public interface ProductService {

    void applyDiscount(Product product);
    
    List<Integer> getRuleIdList();
}
