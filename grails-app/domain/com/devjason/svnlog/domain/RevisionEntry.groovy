package com.devjason.svnlog.domain

import java.util.Date;

class RevisionEntry {
	static searchable = false
	
	String action
	String project
	String path
	String artifact
	Branch branch
	
	static belongsTo = [ revision: Revision ]
	
	Date dateCreated
	Date lastUpdated
	
    static constraints = {
		action(nullable:false)		
		project(nullable:false)
		path(nullable:false, maxSize:400)
		artifact(nullable:false, maxSize:350)
		revision(nullable:false)
    }
	
	public String toString() {
		return "action: $action, project: $project, path: $path, branch: $branch, artifact: $artifact"
	}
}
