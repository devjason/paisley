<html>
<head>
	<title>Paisley Analytics</title>
	<link type="text/css" rel="stylesheet" href="${resource(dir:'css',file:'dashboard.css')}" />	
	<script type="text/javascript" src="http://www.google.com/jsapi"></script>
	<script type="text/javascript">
		google.load('visualization', '1', {'packages':['corechart','table']});
		google.setOnLoadCallback(initialize);
		var latestCommitsUrl = "${createLink(action:'latest_commits')}";
		function initialize() {
			var query = new google.visualization.Query("${createLink(action:'monthly_commits')}");
			query.send(drawBarChart);

			var query2 = new google.visualization.Query("${createLink(action:'monthly_commits_by_branch')}");
			query2.send(drawBranchChart);

			var query3 = new google.visualization.Query(latestCommitsUrl);
			query3.send(drawCommitTable);
		}
		function drawBarChart(response) {			
			var data = response.getDataTable();
			var chart = new google.visualization.ColumnChart(
				document.getElementById('chart_div'));
			var chartOptions = {
					legend:'none', title:'Monthly Commit Volume',
					backgroundColor:'black',
					titleTextStyle:{color:'yellow'},
					hAxis:{textStyle:{color:'white'}},
					vAxis:{textStyle:{color:'white'}, baselineColor:'white'},
			};
			chart.draw(data, chartOptions);
		};
		
		function drawBranchChart(response) {			
			var data = response.getDataTable();
			var chart = new google.visualization.ColumnChart(
				document.getElementById('branch_commits_div'));
			var chartOptions = {
					title:'Branch Commits Last Six Weeks',
					backgroundColor:'black',
					titleTextStyle:{color:'yellow'},
					hAxis:{textStyle:{color:'white'}},
					vAxis:{textStyle:{color:'white'}, baselineColor:'white', logScale: true},
					legendTextStyle:{color:'white', fontSize:8},
					isStacked:true,
			};
			chart.draw(data, chartOptions);
		};
		function drawCommitTable(response) {			
			var data = response.getDataTable();
			var cssClassNames = {headerRow:'dtHdr', tableRow:'dtRow',
					selectedTableRow:'dtSelectedRow', hoverTableRow:'dtHoverRow',
					headerCell:'dtHdrCell', tableCell:'dtCell'};
			var chartOptions = {
					title:'Recent Commits',
					alternatingRowStyle:false,
					cssClassNames:cssClassNames,					
			};
			var short_date = new google.visualization.DateFormat({pattern:'MM/dd/yy HH:mm'});
			short_date.format(data, 1);	// format revisiontime
			var chart = new google.visualization.Table(
					document.getElementById('commit_table_div'));
			chart.draw(data, chartOptions);
		};
	</script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.5/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${resource(dir:'js',file:'dashboard.js')}"></script>
	<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.5/themes/sunny/jquery-ui.css" />	
</head>
<body>
	<div id="header">FCE Subversion Analytics</div>
	<div id="commits_wrapper" class="datatable">
		<form>
    		<g:select 
    			optionKey="id" optionValue="name" name="branch.name" id="branchname" class="ui-state-active" style="padding : 3px;"
    			from="${com.devjason.svnlog.domain.Branch.listOrderByDateCreated(order:'desc')}"/>                
		</form>
		<br/>
		<div id="commit_table_div"></div>
	</div>
	<div id="chart_div" class="chart"></div>
	<div id="branch_commits_div" class="chart"></div>
</body>
</html>