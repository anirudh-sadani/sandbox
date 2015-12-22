var chartHeight = 510;
var chartWidth = 1275;

function navigateCarousel(slideNum){
	$("#myCarousel").carousel(slideNum);
}


$('#opener').on('click', function() {		
	var panel = $('#slide-panel');
	if (panel.hasClass("visible")) {
		panel.removeClass('visible').animate({'margin-left':'-300px'});
	} else {
		panel.addClass('visible').animate({'margin-left':'0px'});
	}	
	return false;	
});


function getDateObjectForAttribute(rowkey)
{
	return new Date(rowkey);
}