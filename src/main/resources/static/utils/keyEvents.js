function forbidBackSpace(e) {
	var ev = e || window.event; 
	var obj = ev.target || ev.srcElement; 

	if(ev.keyCode == 8){
		//obj.value = "";
		ev.keyCode=0;
		ev.returnValue=false;  
		return false;
	}
	return false;
}