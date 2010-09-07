package com.devjason.svnlog.service;

import groovy.util.XmlSlurper;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.devjason.svnlog.domain.Branch;
import com.devjason.svnlog.domain.Revision;
import com.devjason.svnlog.domain.RevisionEntry;

class SvnLogExtractorService {
	
	static transactional = true	
	static final revisionDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")	// not thread safe
	
	def saveLogEntry(logEntry) {
		Revision revision = new Revision([
					revisionNumber : Integer.valueOf(logEntry.@revision.text()), 
					author : logEntry.author.text(),
					revisionTime : convertSvnDateStringToDate(logEntry.date.text()),
					message : logEntry.msg.text() ])
		
		createRevisionEntries(logEntry.paths, revision)
		
		if(!revision.save(flush:true)) {					
			log.error("saving revision ${revision.revisionNumber} failed")
			revision.errors.allErrors.each {
				log.error("error: $it")
			}
		}
		revision
	}
	
	def createRevisionEntries(paths, revision) {
		def revisionEntries = []		
		paths.path.list().each { path ->
			def revisionEntry = createRevisionEntry(path)
			if (revisionEntry) {
				revision.addToRevisionEntries(revisionEntry)
			}
		}
	}
	
	def createRevisionEntry(path) {
		if (pathTooSmall(path.text())) {
			return null
		}
		return new RevisionEntry([
			path : path.text(),
			branch : locateOrCreateBranch(path.text()),
			action : path.@action.text(),
			project : getProjectFromPath(path.text()),
			artifact : getArtifactFromPath(path.text())
		])
	}
	
	def pathTooSmall(path) {
		def tokens = path.tokenize ("/")
		return tokens.size() <= 3
	}
	
	def locateOrCreateBranch(String path) {
		def tokens = path.tokenize ("/")
		Branch branch = Branch.findByName(tokens[2])
		if (!branch) {
			branch = new Branch(name:tokens[2])
			branch.save(flush:true)
		}
		branch
	}
	
	def getProjectFromPath(String path) {
		def tokens = path.tokenize ("/")
		return tokens[1]
	}
	
	def getArtifactFromPath(String path) {
		int idx = 0
		4.times {
			idx = path.indexOf('/')
			path = path.substring(idx+1, path.length())
		}
		return path
	}
	
	def convertSvnDateStringToDate(String revisionDate) {
		Calendar c = Calendar.getInstance()
		c.setTime(revisionDateFormat.parse(revisionDate))
		c.set(Calendar.ZONE_OFFSET, 0)
		c.getTime()
	}
}
