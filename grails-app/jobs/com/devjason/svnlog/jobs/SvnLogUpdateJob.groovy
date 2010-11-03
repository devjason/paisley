package com.devjason.svnlog.jobs

import com.devjason.svnlog.domain.Revision
import org.springframework.orm.hibernate3.SessionFactoryUtils

class SvnLogUpdateJob {
	
	def sessionFactory
	def searchableService
	def svnLogExtractorService
	
	static final String SVN_COMMAND = "svn log -v -g --xml -rREV_NUMBER:HEAD http://abqlx1/fullcourtweb"
	static final String CURRENT_REVISION = "svn info -rHEAD http://abqlx1/fullcourtweb"
	static final String GREP_REVISION = "grep Revision"
	
	static triggers = {
		simple name: 'mySimpleTrigger', startDelay: 10000, repeatInterval: 1360000
	}
	
	def group = "SvnLogUpdate"
	
	def execute() {		
		log.info "starting SvnLogUpdateJob at ${new Date()}"
		def session = SessionFactoryUtils.getSession(sessionFactory, false)
		def maxRevision = Revision.createCriteria().get {
			projections { max('revisionNumber') }
		} 
		maxRevision = maxRevision ? maxRevision : 0
		def currentRevision = getCurrentSvnRevision()
		if (currentRevision <= maxRevision) {
			log.error """current revision number from Paisley: $maxRevision
			current revision number from SVN ${currentRevision}
			database is up to date"""
			return
		} else {
			log.error """current revision number from Paisley: $maxRevision
			current revision number from SVN ${currentRevision}
			performing update"""
		}
				
		for (fromRevision in maxRevision..currentRevision) {
			try {
				executeSvnProcessAndUpdate(fromRevision, fromRevision, session)				
			} catch (Exception e) {
				log.error("error injesting svn log for revision -r${fromRevision}:${fromRevision}", e)
			}
		}
		
		log.info "insert complete, forking a separate thread for indexing at ${new Date()}"
		Thread.start {
			// searchableService.index()
			log.info "ending indexing at ${new Date()}"
		}
		
		log.info "ending SvnLogUpdateJob at ${new Date()}"
	}
	
	def executeSvnProcessAndUpdate(fromRevision, toRevision, session) {
		def svnCommand = SVN_COMMAND.replace("REV_NUMBER", fromRevision.toString())
		svnCommand = svnCommand.replace("HEAD", toRevision.toString())
		
		log.error "executing command $svnCommand"
		
		def svnLogProcess = svnCommand.execute()
		def svnLogContent = svnLogProcess.text
		
		XmlSlurper slurper = new XmlSlurper()
		def logEntries = slurper.parseText(svnLogContent)
		logEntries.logentry.list().eachWithIndex { logentry, idx ->
			try {
				svnLogExtractorService.saveLogEntry(logentry)
			} catch (Exception e) {
				log.error("error injesting svn log for revision -r${fromRevision}:${toRevision}", e)
			}
			session.clear()
		}
	}
	
	def getCurrentSvnRevision() {
		def svnProcess = CURRENT_REVISION.execute()
		def grepProcess = GREP_REVISION.execute()
		
		svnProcess | grepProcess
		def revisionNumber = grepProcess.text
		def tokens = revisionNumber.tokenize()
		return Integer.parseInt(tokens[1])
	}
}
