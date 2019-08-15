 function viewBPMImage(procinstId){
	var index = layer.open({
		type: 2,
        title: '流程进度图',
        maxmin: true,
        //shadeClose: true, //点击遮罩关闭层
        //area : [$(window).width()+"px" , $(window).height()+"px" ],
         area : ["1160px", "600px" ],
         content: webroot + "activiti/viewCurrentImage?procinstid="+procinstId
      });
	
	//layer.full(index);
}
 
 
 function turnTask(taskid,id,module){
	var index = layer.open({
 		 type: 2,
         title: '任务办理',
         maxmin: true,
         //shadeClose: true, //点击遮罩关闭层
          area : [$(window).width()+"px" , $(window).height() +"px" ],
          //area : ["370px", "420px" ],
         content: webroot +module+"/turnTask?taskid="+taskid+"&id="+id
    });
 	layer.full(index);
}
 
function turnTaskForm(taskid,id){
	var index = layer.open({
	  type: 2,
      title: '任务办理',
      maxmin: true,
      //shadeClose: true, //点击遮罩关闭层
        area : [$(window).width()+"px" , $(window).height()+"px" ],
       //area : ["370px", "420px" ],
      content: webroot +"activiti/turnTaskForm?taskid="+taskid+"&id="+id
    });
	layer.full(index);
}
 
 