$(function() {
    function cb(start, end) {
      $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));

    	initiateSessionAnalysis(start.format('DD-MM-YYYY'), end.format('DD-MM-YYYY'));
    	initiateHitsByDay(start.format('DD-MM-YYYY'), end.format('DD-MM-YYYY'));
    	initiateMostViewed(start.format('DD-MM-YYYY'), end.format('DD-MM-YYYY'));
    	//initiateCartAnalysis(start.format('DD-MM-YYYY'), end.format('DD-MM-YYYY'));
    	initiateCartAnalysis('15-12-2015', end.format('DD-MM-YYYY'));
    	inititateConversion(start.format('DD-MM-YYYY'), end.format('DD-MM-YYYY'));
    	inititateItemVisits(start.format('DD-MM-YYYY'), end.format('DD-MM-YYYY'));
    	//initiateGeoAnalysis(start.format('DD-MM-YYYY'), end.format('DD-MM-YYYY'));
  }
  cb(moment().subtract(45, 'days'), moment());

  $('#reportrange').daterangepicker({
      ranges: {
         'Today': [moment(), moment()],
         'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
         'Last 7 Days': [moment().subtract(6, 'days'), moment()],
         'Last 30 Days': [moment().subtract(29, 'days'), moment()],
         'This Month': [moment().startOf('month'), moment().endOf('month')],
         'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
      }
  }, cb);
});

function changeTitle(textTitle)
{
	$('#reportTitle span').html('<font color="black" size=5 >&nbsp;&nbsp;&nbsp;' + textTitle + '</font>');
}
