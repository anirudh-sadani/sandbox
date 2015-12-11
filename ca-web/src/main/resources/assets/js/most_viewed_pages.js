d3.json(
"/ca/data/most_visited_pages_by_users", 
function(error, json){
	if (error) return console.warn(error);

	var result = [];
	for(var i=0; i<json.length; i++){
					for (var propName in json[i]) {
						if(propName != 'rowkey'){
							result.push(new row(json[i].rowkey, json[i][propName], propName));
						}
					}
				}
console.log(json);

var expensesByName = d3.nest()
.key(function(d) { return d.key; })
.entries(result);
console.log(expensesByName);
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
		var chart = nv.models.multiBarChart();
		chart.yAxis
		.tickFormat(d3.format(',.1f'));

//chart.yRange([0,0,0,10,2000]);

		d3.select('#mostvisited svg')
		.datum(result1)
		.transition().duration(500)
		.call(chart);

		nv.utils.windowResize(chart.update);

		return chart;
	});
}




