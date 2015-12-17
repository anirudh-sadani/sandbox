d3.json(
"/ca/data/hits_by_day?startDate=10-11-2015&endDate=30-11-2015", 
function(error, json){
	if (error) return console.warn(error);

	var result = [];
	for(var i=0; i<json.length; i++)
	{
		var item = [];
		item.push(json[i].rowkey);
		item.push(json[i].count);
		result.push(item);
	}

	var updated_result = d3.nest()
	  .key(function(d) { return d[0];})
	  .rollup(function(d) { 
	   return d3.sum(d, function(g) {return g[1]; });
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


function drawHitsByDay(result1)
{
	  nv.addGraph(function() {
        var chart = nv.models.lineChart()
            .useInteractiveGuideline(true)
            .x(function(d) { return d.x })
            .y(function(d) { alert(d.y); return d.y })
            .color(d3.scale.category10().range())
            .duration(300)
            .clipVoronoi(false);
        chart.dispatch.on('renderEnd', function() {
            console.log('render complete: cumulative line with guide line');
        });
        chart.xAxis.tickFormat(function(d) {
            return d3.time.format('%m/%d/%y')(new Date(d))
        });
        chart.yAxis.tickFormat(d3.format(',.1f'));
        d3.select('#hitsbyday svg')
            .datum(result1)
            .call(chart);
        //TODO: Figure out a good way to do this automatically
        nv.utils.windowResize(chart.update);
       
        return chart;
    });
}

