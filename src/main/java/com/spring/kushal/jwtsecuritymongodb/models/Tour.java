package com.spring.kushal.jwtsecuritymongodb.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "tours")

public class Tour {
	
	@Id
	private String id;
	
	@NotBlank
	private String name;
	
	private String description;
	
	private boolean active;
	
	@JsonFormat(pattern="dd-MMM-yyyy HH:mm")
	private Date date;
	
	 @DBRef(lazy = true)
	//@JsonIgnoreProperties(value = "createdBy.tours")
	private User createdBy; 
	
	 @DBRef()
	//@JsonIgnoreProperties(value = "tours")
	private Set<User> participants =new HashSet<User>(); 
	
	private Set<Balance> balance=new HashSet<Balance>();
	
	private Set<Expenditure> expenditure =new HashSet<Expenditure>();
	
	//constuctor
	public Tour() {
		super();
	}

	public Tour(@NotBlank String name, String description, boolean active, User createdBy, Set<User> participants) {
		super();
		this.name = name;
		this.description = description;
		this.active = active;
		this.createdBy = createdBy;
		this.participants = participants;
	}

	public Tour(String id, @NotBlank String name, String description, boolean active, Date date, User createdBy,
			Set<User> participants, Set<Balance> balance, Set<Expenditure> expenditure) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.active = active;
		this.date = date;
		this.createdBy = createdBy;
		this.participants = participants;
		this.balance = balance;
		this.expenditure = expenditure;
	}
	
	//getter & setters
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Set<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}
	
	public void addParticipants(User participant) {
		this.participants.add(participant);
	}

	public Set<Balance> getBalance() {
		return balance;
	}

	public void setBalance(Set<Balance> balance) {
		this.balance = balance;
	}

	public void addBalance(Balance balance) {
		this.balance.add(balance);
	}
	
	public Set<Expenditure> getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(Set<Expenditure> expenditure) {
		this.expenditure = expenditure;
	}
	
	public void addExpentiture(Expenditure expenditure) {
		this.expenditure.add(expenditure);
	}
}
