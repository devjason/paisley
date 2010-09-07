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
}
