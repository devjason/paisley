var filterCount = 0;
function addTableRow(jQtable){
    jQtable.each(function(){
        var $table = $(this);
        // Number of td's in the last table row
        var n = $('tr:last td', this).length;
        var tds = '<tr>';
        for(var i = 0; i < n; i++){
            tds += '<td class="ui-state-default">&nbsp;</td>';
        }
        tds += '</tr>';
        if($('tbody', this).length > 0){
            $('tbody', this).append(tds);
        }else {
            $(this).append(tds);
        }
    });
}

$(function() {
	//$('#ajaxInProgress').ajaxStart(function() {
	//	$(this).addClass('progress');
	//}).ajaxStop(function() {
	//	$(this).removeClass('progress');
	//});
	
	$('#resultsTableWrapper').hide();
	
	$('#addFilterButton').click(function(){
		var filterItem = $('<div>')
			.addClass('filterItem')
			.appendTo('#filterPane')
			.data('suffix','.' + (filterCount++));
		
		$('div.template.filterChooser')
			.children()
			.clone()
			.appendTo(filterItem)
			.trigger('adjustName');
	});
	
	$('select.filterChooser').live('change',function(){
		var filterType = $(':selected',this).attr('data-filter-type');
		var filterItem = $(this).closest('.filterItem');
		
		$('.qualifier',filterItem).remove();
		$('div.template.'+filterType)
			.children()
			.clone()
			.addClass('qualifier')
			.appendTo(filterItem)
			.trigger('adjustName');
		
		$('option[value=""]',this).remove();
		$('.dateValue').datepicker();
	});
	
	$('button.filterRemover').live('click',function(){
		$(this).closest('div.filterItem').remove();
	});
	
	$('.filterItem [name]').live('adjustName',function(){
		var suffix = $(this).closest('.filterItem').data('suffix');
		if (/(\w)+\.(\d)+$/.test($(this).attr('name'))) return;
		$(this).attr('name',$(this).attr('name')+suffix);
	});
	
	$('#addFilterButton').button({
		icons : {
			primary : 'ui-icon-plusthick'
		}
	});
	
	$('#applyFilterButton').button({
		icons : {
			primary : 'ui-icon-circle-check'
		}
	});
	
	$('#addFilterButton').click();
	
	$('#applyFilterButton').click(function() {
		$('#ajaxInProgress').addClass('progress');
		var $table = $('#resultsTable');
		$('.hideOnSearch', $table).hide();
		$('.hideOnSearch', $table).siblings().remove();
		
		$('#filtersPane').slideUp('2000');
		$('<div style="margin-bottom : 10px;"><button type="button" id="showCriteria">Show Search Criteria</button></div>').prependTo('#resultsPane');
		$('#showCriteria').button();
		$('#showCriteria').click(function() {
			$('#filtersPane').slideDown('2000');
			$(this).remove();
		});
		
		$.get('/Paisley/search/search', $('#searchForm').serializeArray(), function(response) {			
			$('#resultsTableWrapper').show();
			if ($(response).find('revision').size() > 0) {
				$('#resultsPane span.none').remove();
				$('#resultsTableWrapper').show();
				var $resultsTableTBody = $('#resultsTable tbody');				
				$(response).find('revision').each(function() {		
					var $revision = $(this);
					var $table = $('#resultsTable');
					$('.hideOnSearch', $table).hide();
					addTableRow($table);
					var $rowElements = $('tr:last td', $table);
					
					var index = 0;
					$rowElements.each(function() {
						switch(index) {
							case 0 : $(this).html($revision.find('revisionNumber').text()); break;
							case 1 : $(this).html($revision.find('revisionTime').text().substring(0,16)); break;
							case 2 : $(this).html($revision.find('author').text()); break;
							case 3 : $(this).html($revision.find('message').text()); break;
						}
 						index++;
					});
										
				});
			} else {
				$('#resultsTableWrapper').hide();
				$('<span class="none">No results displayed</span>').appendTo('#resultsPane').hide().fadeIn('slow');
			}
			$('#ajaxInProgress').removeClass('progress');
		}, 'xml');
		
		return true;
	});
});