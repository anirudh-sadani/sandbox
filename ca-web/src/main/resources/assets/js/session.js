function initiateSessionAnalysis(startDate, endDate)
{

d3.json(
"/ca/data/session_details?startDate="+startDate+"&endDate=" + endDate, 
function(error, json){
	if (error) return console.warn(error);

	var back = [];
var refresh = [];
	var sessionDuration = [];
	for(var i=0; i<json.length; i++)
	{
		for (var propName in json[i]) {
			if(propName == 'duration'){
				var sessionItem = [];
				sessionItem.push(json[i].datekey);
				sessionItem.push(json[i][propName]/(1000*60));
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


drawSessionAnalysis(
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
			}].map(function(series) 
			{
            			series.values = series.values.map
				(
							function(d) 
							{ return {
									x:  getDateObjectForAttribute(d.key), y: d.values 
								} 
							}
				);
            			return series;
        		}
			)
)});

}

function drawSessionAnalysis(chartData)
{
	console.log(chartData);
    nv.addGraph(function() {
	var chart;
        chart = nv.models.linePlusBarChart()
            .legendRightAxisHint(' [Using Right Axis]')
            .color(d3.scale.category10().range());
        chart.xAxis.tickFormat(function(d) {
                return d3.time.format('%x')(new Date(d))
            })
            .showMaxMin(false);
        chart.y1Axis.tickFormat(function(d) { return d3.format(',f')(d) });
        chart.bars.forceY([0]).padData(false);
        chart.x2Axis.tickFormat(function(d) {
            return d3.time.format('%x')(new Date(d))
        }).showMaxMin(false);
        d3.select('#sessionanalysis svg')
		                                           
.style("height", chartHeight)                                               
		.style("width", chartWidth)
            .datum(chartData)
            .transition().duration(500).call(chart);
        nv.utils.windowResize(chart.update);
        chart.dispatch.on('stateChange', function(e) { nv.log('New State:', JSON.stringify(e)); });
        return chart;
    });
}
