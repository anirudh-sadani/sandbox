

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
