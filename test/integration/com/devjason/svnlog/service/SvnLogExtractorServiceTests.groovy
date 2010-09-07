package com.devjason.svnlog.service

import org.springframework.orm.hibernate3.SessionFactoryUtils 

import com.devjason.svnlog.domain.Revision;
import grails.test.*

class SvnLogExtractorServiceTests extends GrailsUnitTestCase {
	
	def sessionFactory
	def svnLogExtractorService
	
	static final String INIT_LOAD_FILE = "/home/bozidar/Desktop/svnlog.xml"
	static final String SMALL_INIT_LOAD_FILE = "/home/bozidar/svnlogsmall.xml"
	
	protected void setUp() {
		super.setUp()
	}
	
	protected void tearDown() {
		super.tearDown()
	}
	
	void testSvnLogExtractorServiceInitMethod() {		
		def session = SessionFactoryUtils.getSession(sessionFactory, false)
		if (!Revision.count()) {
			File file = new File(INIT_LOAD_FILE)
			XmlSlurper slurper = new XmlSlurper()
			def logEntries = slurper.parse(file)
			logEntries.logentry.list().eachWithIndex { logentry, idx ->
				try {
					svnLogExtractorService.saveLogEntry(logentry)
				} catch (Exception e) {
					println "log entry failed > ${logentry.@revision.text()}"
					println "reason : ${e.getMessage()}"
				}
				session.clear()
			}
		}
	}
}
