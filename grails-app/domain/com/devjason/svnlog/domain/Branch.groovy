package com.devjason.svnlog.domain

class Branch {
	static searchable = false
	
	String name
	
	Date dateCreated	
	Date lastUpdated
    
	static constraints = {		
		name(nullable:false)
    }
	
	public String toString() {
		return "name: $name"
	}
}
