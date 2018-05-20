package com.app.drools.api.controller;

import com.app.drools.api.listener.TrackingAgendaEventListener;
import com.app.drools.api.model.Product;
import com.app.drools.api.model.ProductResponse;
import com.app.drools.api.service.ProductService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import io.swagger.annotations.ApiParam;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

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

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
	}
	private AgendaEventListener trackingAgendaEventListener;

	// private FactHandle fact;

	@Autowired
	public HomeController(ProductService productService) {
		this.productService = productService;

	}

	
	@GetMapping(value = "/getDiscount/type", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ProductResponse> getDiscount(
			/* @ApiParam(value = "Value for Product Type", required = true) */
			@RequestParam(required = true) String type, @RequestParam(required = true) String quality,
			@RequestParam(required = true) String made, @RequestParam(value = "price", required = false) Integer price,
			@RequestParam(value = "purchasedDate", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") Date purchasedDate)
			throws ParseException {

		Product product = new Product();
		product.setType(type);
		product.setQuality(quality);
		product.setMade(made);
		product.setPrice(price);
		
		product.setPurchasedDate(purchasedDate);
		System.out.println("Date Printing" + product.getPurchasedDate());

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
	public ResponseEntity<List<Product>> save(@RequestBody List<Product> product) {

		return  new ResponseEntity<>(productService.save(product),HttpStatus.CREATED);
	}

	@GetMapping(value = "getDiscount/type/product")
	public List<ProductResponse> getAllProduct() {

		List<Product> inputProducts = productService.findAll();
		List<ProductResponse> outputAfterRulefire = new ArrayList<>();

		for (Product product : inputProducts) {

			productService.applyDiscount(product);
			// productService.getRuleIdList();
			List<Integer> ruleIdList = productService.getRuleIdList();
			ProductResponse response = new ProductResponse();
			response.setType(product.getType());
			response.setQuality(product.getQuality());
			response.setMade(product.getMade());
			response.setPrice(product.getPrice());
			response.setPurchasedDate(product.getPurchasedDate());
			response.setDiscount(product.getDiscount());
			response.setRule(ruleIdList);
			outputAfterRulefire.add(response);
		}

		// System.out.println("product" +product);

		return outputAfterRulefire;
	}

	@InitBinder
	public void initBinder(final WebDataBinder binder) {

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}



}
