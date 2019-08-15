/*! ziyue WebUploader 0.1.5 */
/**
 * huyq 2018-08-03
 */
/**
页面引入代码
<div class="uploder-container">
	<div class="uploader-info" style="dispaly:none">
		<div class="uploader-info-name">AssetApply</div>
		<div class="uploader-info-limit">30</div>
	</div>
	<div class="webuploader-ext" >
		允许<span class="uploader-ext-format">jpg,pdf</span>格式文件
	</div>
	<div style="margin:3px 0px 5px 5px;">
		<div class="uploader-file-btn" id="asset-apply-other" >请上传附件</div>
		<div class="uploader-file-list"></div>
	</div>
</div>
*/


var uploaders ;
$(function(){
	uploaders = new Array();
	$('.uploder-container').each(function(index){
			//获取页面变量
			var uploaderFileBtn = $(this).find('.uploader-file-btn').attr("id");
			var uploaderFileName = $(this).find('.uploader-info-name').eq(0).html();
			var uploaderFileLimit = $(this).find('.uploader-info-limit').eq(0).html();
			var uploaderFileFormat = $(this).find('.uploader-ext-format').eq(0).html();
			var uploaderFileList = $(this).find('.uploader-file-list');
			
			//初始化上传组件
			uploaders[index] = WebUploader.create({
				swf : webroot + "webjars/github-com-fex-team-webuploader/0.1.5/dist/Uploader.swf",
				server : webroot + "attach/webuploader",
				chunked: true,
			    chunkSize: 5 * 1024 * 1024,
			    pick: {
			        id: '#' +  uploaderFileBtn,
			        name: uploaderFileName ,  
			        multiple:true
			    },
			    formData:{guid:WebUploader.Base.guid()},
			    fileVal: uploaderFileName ,
			    threads:3,
			    fileNumLimit : uploaderFileLimit , //限制文件个数
			    resize : false,
			    accept: {
			    	extensions: uploaderFileFormat
			    }
	        });
			
			//添加文件，进入文件队列
			uploaders[index].on('fileQueued', function(file) {
				uploaderFileList.append(
					'<div id="' + file.id + '" class="webuploader-item">' +
						'<span class="webuploader-info">' + file.name +'</span>' +
						'<span class="webuploader-size"> / <em>'+ WebUploader.Base.formatSize(file.size) +'</em> / </span>' +
						'<span class="webuploader-state"><em>...</em></span>' +
						'<span class="webuploader-option" id="wuFileOption'+file.id+'"><a href="javascript:delUploadFile(\''+ file.id+'\')">[删除]</a></span>' +
						'<div  class="webuploader-progress"></div>' +
						'<span style="display:none;">'+
							'<input type="hidden" name="wuFileId" value="'+file.id+'"/>'+
							'<input type="hidden" name="wuGuId" value="'+uploaders[index].options.formData.guid+'"/>'+
							'<input type="hidden" name="wuFileName" value="'+file.name+'"/>'+
							'<input type="hidden" name="wuFileExt" value="'+file.ext+'"/>'+
							'<input type="hidden" name="fileVal" value="'+uploaders[index].options.fileVal+'"/>'+
							'<input type="hidden" id="wuFileStatus'+file.id+'" name="wuFileStatus" value="inited"/>'+
						'</span>' +
				 	'</div>'
				);
			});
			
			//合并文件
			uploaders[index].on('uploadSuccess', function(file) {
				var chunkSize = uploaders[index].options.chunkSize; // 切片大小
				var chunks = Math.ceil(file.size/ chunkSize );
				if( chunks <= 1 ){
					$('#' + file.id).find('.webuploader-state').text("已上传");
					$('#wuFileStatus'+file.id).val("complete");
					$('#wuFileOption'+file.id).remove();
					return;
				}
				$('#' + file.id).find('.webuploader-state').text("正在合并文件分片....");
				$.ajax({
		             type: "POST",
		             url: webroot + "attach/merge",
		             data: {
		            	 	guid: uploaders[index].options.formData.guid,
							chunks:Math.ceil(file.size/ chunkSize ),
							ext:file.ext,
							fileId:file.id
		             },
		             success: function(data){
		            	 if(data.status == "ok"){
		            		 $('#' + file.id).find('.webuploader-state').text("已上传");
		            		 $('#wuFileStatus'+file.id).val("complete");
		            		 $('#wuFileOption'+file.id).remove();
		            	 }else{
		            		 $('#' + file.id).find('.webuploader-state').text("合并文件错误");
		            		 $('#wuFileStatus'+file.id).val("error");
		            	 }
		             }
		         });
			});
			
			//文件上传错误
			uploaders[index].on('uploadError', function(file,reason ) {
				$('#' + file.id).find('.webuploader-state').text('上传出错,请删除文件重新上传 ' + reason );
				$('#wuFileStatus'+file.id).val("error");
			});
			
			//文件上传完成
			uploaders[index].on('uploadComplete', function(file) {
				//$('#' + file.id).find('.webuploader-progress').fadeOut();
			});		
			
			//文件上传进度
			uploaders[index].on('uploadProgress', function(file,percentage) {
				$('#' + file.id).find(".webuploader-progress").css("width",parseInt( percentage * 600 ));
				$('#' + file.id).find(".webuploader-progress").html(parseInt(percentage*100)+"%");
			});
	});
});

//删除文件
function delUploadFile(fileid){
	var cwin = layer.confirm('确定取消文件吗?', {
		btn: ['删除','放弃'] //按钮
	}, function(cwin){
		layer.close(cwin);
		$("#"+fileid).remove();
		for(var i = 0; i< uploaders.length ;i++ ){
			var dfile = uploaders[i].getFile(fileid);
			if( typeof(dfile)  != "undefined" ){
				uploaders[i].removeFile( dfile ,true);
			}  
		}
	}, function(cwin){
		layer.close(cwin);
	}); 
}


function emptyAllFile(){
	for(var i = 0; i< uploaders.length ;i++ ){
		var files = uploaders[i].getFiles();
		if(files.length > 1){
			for(var j = files.length -1 ; j >= 0  ; j-- ){
				var file = files[j];
				$("#"+file.id).remove();
				uploaders[i].removeFile(file ,true);
			}
		}
	}
}

function delAttachDiv(id){
	var cwin = layer.confirm('确定要删除吗?', {
		btn: ['删除','放弃'] //按钮
	}, function(cwin){
		layer.close(cwin);
		$("#"+id).remove();
	}, function(cwin){
		layer.close(cwin);
	}); 
}



//验收是否选择了要上传的文件
function  checkFileVals(fileVal){
	var _fileVals = document.getElementsByName("fileVal");
	var isAdd = false;
	if( _fileVals.length > 0  ){
		for(var i = 0 ;i< _fileVals.length ; i++){
			if( fileVal == _fileVals[i].value   ){
				isAdd = true;
				break;
			}
		}
	}
	return isAdd;
}


var timeid; //定时任务Id
$("#webuploader-btn").on('click', function() {
	uploadAfterVerify();
});

//验证表单后上传
function uploadAfterVerify(){
	//表单校验
	if(verifyForm() == false){
		return false;
	}
	$("#webuploader-btn").attr('disabled','disabled');
	var wuFileIds  = document.getElementsByName("wuFileId");
	if(wuFileIds.length < 1  ){
		$("#webuploader-btn").removeAttr('disabled');
		submitForm();
	}else{
		timeid = window.setInterval("verifyFile()",500);
		for(var i = 0; i< uploaders.length ;i++ ){
			uploaders[i].upload();
		}
	}
}

//验证文件上传情况
function verifyFile(){	
	var wuFileStatuses  = document.getElementsByName("wuFileStatus");
	for(var i = 0 ; i < wuFileStatuses.length ; i++ ){
		var wuFileStatus = wuFileStatuses[i].value;
		if(wuFileStatus == "error"){
			window.clearInterval(timeid); 
			$("#webuploader-btn").removeAttr('disabled');
			return false;
		}
		if( wuFileStatus  != "complete" ){
			return false;
		}
	}
	//可以提交表单了
	window.clearInterval(timeid); 
	$("#webuploader-btn").removeAttr('disabled');
	submitForm();	
}
