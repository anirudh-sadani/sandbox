function inititateConversion(startDate, endDate){
	d3.json(
		"/ca/data/item_visits?startDate="+startDate+"&endDate=" + endDate, 
		function(error, json){
			if (error) return console.warn(error);
			var cart = [];
			var wishlist = [];
			var purchased = [];
			var viewed = [];
			for(var i=0; i<json.length; i++){
				for (var propName in json[i]) {
					if(propName == 'PURCHASED'){
						purchased.push(new conversionRow(json[i].datekey, (json[i]['PURCHASED'] + 0.0)/(json[i]['ITEMS_VIEWED_COUNT'] + 1.0)));
					}
				}
			}
			var updated_purchased = d3.nest()
				.key(function(d) { return d.x;})
			  	.rollup(function(d) { 
			   	return d3.mean(d, function(g) {return g.y; });
			  	}).entries(purchased);

			updated_purchased.forEach(function(d) {
				d.x = getDateObjectForAttribute(d.key);
			 	d.y = d.values;
			});
			drawConversion(
				[{
					values: updated_purchased,      //values - represents the array of {x,y} data points
					key: 'Conversion Ratio', //key  - the name of the series.
					color: '#ff7f0e'  //color - optional: choose your own line color.
				}]
			);
		}
	);
}

function conversionRow(rowkey, count)
{
	this.x = rowkey;
	this.y = +count;
}

function drawConversion(result1){
	nv.addGraph(function() {
		var chart = nv.models.lineChart()
	        .margin({left: 100})  //Adjust chart margins to give the x-axis some breathing room.
	        .useInteractiveGuideline(true)  //We want nice looking tooltips and a guideline!
	        .showLegend(true)       //Show the legend, allowing users to turn on/off line series.
	        .showYAxis(true)        //Show the y-axis
	        .showXAxis(true);
		chart.yAxis     //Chart y-axis settings
			.axisLabel('Counts')
		    .tickFormat(d3.format('.08f'));
 		chart.xAxis
       		.axisLabel('Date')
       		.rotateLabels(-45)
       		.tickFormat(function(d) { return d3.time.format('%b %d')(new Date(d)); });
 		chart.xScale(d3.time.scale());
		d3.select('#conversionanalysis svg') 
			.style("height", chartHeight)                                               
			.style("width", chartWidth)  //Select the <svg> element you want to render the chart in.   
		    .datum(result1)         //Populate the <svg> element with chart data...
		    .call(chart);          //Finally, render the chart!
		nv.utils.windowResize(function() { chart.update() });
		return chart;
	});
}
