var DASHBOARD = {};

DASHBOARD.fceJIRALinks = function() {
	$('.google-visualization-table-table tr td:last-child').each(function() {
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
		// JIRA number is somewhere in the middle of the message but no link
		$element = $(this);
		$.each(['FCE-', 'WBG-', 'CP-', 'FCA-'], function(index, value) {
			if (svnMessage.toLowerCase().indexOf(value.toLowerCase()) != -1) {
				DASHBOARD.addLink($element, svnMessage,value);
			}
		})		
	});		
}

DASHBOARD.addLink = function($element, svnMessage, prefix) {
	var jiraPrefix = "<a href='http://jira.justicesystems.com/jira/browse/";
	var jiraSuffix = "' class='jiraLink' target='_window'>";
	var jiraEnd = "</a>";
	
	var svnFCEPartToWrap = '';
	var svnNonFCEBefore = svnMessage.substring(0,svnMessage.indexOf(prefix));
	//
	// message starts with JIRA number
	if (svnNonFCEBefore == '') {						
		if (svnMessage.indexOf(' ') == -1) {
			// 
			// nothing in the message but JIRA number
			$element.html(jiraPrefix + svnMessage + jiraSuffix + svnMessage + jiraEnd);
		} else {
			//
			// JIRA number plus additional text after the JIRA number
			var jiraNumber = svnMessage.substring(0,svnMessage.indexOf(' '));
			$element.html(jiraPrefix + jiraNumber.replace(':','') + jiraSuffix + jiraNumber + jiraEnd + svnMessage.substring(svnMessage.indexOf(' ')+1));
		}
	}
	// message contains JIRA number somewhere 
	else {						
		var svnMessageAfter = svnMessage.substring(svnNonFCEBefore.length-1);						
		if (svnMessageAfter.indexOf(' ') == -1) {
			$element.html(svnNonFCEBefore + jiraPrefix + svnMessageAfter + jiraSuffix + svnMessageAfter + jiraEnd);
		} else {
			//
			// JIRA number plus additional text after the JIRA number
			svnMessageAfter = svnMessageAfter.substring(1);
			var jiraNumber = svnMessageAfter.substring(0,svnMessageAfter.indexOf(' '));
			if (jiraNumber == '') {
				jiraNumber = svnMessageAfter;
				svnMessageAfter = '';
			}
			$element.html(svnNonFCEBefore + 
					jiraPrefix + 
					jiraNumber.replace(':','') + 
					jiraSuffix + 
					jiraNumber + 
					jiraEnd + 
					svnMessageAfter.substring(svnMessageAfter.indexOf(' ')+1));
		}
	}
}

$(function() {
	//
	// display JIRA links
	setTimeout(DASHBOARD.fceJIRALinks, 500);
	
	//
	// add "All Branches option and page load and select it
	$('<option value="0">All Branches</option>').attr('selected', 'selected').prependTo('#branchname');
	
	//
	// re-requery on each branch name change and re-display the commits table
	$('#branchname').change(function() {
		$branchoptions = $(this);
		var branchId = $branchoptions.val();
		var gglQuery = new google.visualization.Query(latestCommitsUrl + "/?branch=" + branchId);
		gglQuery.send(drawCommitTable);
		setTimeout(DASHBOARD.fceJIRALinks, 500);			
	});
});	
