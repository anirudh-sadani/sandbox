function initiateCartAnalysis(startDate, endDate)
{
	var chartData=[];
	d3.json(
		"/ca/data/spending_trends?startDate="+startDate+"&endDate=" + endDate, 
		function(error, json){
			if (error) return console.warn(error);

		var dataToDraw = getTransformedData(json);
		//alert(dataToDraw.toSource());
		chartData.push(dataToDraw)
	console.log(chartData);
 	drawSunBurst(chartData);
	
		});
}

function drawSunBurst(chartData)
{
	var chart;
    nv.addGraph(function() {
        chart = nv.models.sunburstChart();
        chart.color(d3.scale.category20c());
        d3.select("#trendanalysis svg")
		.style("height", chartHeight)                                               
		.style("width", chartWidth)
                .datum(chartData)

                .call(chart);


        nv.utils.windowResize(chart.update);
        return chart;
    });
}

function getTransformedData(rawData){

var newData = { name :"name", children : [] },
    levels = ["categoryName","productName"];

// For each data row, loop through the expected levels traversing the output tree
rawData.forEach(function(d){
    // Keep this as a reference to the current level
    var depthCursor = newData.children;
    // Go down one level at a time
    levels.forEach(function( property, depth ){

        // Look to see if a branch has already been created
        var index;
        depthCursor.forEach(function(child,i){
            if ( d[property] == child.name ) index = i;
        });
        // Add a branch if it isn't there
        if ( isNaN(index) ) {
            depthCursor.push({ name : d[property], children : []});
            index = depthCursor.length - 1;
        }
        // Now reference the new child array as we go deeper into the tree
        depthCursor = depthCursor[index].children;
        // This is a leaf, so add the last element to the specified branch
        if ( depth === levels.length - 1 ) depthCursor.push({ "name" : d.productId, "size" : parseInt(d.price) });
    });
});

return newData;

}
