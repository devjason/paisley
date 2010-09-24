package com.devjason.paisley

import grails.converters.JSON
import grails.converters.XML;

import com.devjason.svnlog.domain.Branch;

class SearchController {

    def index = { }
	
	def search = {
		println params
		println Branch.get(1) as XML
		render Branch.get(1) as XML		
	}
}
