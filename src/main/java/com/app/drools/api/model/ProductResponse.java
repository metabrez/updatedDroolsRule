package com.app.drools.api.model;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ProductResponse {
	
	private String type;
    private String quality;
    private String made;
    private String price;
   @DateTimeFormat(pattern="dd-mm-yyyy")
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date purchasedDate;
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

	public Date getPurchasedDate() {
		return purchasedDate;
	}

	public void setPurchasedDate(Date purchasedDate) {
		this.purchasedDate = purchasedDate;
	}

	@Override
	public String toString() {
		return "ProductResponse [type=" + type + ", quality=" + quality + ", made=" + made + ", price=" + price
				+ ", purchasedDate=" + purchasedDate + ", rule=" + rule + ", discount=" + discount + "]";
	}

}
