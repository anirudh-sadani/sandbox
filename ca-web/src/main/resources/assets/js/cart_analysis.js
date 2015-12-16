 var chart;
    nv.addGraph(function() {
        chart = nv.models.sunburstChart();
        chart.color(d3.scale.category20c());
        d3.select("#trendanalysis svg")
.style("width", 800)                                               
.style("height", 300)
                .datum(getData())

                .call(chart);


        nv.utils.windowResize(chart.update);
        return chart;
    });
    function getData() {
        return [
	{"name":"Total Items",
	"children":[
	{
            "name": "purchased",
            "children": [
                {
                    "name": "electronics",
                    "children": [
                        {
                            "name": "tv",
                            "children": [
                                {"name": "Samsung", "size": 3938},
                                {"name": "Sony", "size": 3812},
                                {"name": "Philips", "size": 6714},
                                {"name": "LG", "size": 743}
                            ]
                        },
                        {
                            "name": "mobile",
                            "children": [
                                {"name": "Mi", "size": 3534},
                                {"name": "Samsung", "size": 5731},
                                {"name": "HTC", "size": 7840},
                                {"name": "Apple", "size": 5914},
                                {"name": "Windows", "size": 3416}
                            ]
                        },
                        {
                            "name": "Laptop",
                            "children": [
                                {"name": "Dell", "size": 7074}
                            ]
                        }
                    ]
                }
            ]
        },

{
            "name": "wishlist",
            "children": [
                {
                    "name": "electronics",
                    "children": [
                        {
                            "name": "tv",
                            "children": [
                                {"name": "Samsung", "size": 3938},
                                {"name": "Sony", "size": 3812},
                                {"name": "Philips", "size": 6714},
                                {"name": "LG", "size": 743}
                            ]
                        },
                        {
                            "name": "mobile",
                            "children": [
                                {"name": "Mi", "size": 3534},
                                {"name": "Samsung", "size": 5731},
                                {"name": "HTC", "size": 7840},
                                {"name": "Apple", "size": 5914},
                                {"name": "Windows", "size": 3416}
                            ]
                        },
                        {
                            "name": "Laptop",
                            "children": [
                                {"name": "Dell", "size": 7074}
                            ]
                        }
                    ]
                }
            ]
        },

{
            "name": "addToCart",
            "children": [
                {
                    "name": "electronics",
                    "children": [
                        {
                            "name": "tv",
                            "children": [
                                {"name": "Samsung", "size": 3938},
                                {"name": "Sony", "size": 3812},
                                {"name": "Philips", "size": 6714},
                                {"name": "LG", "size": 743}
                            ]
                        },
                        {
                            "name": "mobile",
                            "children": [
                                {"name": "Mi", "size": 3534},
                                {"name": "Samsung", "size": 5731},
                                {"name": "HTC", "size": 7840},
                                {"name": "Apple", "size": 5914},
                                {"name": "Windows", "size": 3416}
                            ]
                        },
                        {
                            "name": "Laptop",
                            "children": [
                                {"name": "Dell", "size": 7074}
                            ]
                        }
                    ]
                }
            ]
        }
]
}
















];
    }