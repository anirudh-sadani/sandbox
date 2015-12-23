	function generateUserActivity()
	{
		alert(document.getElementById('emailField').value);


		d3.json(
	"/ca/data/item_visits_by_user?emailId="+document.getElementById('emailField').value, 
	function(error, json){
		if (error) return console.warn(error);

		var cart = [];
		var wishlist = [];
		var purchased = [];
		var viewed = [];
		for(var i=0; i<json.length; i++)
		{
			for (var propName in json[i]) {
				if(propName == 'PURCHASED'){
					purchased.push(new itemVisitRow(json[i].datekey, json[i][propName]));
				}
				if(propName == 'WISHLIST'){
					wishlist.push(new itemVisitRow(json[i].datekey, json[i][propName]));
				}
				if(propName == 'ITEMS_VIEWED_COUNT'){
					viewed.push(new itemVisitRow(json[i].datekey, json[i][propName]));
				}
				if(propName == 'CART_ITEMS'){
					cart.push(new itemVisitRow(json[i].datekey, json[i][propName]));
				}
			}
		}

	var updated_purchased = d3.nest()
	  .key(function(d) { return d.x;})
	  .rollup(function(d) { 
	   return d3.sum(d, function(g) {return g.y; });
	  }).entries(purchased);

	var updated_viewed = d3.nest()
	  .key(function(d) { return d.x;})
	  .rollup(function(d) { 
	   return d3.sum(d, function(g) {return g.y; });
	  }).entries(viewed);

	var updated_cart = d3.nest()
	  .key(function(d) { return d.x;})
	  .rollup(function(d) { 
	   return d3.sum(d, function(g) {return g.y; });
	  }).entries(cart);

	var updated_wishlist = d3.nest()
	  .key(function(d) { return d.x;})
	  .rollup(function(d) { 
	   return d3.sum(d, function(g) {return g.y; });
	  }).entries(wishlist);

	updated_cart.forEach(function(d) {
	 d.x = getDateObjectForAttribute(d.key);
	 d.y = d.values;
	});

	updated_viewed.forEach(function(d) {
	 d.x = getDateObjectForAttribute(d.key);
	 d.y = d.values;
	});

	updated_purchased.forEach(function(d) {
	 d.x = getDateObjectForAttribute(d.key);
	 d.y = d.values;
	});

	updated_wishlist.forEach(function(d) {
	 d.x = getDateObjectForAttribute(d.key);
	 d.y = d.values;
	});


	drawUserItemVisits(
	[	
		{
		values: updated_purchased,      //values - represents the array of {x,y} data points
		key: 'Purchased', //key  - the name of the series.
		color: '#ff7f0e'  //color - optional: choose your own line color.
		},
		{
		values: updated_cart,
		key: 'Cart Items',
		color: '#115488'//area - set to true if you want this line to turn into a filled area chart.
		},
		{
		values: updated_viewed,
		key: 'Viewed',
		color: '#1177ff'//area - set to true if you want this line to turn into a filled area chart.
		},
		{
		values: updated_wishlist,
		key: 'Wishlist',
		color: '#7700ff'  //area - set to true if you want this line to turn into a filled area chart.
		}
	]);
	}
);


d3.json(
		"/ca/data/session_details_by_user?emailId="+document.getElementById('emailField').value, 
		function(error, json){
			if (error) return console.warn(error);
			
			var back = [];
			var refresh = [];
			var sessionDuration = [];
			for(var i=0; i<json.length; i++){
				for (var propName in json[i]) {
					if(propName == 'duration'){
						var sessionItem = [];
						sessionItem.push(json[i].datekey);
						sessionItem.push(json[i][propName]/(60));
						sessionDuration.push(sessionItem );
					}
					if(propName == 'backCnt'){
						var backItem = [];
						backItem.push(json[i].datekey);
						backItem.push(json[i][propName]);
						back.push(backItem);
					}
					if(propName == 'refreshCnt'){
						var refreshItem = [];
						refreshItem.push(json[i].datekey);
						refreshItem.push(json[i][propName]);
						refresh.push(refreshItem);
					}					
				}
			}

			var updated_back = d3.nest()
				.key(function(d) { return d[0];})
				.rollup(function(d) { 
				return d3.mean(d, function(g) { return g[1]; });
			}).entries(back);

			var updated_sessionDuration = d3.nest()
				.key(function(d) { return d[0];})
				.rollup(function(d) { 
				return d3.mean(d, function(g) {return g[1]; });
			}).entries(sessionDuration);

			var updated_refresh = d3.nest()
				.key(function(d) { return d[0];})
				.rollup(function(d) { 
				return d3.mean(d, function(g) {return g[1]; });
			}).entries(refresh);


			drawUserSessionAnalysis(
				[{
					values: updated_sessionDuration,      //values - represents the array of {x,y} data points
					key: 'Duration',
					"bar": true, //key  - the name of the series.
					color: '#ff7f0e'  //color - optional: choose your own line color.
				},
				{
					values: updated_back,      //values - represents the array of {x,y} data points
					key: 'Back', //key  - the name of the series.
					color: '#117f2e'  //color - optional: choose your own line color.
				},
				{
					values: updated_refresh,      //values - represents the array of {x,y} data points
					key: 'Refresh', //key  - the name of the series.
					color: '#717f11'  //color - optional: choose your own line color.
				}].map(function(series){
	            	series.values = series.values.map
					(
						function(d){ 
							return {
								x:  getDateObjectForAttribute(d.key), y: d.values 
							} 
						}
					);
	            	return series;
	        	})
			)
		}
	);




	}



function drawUserItemVisits(result1){
	console.log(result1)
	nv.addGraph(function() {
		var chart = nv.models.lineChart()
                .margin({left: 100})  //Adjust chart margins to give the x-axis some breathing room.
                .useInteractiveGuideline(true)  //We want nice looking tooltips and a guideline!
                .showLegend(true)       //Show the legend, allowing users to turn on/off line series.
                .showYAxis(true)        //Show the y-axis
                .showXAxis(true);
		chart.yAxis     //Chart y-axis settings
		    .axisLabel('Counts')
		    .tickFormat(d3.format('.02f'));
 		chart.xAxis
       		.axisLabel('Date')
       		.rotateLabels(-45)
       		.tickFormat(function(d) { return d3.time.format('%b %d')(new Date(d)); });
 
		chart.xScale(d3.time.scale());
		d3.select('#usersessionanalysissvg1') 
			.style("height", chartHeight/2)                                               
			.style("width", chartWidth) //Select the <svg> element you want to render the chart in.   
		    .datum(result1)         //Populate the <svg> element with chart data...
		    .call(chart);          //Finally, render the chart!

		nv.utils.windowResize(function() { chart.update() });
		return chart;
	});
}


function drawUserSessionAnalysis(chartData){
	nv.addGraph(function() {
		var chart;
		chart = nv.models.linePlusBarChart()
			.legendRightAxisHint(' [Using Right Axis]')
			.color(d3.scale.category10().range());
		chart.xAxis.tickFormat(function(d) {
			return d3.time.format('%x')(new Date(d))
		}).showMaxMin(false);
		chart.y1Axis.tickFormat(function(d) { return d3.format(',f')(d) });
		chart.bars.forceY([0]).padData(false);
		chart.x2Axis.tickFormat(function(d) {
			return d3.time.format('%x')(new Date(d))
		}).showMaxMin(false);
	
		d3.select('#usersessionanalysissvg2')
	        .style("height", chartHeight/2)                                               
			.style("width", chartWidth)
			.datum(chartData)
			.transition().duration(500).call(chart);
		nv.utils.windowResize(chart.update);
		chart.dispatch.on('stateChange', function(e) { nv.log('New State:', JSON.stringify(e)); });
		return chart;
	});
}
