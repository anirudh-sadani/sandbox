
function Item(x, y, key){
	this.x = x;
	this.y = +y;
	this.key = key;
}

function drawMostVisited(nested_data){
	nv.addGraph(function() {
		var chart = nv.models.multiBarChart();

		chart.yAxis
		.tickFormat(d3.format(',.1f'));

		d3.select('#mostviewed svg')
		.datum(nested_data)
		.transition().duration(500)
		.call(chart);

		nv.utils.windowResize(chart.update);

		return chart;
	});
}

function fetchMostVisited() {
	d3.json(
			"/api/rest/most_visited_pages_distribution?from_hour=2015-09-25&to_hour=2015-11-01", 
			function(error, json){
				if (error) return console.warn(error);

				var result = [];
				for(var i=0; i<json.length; i++){
					for (var propName in json[i]) {
						if(propName != 'rowkey'){
							result.push(new Item(json[i].rowkey, json[i][propName], propName));
						}
					}
				}
				var nested_data = d3.nest()
				.key(function(d) { return d.key; })
				.entries(result);
				console.log(result);
				console.log(nested_data);
				drawMostVisited(nested_data);
			}
	);
}
fetchMostVisited();