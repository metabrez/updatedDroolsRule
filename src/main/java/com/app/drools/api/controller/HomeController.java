package com.app.drools.api.controller;

import com.app.drools.api.listener.TrackingAgendaEventListener;
import com.app.drools.api.model.Product;
import com.app.drools.api.model.ProductResponse;
import com.app.drools.api.service.ProductService;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@Autowired
	private ProductService productService;

	private AgendaEventListener trackingAgendaEventListener;

	// private FactHandle fact;

	@Autowired
	public HomeController(ProductService productService) {
		this.productService = productService;

	}

	// http://localhost:8080/app/drools/api/getDiscount/type/diamond
	// http://localhost:8080/app/drools/api/getDiscount/type?type=diamond&quality=a&made=uk
	//http://localhost:8080/app/drools/api/getDiscount/type?type=diamond&quality=a&made=uk&price=100&purchasedDate=25-Mar-2015

	@GetMapping(value = "/getDiscount/type", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ProductResponse> getDiscount(
			/* @ApiParam(value = "Value for Product Type", required = true) */
			@RequestParam(required = true) String type, @RequestParam(required = true) String quality,
			@RequestParam(required = true) String made, @RequestParam(required = true) String price,
			@RequestParam(required = true)  @JsonFormat (shape=JsonFormat.Shape.STRING,pattern="dd-MM-yyyy")Date purchasedDate) {
		Product product = new Product();
		product.setType(type);
		product.setQuality(quality);
		product.setMade(made);
		product.setPrice(price);
		product.setPurchasedDate(purchasedDate);

		productService.applyDiscount(product);

		List<Integer> ruleIdList = productService.getRuleIdList();
		ProductResponse response = new ProductResponse();
		response.setType(product.getType());
		response.setQuality(product.getQuality());
		response.setMade(product.getMade());
		response.setPrice(product.getPrice());
		response.setPurchasedDate(product.getPurchasedDate());
		response.setDiscount(product.getDiscount());
		response.setRule(ruleIdList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/getDiscount/type/product")
	public Product save(@RequestBody Product product) {

		return productService.save(product);
	}

	@GetMapping(value = "getDiscount/type/products")
	public List<Product> getAllProduct() {
		Product p = new Product();
		p.setType(p.getType());
		p.setQuality(p.getQuality());
		p.setMade(p.getMade());
		p.setPrice(p.getPrice());
		p.setPurchasedDate(p.getPurchasedDate());
		p.setRule(p.getRule());
		p.setDiscount(p.getDiscount());
		productService.applyDiscount(p);
		return productService.findAll();
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		
		final SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,true));
	}
}
