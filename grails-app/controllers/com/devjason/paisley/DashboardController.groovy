package com.devjason.paisley

import grails.converters.JSON
import groovy.sql.Sql

class DashboardController {
	//static layout = 'main'
	def dataSource

    def index = { }
	
	def monthly_commits = {
		def map = [:]
		// map.version = 0.5
		map.reqId = '0'
		map.status = 'ok'
		
		def columns = []
		columns << [label: 'Month', type: 'string']
		columns << [label: 'Commits', type: 'number']
		
		def db = new Sql(dataSource)
		def results = db.rows(
				"select year(revision_time) as yr, " +
				"month(revision_time) as mth, " +
				"count(*) as cnt " +
				"from revision " +
				"group by year(revision_time), month(revision_time) "
		)
		
		def rows = []
		def cells
		results.each {
			cells = []
			cells << [v: "${it.mth}-${it.yr}"] << [v: it.cnt]
			rows << ['c': cells]
		}
		
		def table = [cols: columns, rows: rows]
		map.table = table
		render "google.visualization.Query.setResponse(" + (map as JSON) + ")"
	}
	
	
	
	def monthly_commits_by_branch = {
		def db = new Sql(dataSource)
		
		// Setup Google Response object
		def map = [:]
		// map.version = 0.5
		map.reqId = '1'
		map.status = 'ok'
		
		// Query for active branches
		def results = db.rows(
			"select distinct b.name " +
			"from branch b " +
			"join revision_entry re on b.id = re.branch_id " +
			"join revision r on re.revision_id = r.id " +
			"where r.revision_time > DATE_SUB(CURDATE(), INTERVAL 6 WEEK) " +
			"order by b.name asc"
		)
		def branches = results.collect { it.name }
		def columns = []
		columns << [label: 'Month', type: 'string']
		branches.each {
			columns << [label: it, type: 'number']
		}
		
		// Query for commit counts
		results = db.rows(
			"select year(revision_time) as yr, " +
			"month(revision_time) as mth, " +
			"day(revision_time) as day, " +
			"count(*) as cnt, " +
			"b.name as name " +
			"from revision " +
			"join revision_entry re on re.revision_id = revision.id " +
			"join branch b on re.branch_id = b.id " +
			"where revision_time > DATE_SUB(CURDATE(), INTERVAL 6 WEEK) " +
			"group by year(revision_time), month(revision_time), day(revision_time), b.name"
		)
		def min = {a,b -> a < b ? a : b }
		def rows = []
		def cells
		results.each {
			cells = []
			cells << [v: "${it.mth}-${it.day}-${it.yr}"]
			branches.each { b ->
				if (b.equalsIgnoreCase(it.name)) {
					cells << [v: it.cnt]
				} else {
					cells << [v: 0]
				}
			}
			rows << ['c': cells]
		}
		
		def table = [cols: columns, rows: rows]
		map.table = table
		render "google.visualization.Query.setResponse(" + (map as JSON) + ")"
	}
	

	def latest_commits = {
		def db = new Sql(dataSource)
			
		// Query for active branches
		def results = db.rows(
			"select revision_number, revision_time, author, message " +
			"from revision order by revision_time desc limit 0,10"
		)
		def columns = []
		columns << [label: 'Number', type: 'number']
		columns << [label: 'Time', type: 'datetime']
		columns << [label: 'Author', type: 'string']
		columns << [label: 'Message', type: 'string']
		
		def rows = []
		def cells
		results.each {
			cells = []
			cells << [v: it.revision_number] << [v: it.revision_time]
			cells << [v: it.author] << [v: it.message]
			rows << ['c': cells]
		}
		
		def table = [cols: columns, rows: rows]
		
		// Setup Google Response object
		def map = [:]
		map.reqId = '2'
		map.status = 'ok'
		map.table = table
		render "google.visualization.Query.setResponse(" + (map as JSON) + ")"
	}	
}
