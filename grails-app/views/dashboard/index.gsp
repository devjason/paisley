<html>
<head>
	<title>Paisley Analytics</title>
	<script type="text/javascript" src="http://www.google.com/jsapi"></script>
	<script type="text/javascript">
		google.load('visualization', '1', {'packages':['corechart']});
		google.setOnLoadCallback(initialize);

		function initialize() {
			var query = new google.visualization.Query("${createLink(action:'visitors')}");
			query.send(drawChart);
		}
		function drawChart(response) {			
			var data = response.getDataTable();
			var chart = new google.visualization.AreaChart(
				document.getElementById('chart_div'));
			chart.draw(data,
				{width:860, height:240, legend:'none', title:'Commit Volume'});
		};
	</script>
</head>
<body>
	<div id="chart_div"></div>
</body>
</html>