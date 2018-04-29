package com.app.drools.api.model;

import java.util.List;

public class ProductResponse {
	
	private String type;
    private String quality;
    private String made;
    private String price;
    
    private List<Integer> rule;
   
    public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getMade() {
		return made;
	}

	public void setMade(String made) {
		this.made = made;
	}

	private int discount;
  //  private String ruleName;

    public ProductResponse() {

    }

    public ProductResponse(String type, int discount) {
        this.type = type;
        this.discount = discount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

	@Override
	public String toString() {
		return "ProductResponse [type=" + type + ", quality=" + quality + ", made=" + made + ", price=" + price
				+ ", rule=" + rule + ", discount=" + discount + "]";
	}

	public List<Integer> getRule() {
		return rule;
	}

	public void setRule(List<Integer> rule) {
		this.rule = rule;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
