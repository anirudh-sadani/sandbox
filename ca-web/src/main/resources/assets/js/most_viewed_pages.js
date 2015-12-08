d3.json(
"/ca/data/hits_by_day", 
function(error, json){
	if (error) return console.warn(error);

	var result = [];
	for(var i=0; i<json.length; i++){
		result.push(new row(json[i].rowkey, json[i].count, "Hits Count"));
	}
var expensesByName = d3.nest()
.key(function(d) { return d.key; })
.entries(result);

	drawMostVisited(expensesByName);
}
);

function row(rowkey, count, key){
	this.x = rowkey;
	this.y = count;
	this.key = key;
}


function drawMostVisited(result1){
	nv.addGraph(function() {
		var chart = nv.models.discreteBarChart();
		chart.yAxis
		.tickFormat(d3.format(',.1f'));

		d3.select('#mostvisited svg')
		.datum(result1)
		.transition().duration(500)
		.call(chart);

		nv.utils.windowResize(chart.update);

		return chart;
	});
}




