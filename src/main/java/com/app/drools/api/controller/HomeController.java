package com.app.drools.api.controller;

import com.app.drools.api.model.Product;
import com.app.drools.api.service.ProductService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }
    
    //http://localhost:8080/app/drools/api/getDiscount/type/diamond
    //http://localhost:8080/app/drools/api/getDiscount/type?type=diamond&quality=a&made=uk
    

    @GetMapping(value = "/getDiscount/type", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Product> getDiscount(
           /* @ApiParam(value = "Value for Product Type", required = true)*/ 
    		@RequestParam(required=true) String type,
           @RequestParam(required=true) String quality,@RequestParam(required=true)String made
    		) {
        Product product = new Product();
        product.setType(type);
        product.setQuality(quality);
        product.setMade(made);
        productService.applyDiscount(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
