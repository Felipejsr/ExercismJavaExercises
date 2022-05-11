package com.example.atvrestapi.model;

import java.util.Map;
import java.util.stream.DoubleStream;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@ElementCollection
	private Map<String, Double> owes;
	
	@ElementCollection
	private Map<String, Double> owed_by;
	
	private String balance;
	
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public String getBalance() {
		Double total_owes = 0.0;
		Double total_owed_by = 0.0;
		if(owes != null) {
			for( Double x : owes.values() )
				total_owes += x;
		}
		if(owed_by != null) {
			for( Double x : owed_by.values() )
				total_owed_by += x;
		}
		
		return String.format("%.2f - %.2f", total_owed_by, total_owes);
	}
	public Map<String, Double> getOwes() {
		return owes;
	}
	public void setOwes(Map<String, Double> owes) {
		this.owes = owes;
	}
	public Map<String, Double> getOwed_by() {
		return owed_by;
	}
	public void setOwed_by(Map<String, Double> owed_by) {
		this.owed_by = owed_by;
	}
	
	public void updateValue(Boolean isOwing, String user, Double quant) {
		if(isOwing) {
			if(owed_by.containsKey(user))
				quant += owed_by.get(user);
			owed_by.put(user, quant);
		}
		else if(!isOwing) {
			if(owes.containsKey(user))
				quant += owes.get(user);
			owes.put(user, quant);
		}
	}
	
	
}
