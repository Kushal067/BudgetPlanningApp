package com.spring.kushal.jwtsecuritymongodb.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Document(collection = "expenditures")
public class Expenditure {
	
	@Id
	private String id;
	
	@DBRef
	@JsonIgnoreProperties({"tours","profilePicture"})
	private User spentBy;
	
	@DBRef
	@JsonIgnoreProperties({"tours","profilePicture"})
	private Set<User> splitBillOn=new HashSet<User>();
	
	private double amountSpent;
	
	private String billNote;
	
	//@JsonFormat(pattern="dd-MM-yyyy HH:mm")
	private Date billDate;

	//Constructor
	public Expenditure( User spentBy, Set<User> splitBillOn, long amountSpent, String billNote) {
		super();
		
		this.spentBy = spentBy;
		this.splitBillOn = splitBillOn;
		this.amountSpent = amountSpent;
		this.billNote = billNote;
	//	this.billDate = billDate;
	}

	public Expenditure() {
		super();
	}
	//getter & Setter

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getSpentBy() {
		return spentBy;
	}

	public void setSpentBy(User spentBy) {
		this.spentBy = spentBy;
	}

	public Set<User> getSplitBillOn() {
		return splitBillOn;
	}

	public void setSplitBillOn(Set<User> splitBillOn) {
		this.splitBillOn = splitBillOn;
	}

	public double getAmountSpent() {
		return amountSpent;
	}

	public void setAmountSpent(double amountSpent) {
		this.amountSpent = amountSpent;
	}

	public String getBillNote() {
		return billNote;
	}

	public void setBillNote(String billNote) {
		this.billNote = billNote;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	
}
