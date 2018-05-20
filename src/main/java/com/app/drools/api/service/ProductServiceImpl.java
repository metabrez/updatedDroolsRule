package com.app.drools.api.service;

import com.app.drools.api.listener.TrackingAgendaEventListener;
import com.app.drools.api.model.Product;
import com.app.drools.api.repo.ProductRepo;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.internal.utils.KieService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo productRepo;
	private KieContainer kieContainer;
	// private final KieSession kSession;

	private List<Integer> ruleIdList;

	@Autowired
	public ProductServiceImpl(KieContainer kieContainer) {
		this.kieContainer = kieContainer;

	}

	@Override
	public void applyDiscount(Product product) {
		KieSession kSession = kieContainer.newKieSession("ksession-rule");
		AgendaEventListener trackingAgendaEventListener = new TrackingAgendaEventListener();
		
		kSession.insert(product);
		// read excel data (date) from kSession and compare with product.purchaseDate.
		
		kSession.addEventListener(trackingAgendaEventListener);
		int firedRules = kSession.fireAllRules();
		//System.out.println(" Total number of Fired Rules are : " + firedRules);

		ruleIdList = ((TrackingAgendaEventListener) trackingAgendaEventListener).getRuleId();

		kSession.dispose();

	}

	@Override
	public List<Integer> getRuleIdList() {
		return ruleIdList;
	}

	/*@Override
	public Product save(Product product) {
		
		return productRepo.save(product);
		
	}*/

	@Override
	public List<Product> findAll() {
		return productRepo.findAll();
	}

	@Override
	public List<Product> save(List<Product> product) {
		return productRepo.save(product);
	}
}
