package com.app.drools.api.service;

import com.app.drools.api.listener.TrackingAgendaEventListener;
import com.app.drools.api.model.Product;

import org.kie.api.internal.utils.KieService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

	private KieContainer kieContainer;
   // private final KieSession kSession;
    
    @Autowired
    public ProductServiceImpl(KieContainer kieContainer){
        this.kieContainer = kieContainer;
    }

    @Override
    public void applyDiscount(Product product) {
    	KieSession kSession=kieContainer.newKieSession("ksession-rule");
        kSession.insert(product);
        kSession.addEventListener(new TrackingAgendaEventListener());
       int firedRules= kSession.fireAllRules();
       System.out.println(" Total number of Fired Rules are : " + firedRules);
       
        kSession.dispose();
       
    }
}
