package com.spring.kushal.jwtsecuritymongodb.models;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Balance {
	
	@DBRef
	@JsonIgnoreProperties(value = {"tours","profilePicture"})
	private User user;
	
	private double balance;
	
	private double share;
	
	private double totalAmountSpent;
	//constructor

	public Balance(User user, long balance, long share, long totalAmountSpent) {
		super();
		this.user = user;
		this.balance = balance;
		this.share = share;
		this.totalAmountSpent = totalAmountSpent;
	}
	public Balance() {
		super();
	}
	//Getter & Setter
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getShare() {
		return share;
	}
	public void setShare(double share) {
		this.share = share;
	}
	public double getTotalAmountSpent() {
		return totalAmountSpent;
	}
	public void setTotalAmountSpent(double totalAmountSpent) {
		this.totalAmountSpent = totalAmountSpent;
	}
	
}
