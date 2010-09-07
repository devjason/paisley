<html>
<head>
	<title>Paisley Analytics</title>
	<link type="text/css" rel="stylesheet" href="${resource(dir:'css',file:'dashboard.css')}" />
	<script type="text/javascript" src="http://www.google.com/jsapi"></script>
	<script type="text/javascript">
		google.load('visualization', '1', {'packages':['corechart']});
		google.setOnLoadCallback(initialize);

		function initialize() {
			var query = new google.visualization.Query("${createLink(action:'monthly_commits')}");
			query.send(drawBarChart);
		}
		function drawBarChart(response) {			
			var data = response.getDataTable();
			var chart = new google.visualization.ColumnChart(
				document.getElementById('chart_div'));
			var chartOptions = {
					width:1000, height:300, legend:'none', title:'Monthly Commit Volume',
					backgroundColor:'black',
					titleTextStyle:{color:'yellow'},
					hAxis:{textStyle:{color:'white'}},
					vAxis:{textStyle:{color:'white'}, baselineColor:'white'},
			};
			chart.draw(data, chartOptions);
		};
	</script>
</head>
<body>
	<h1>Paisley FCE Analytics</h1>
	<div id="chart_div"></div>
</body>
</html>