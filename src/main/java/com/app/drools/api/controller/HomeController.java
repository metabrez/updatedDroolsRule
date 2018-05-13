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

	// before
	// http://localhost:8080/app/drools/api/getDiscount/type?type=diamond&quality=a&made=uk&price=140&purchasedDate=12-1-2014
	// http://localhost:8080/app/drools/api/getDiscount/type?type=diamond&quality=a&made=uk&price=260&purchasedDate=12-1-2014
	// after
	// http://localhost:8080/app/drools/api/getDiscount/type?type=diamond&quality=a&made=uk&price=240&purchasedDate=5-5-2018
	// equal
	// http://localhost:8080/app/drools/api/getDiscount/type?type=diamond&quality=a&made=uk&price=300&purchasedDate=8-11-2010
	@GetMapping(value = "/getDiscount/type", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ProductResponse> getDiscount(
			/* @ApiParam(value = "Value for Product Type", required = true) */
			@RequestParam(required = true) String type, @RequestParam(required = true) String quality,
			@RequestParam(required = true) String made, @RequestParam(value = "price", required = false) Integer price,
			@RequestParam(value = "purchasedDate", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") String purchasedDate)
			throws ParseException {

		Product product = new Product();
		product.setType(type);
		product.setQuality(quality);
		product.setMade(made);
		product.setPrice(price);
		/*
		 * Date defaultDate=new SimpleDateFormat("").parse("00-00-0000");
		 * product.setPurchasedDate(purchasedDate == null ? defaultDate :
		 * purchasedDate);
		 */
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
	public Product save(@RequestBody Product product) {

		return productService.save(product);
	}

	@GetMapping(value = "getDiscount/type/products")
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

	/*
	 * @InitBinder public void initBinder(WebDataBinder binder) throws Exception {
	 * final DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); final
	 * CustomDateEditor dateEditor = new CustomDateEditor(df, true) {
	 * 
	 * @Override public void setAsText(String text) throws IllegalArgumentException
	 * { if ("today".equals(text)) { setValue(null); } else { super.setAsText(text);
	 * } } }; binder.registerCustomEditor(Date.class, dateEditor); }
	 */

}
