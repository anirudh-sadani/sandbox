function initiateGeoAnalysis()
{
  d3.json( 
"/ca/data/hits_by_location?startDate=06-10-2015&endDate=21-12-2015", 
	function(error, json)
	{
		if (error) return console.warn(error);

		initMap(json);
	});

}

function initializeDefaultMap(){

var mymapData = [{"rowkey":"2015-11-10_10925","hitCount":"22","state":"NY","longitude":"-74.29778","latitude":"41.211794","datekey":"2015-11-10","city":"Greenwood Lake"},{"rowkey":"2015-11-10_13157","hitCount":"17","state":"NY","longitude":"-75.72307","latitude":"43.209147","datekey":"2015-11-10","city":"Sylvan Beach"},{"rowkey":"2015-11-10_14839","hitCount":"26","state":"NY","longitude":"-77.63705","latitude":"42.129806","datekey":"2015-11-10","city":"Greenwood"}];

initMap(mymapData);
}

var map;
 function initMap(mapData) {
    alert("creating map");
   // Create the map.
   map = new google.maps.Map(document.getElementById('map'), {
     zoom: 3,
     center: {lat: 37.090, lng: -95.712},
     mapTypeId: google.maps.MapTypeId.TERRAIN
   });


	for(var i=0; i<mapData.length; i++)
	{
		var cityCircle = new google.maps.Circle({
       strokeColor: '#FF0000',
       strokeOpacity: 0.8,
       strokeWeight: 2,
       fillColor: '#FF0000',
       fillOpacity: 0.35,
       map: map,
       center: new centerItem(parseFloat(mapData[i].latitude), parseFloat(mapData[i].longitude)),
       radius: Math.sqrt(parseInt(mapData[i].hitCount)) * 1000
     });
	}

 
 }

function centerItem(lat, lng)
{
	this.lat=lat;
	this.lng=lng;
	
}
