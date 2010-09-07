package com.devjason.svnlog.domain

import java.util.Date;

class Revision {
	static searchable = false
	
	Integer revisionNumber
	Date revisionTime
	String author
	String message
	
	static hasMany = [revisionEntries : RevisionEntry]
	
	Date dateCreated
	Date lastUpdated
	
    static constraints = {
		revisionNumber(nullable:false, unique:true)
		revisionTime(nullable:false)
		author(nullable:false)
		message(nullable:true, size:0..2000)
		revisionEntries(nullable:false, minSize:1)
    }
	
	public String toString() {
		return """
		id : $id, 
		revisionNumber: $revisionNumber, 
		revisionTime: $revisionTime, 
		author: $author,
		message: $message"""
	}
}
