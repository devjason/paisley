<html>
<head>
	<title>Paisley Analytics</title>
	<link type="text/css" rel="stylesheet" href="${resource(dir:'css',file:'search.css')}" />	
	<script type="text/javascript" src="http://www.google.com/jsapi"></script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.5/jquery-ui.min.js"></script>	
	<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.5/themes/sunny/jquery-ui.css" />	
	<script type="text/javascript" src="${resource(dir:'js/jquery',file:'jquery.validate.min.js')}"></script>	
	<script type="text/javascript" src="${resource(dir:'js',file:'search.js')}"></script>
</head>
<body>
	<div id="pageContent" class="ui-corner-all">
      	<h1 style="margin-bottom : 10px;">Justice Systems SVN Search Arena</h1>
      	      
      	<form id="searchForm" action="/Paisley/search/search" method="post">
        	<fieldset id="filtersPane" class="ui-corner-all">
          		<legend class="ui-widget-header ui-corner-all">Filters</legend>
          			<div id="filterPane" ></div>
          			<div class="buttonBar">
            			<button type="button" id="addFilterButton">Add Filter</button>
            			<button type="button" id="applyFilterButton">Apply Filters</button>
          			</div>
        	</fieldset>
        	<div id="ajaxInProgress"></div>	
        	<div id="resultsPane"><span class="none">No results displayed</span>
        		<div id="resultsTableWrapper">
					<table id="resultsTable" border="0">
					  	<thead class="ui-widget-header ui-corner-all">
					    	<tr>
					      		<th width="10%">Rev #</th>
					      		<th width="20%">Revision Time</th>
					      		<th width="10%">Developer</th>
					      		<th width="60%">Commit Message</th>
					    	</tr>
					  	</thead>				  
					  	<tbody>
					    	<tr class="hideOnSearch">
							  <td class="ui-state-default">&nbsp;</td>
							  <td class="ui-state-default">&nbsp;</td>
							  <td class="ui-state-default">&nbsp;</td>
							  <td class="ui-state-default">&nbsp;</td>				      
							</tr>		    
					  	</tbody>
					</table>
				</div>        		
        	</div>
      	</form>
	</div>
	
	<!-- hidden results table -->
	
	<!-- hidden templates -->
    <div id="templates">

      <div class="template filterChooser">
        <button type="button" class="filterRemover" title="Remove this filter">X</button>

        <select name="filter" class="filterChooser" title="Select a property to filter">
          <option value="" data-filter-type="" selected="selected">-- choose a filter --</option>
          <option value="revision.author" data-filter-type="stringMatch">Author</option>
          <option value="branch.name" data-filter-type="stringMatch">Branch</option>
          <option value="revision.message" data-filter-type="stringMatch">Commit Message</option>
          <option value="revision.revision_number" data-filter-type="numberRange">Revision Number</option>
          <option value="revision.revision_time" data-filter-type="dateRange">Revision Date</option>
          <option value="revision_entry.artifact" data-filter-type="stringMatch">File Committed</option>
          <option value="revision_entry.project" data-filter-type="stringMatch">Project</option>
        </select>
      </div> 

      <div class="template stringMatch">
        	<select name="stringMatchType">
          		<option value="*">contains</option>
          		<option value="^">starts with</option>
          		<option value="$">ends with</option>
          		<option value="=">is exactly</option>
        	</select>
        	<input type="text" name="term"/>
      </div>

      <div class="template numberRange">
        	<input type="text" name="numberRange1" class="numeric"/> <span>through</span>
        	<input type="text" name="numberRange2" class="numeric"/>
      </div>

      <div class="template dateRange">
        	<input type="text" name="dateRange1" class="dateValue"/> <span>through</span>
        	<input type="text" name="dateRange2" class="dateValue"/>
      </div>

      <div class="template boolean">
        	<input type="radio" name="booleanFilter" value="true" checked="checked"/> <span>Yes</span>
        	<input type="radio" name="booleanFilter" value="false"/> <span>No</span>
      </div>
    </div>
</body>
</html>