var currentDrawn = "mostvisited";

function toggleViews(action) {

eval('$("#'+ currentDrawn + '").toggle();');

eval('$("#'+ action + '").toggle();');
	currentDrawn = action;
    
alert(currentDrawn);

}

function showUserSession(){

$("#second").hide();

$("#first").show();

}

function showECommerce(){
$("#first").hide();

$("#second").show();



}
showUserSession();
