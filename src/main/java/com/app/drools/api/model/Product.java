package com.app.drools.api.model;

public class Product {

    private String type;
    private String quality;
    private String made;
   
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

    public Product() {

    }

    public Product(String type, int discount) {
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
		return "Product [type=" + type + ", quality=" + quality + ", made=" + made + "]";
	}
    

}