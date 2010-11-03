package com.devjason.paisley

import com.devjason.svnlog.domain.Revision 
import groovy.sql.Sql
import grails.converters.XML 

class SearchController {
	def dataSource
	
	def index = {
	}
	
	def search = {
		def select = "select distinct revision.id, revision.revision_time "
		def where = []
		def tables = ['revision']
		
		def filters = params.filter
		def stringMatchTypes = params.stringMatchType
		def terms = params.term
		
		def numberRangeTo = params.numberRange2
		def numberRangeFrom = params.numberRange1
		
		def dateRangeTo = params.dateRange2
		def dateRangeFrom = params.dateRange1
		
		def index = 0
		filters = filters.sort()
		filters.each { paramIndex, filter ->
			switch(filter) {
				case ['revision.author', 'revision.message', 'branch.name', 'revision.message', 'revision_entry.artifact', 'revision_entry.project'] :
					doStringMatching(where, tables, paramIndex, filter, terms.get(paramIndex), stringMatchTypes.get(paramIndex))					
					break
				
				case 'revision.revision_number' :
					doNumberRangeMatching(where, tables, paramIndex, filter, numberRangeFrom, numberRangeTo)
					break
					
				case 'revision.revision_time' :
					doDateRangeMatching(where, tables, paramIndex, filter, dateRangeFrom, dateRangeTo)
					break 
			}			
		}		
		def joinTables = extrapolateJoin(tables)
		def from = extrapolateFromClause(joinTables[1].unique())
		def and = (joinTables[1].size() > 1) ? ' and ' : ''
		def sql = select + from + " where " + joinTables[0] + and + where.join(' and ') + " order by revision.revision_time desc"
		//println sql
		
		def db = new Sql(dataSource)
		def results = db.rows(sql)
		def revisions = []
		revisions= results.collect { Revision.get(it.id) }
		println revisions as XML
		render revisions as XML
	}
	
	private void doNumberRangeMatching(where, tables, paramIndex, filter, numberRangeFrom, numberRangeTo) {
		tables << filter.substring(0, filter.indexOf('.'))
		where << "$filter between ${numberRangeFrom.get(paramIndex)} and ${numberRangeTo.get(paramIndex)}"		
	}
	
	private void doDateRangeMatching(where, tables, paramIndex, filter, numberRangeFrom, numberRangeTo) {
		tables << filter.substring(0, filter.indexOf('.'))
		where << "$filter between '${numberRangeFrom.get(paramIndex)}' and '${numberRangeTo.get(paramIndex)}'"
	}
	
	private void doStringMatching(where, tables, paramIndex, filter, term, matchType) {
		tables << filter.substring(0, filter.indexOf('.'))
		switch(matchType) {			
			case '*' :	where << "${filter} like '%${term}%'"				
				break
			case '^' :  where << "${filter} like '${term}%'"
				break
			case '$' :  where << "${filter} like '%${term}'"
				break
			case '=' :	where << "${filter} = '${term}'" 
				break
		}
	}
	
	private String extrapolateFromClause(tables) {
		def from = "from ${tables.join(', ')} "
	}
	
	private List extrapolateJoin(tables) {
		def joinStr = ""
		if (tables.contains('branch')) {
			joinStr = " revision.id = revision_entry.revision_id and revision_entry.branch_id = branch.id"
			tables << "revision_entry"
		} else if (tables.contains('revision_entry')) {
			joinStr = " revision.id = revision_entry.revision_id "
		}
		return [joinStr, tables]
	}
}