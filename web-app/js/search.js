var filterCount = 0;

$(function() {
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
		//$('#resultsPane').load('applyFilters',$('#filtersForm').serializeArray());
		$('#filtersPane').slideUp('2000');
		$('<div style="margin-bottom : 10px;"><button type="button" id="showCriteria">Show Search Criteria</button></div>').prependTo('#resultsPane');
		$('#showCriteria').button();
		$('#showCriteria').click(function() {
			$('#filtersPane').slideDown('2000');
			$(this).remove();
		});
		return false;
	});
});