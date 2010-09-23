var DASHBOARD = {};

DASHBOARD.fceJIRALinks = function() {
	$('.google-visualization-table-table tr td:last-child').each(function() {
		var jiraPrefix = "<a href='http://jira.justicesystems.com/jira/browse/";
		var jiraSuffix = "' class='jiraLink' target='_window'>";
		var jiraEnd = "</a>";
						
		var svnMessage = $(this).text(); 

		//
		// there is a link to JIRA, wrap it with an achor
		if (svnMessage.indexOf('http://jira.justicesystems.com') != -1) {					
			var link = '';
			var afterLink = '';
			var beforeLink = svnMessage.substring(0, svnMessage.indexOf('http://jira.justicesystems.com'));
			var tmpAfterLink =  svnMessage.substring(svnMessage.indexOf('http://jira.justicesystems.com'));
			if (tmpAfterLink.indexOf(' ') != -1) {
				link = tmpAfterLink.substring(0,tmpAfterLink.indexOf(' ')); 
			} else {
				link = tmpAfterLink;
			}
			$(this).html(beforeLink + "<a href='" + link + "' class='jiraLink' target='_window'>" + link.substring(link.lastIndexOf("/")+1) + " </a> " + afterLink);  
		}
		//
		// FCE is somewhere in the middle of the message but no link
		else if (svnMessage.indexOf("FCE-") != -1) {
			var svnFCEPartToWrap = '';
			var svnNonFCEBefore = svnMessage.substring(0,svnMessage.indexOf("FCE-"));
			//
			// message starts with JIRA number
			if (svnNonFCEBefore == '') {						
				if (svnMessage.indexOf(' ') == -1) {
					// 
					// nothing in the message but JIRA number
					$(this).html(jiraPrefix + svnMessage + jiraSuffix + svnMessage + jiraEnd);
				} else {
					//
					// JIRA number plus additional text after the JIRA number
					var jiraNumber = svnMessage.substring(0,svnMessage.indexOf(' '));
					$(this).html(jiraPrefix + jiraNumber.replace(':','') + jiraSuffix + jiraNumber + jiraEnd + svnMessage.substring(svnMessage.indexOf(' ')+1));
				}
			}
			// message contains JIRA number somewhere 
			else {						
				var svnMessageAfter = svnMessage.substring(svnNonFCEBefore.length-1);						
				if (svnMessageAfter.indexOf(' ') == -1) {
					alert('setting this to be html, nothing in the string except JIRA # after: ' +
							svnNonFCEBefore + jiraPrefix + svnMessageAfter + jiraSuffix + svnMessageAfter + jiraEnd);
					$(this).html(svnNonFCEBefore + jiraPrefix + svnMessageAfter + jiraSuffix + svnMessageAfter + jiraEnd);
				} else {
					//
					// JIRA number plus additional text after the JIRA number
					svnMessageAfter = svnMessageAfter.substring(1);
					var jiraNumber = svnMessageAfter.substring(0,svnMessageAfter.indexOf(' '));
					if (jiraNumber == '') {
						jiraNumber = svnMessageAfter;
						svnMessageAfter = '';
					}
					$(this).html(svnNonFCEBefore + 
							jiraPrefix + 
							jiraNumber.replace(':','') + 
							jiraSuffix + 
							jiraNumber + 
							jiraEnd + 
							svnMessageAfter.substring(svnMessageAfter.indexOf(' ')+1));
				}
			}
		}  
	});	
}

jQuery(document).ready(function() {
	setTimeout(DASHBOARD.fceJIRALinks, 500);

	/*
	jQuery('tr.dtHoverRow').live('hover', function() {
	}, function() {
	});
	*/
});	