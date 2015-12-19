var chartHeight = 510;
var chartWidth = 1300;

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
	if (rowkey.length == 10)
		return new Date(rowkey.substring(3,5) + "-" + rowkey.substring(0,2) + "-" + rowkey.substring(6));
	else
		return new Date(rowkey.substring(2,4) + "-" + rowkey.substring(0,1) + "-" + rowkey.substring(5));
}

 	
