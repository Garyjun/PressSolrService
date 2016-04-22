package com.brainsoon.solr.util;

import java.util.HashMap;
import java.util.Map;

public class Category {

	private String name;
	
	private Map<String,Long> fields = new HashMap<String,Long>();
	
	public Category() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Long> getFields() {
		return fields;
	}

	public void setFields(Map<String, Long> fields) {
		this.fields = fields;
	}

}
