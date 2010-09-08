<html>
<head>
	<title>Paisley Analytics</title>
	<link type="text/css" rel="stylesheet" href="${resource(dir:'css',file:'dashboard.css')}" />
	<script type="text/javascript" src="http://www.google.com/jsapi"></script>
	<script type="text/javascript">
		google.load('visualization', '1', {'packages':['corechart','table']});
		google.setOnLoadCallback(initialize);

		function initialize() {
			var query = new google.visualization.Query("${createLink(action:'monthly_commits')}");
			query.send(drawBarChart);

			var query2 = new google.visualization.Query("${createLink(action:'monthly_commits_by_branch')}");
			query2.send(drawBranchChart);

			var query3 = new google.visualization.Query("${createLink(action:'latest_commits')}");
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
			var chart = new google.visualization.Table(
				document.getElementById('commit_table_div'));
			var cssClassNames = {headerRow:'dtHdr', tableRow:'dtRow',
					selectedTableRow:'dtSelectedRow', hoverTableRow:'dtHoverRow',
					headerCell:'dtHdrCell', tableCell:'dtCell'};
			var chartOptions = {
					title:'Recent Commits',
					alternatingRowStyle:false,
					cssClassNames:cssClassNames,					
			};
			chart.draw(data, chartOptions);
		};
	</script>
</head>
<body>
	<h1>FCE SVN Analytics</h1>
	<div id="commit_table_div" class="datatable"></div>
	<div id="chart_div" class="chart"></div>
	<div id="branch_commits_div" class="chart"></div>
</body>
</html>