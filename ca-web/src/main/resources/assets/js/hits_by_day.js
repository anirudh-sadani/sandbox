d3.json(
"/ca/data/hits_by_day?startDate=10-11-2015&endDate=30-11-2015", 
function(error, json){
	if (error) return console.warn(error);

	var result = [];
	for(var i=0; i<json.length; i++)
	{
		result.push(new hitsbydayRow(json[i].rowkey, json[i].count));
	}

	var updated_result = d3.nest()
	  .key(function(d) { return d.x;})
	  .rollup(function(d) { 
	   return d3.sum(d, function(g) {return g.y; });
	  }).entries(result);

updated_result.forEach(function(d) {
 d.x = new Date(d.key.substring(3,5) + "-" + d.key.substring(0,2) + "-" + d.key.substring(6));
 d.y = d.values;
});


drawHitsByDay(
[{
values: updated_result,      //values - represents the array of {x,y} data points
key: 'Hits', //key  - the name of the series.
color: '#ff7f0e'  //color - optional: choose your own line color.
}]);




}

);

function hitsbydayRow(rowkey, count)
{
	this.x = rowkey;
	this.y = +count;
}


function drawHitsByDay(result1){
	console.log(result1)
	nv.addGraph(function() {
		var chart = nv.models.lineChart()
                .margin({left: 100})  //Adjust chart margins to give the x-axis some breathing room.
                .useInteractiveGuideline(true)  //We want nice looking tooltips and a guideline!
               
                .showLegend(true)       //Show the legend, allowing users to turn on/off line series.
                .showYAxis(true)        //Show the y-axis
                .showXAxis(true)        //Show the x-axis
		  ;


		  chart.yAxis     //Chart y-axis settings
		      .axisLabel('Counts')
		      .tickFormat(d3.format('.02f'));
 chart.xAxis
       .axisLabel('Date')
       .rotateLabels(-45)
       .tickFormat(function(d) { return d3.time.format('%b %d')(new Date(d)); });
 
chart.xScale(d3.time.scale());


		  /* Done setting the chart up? Time to render it!*/

		  d3.select('#hitsbyday svg') 
.style("width", 800)                                               
.style("height", 400)   //Select the <svg> element you want to render the chart in.   
		      .datum(result1)         //Populate the <svg> element with chart data...
		      .call(chart);          //Finally, render the chart!

		  //Update the chart when window resizes.
		  nv.utils.windowResize(function() { chart.update() });
		  return chart;
	});
}

