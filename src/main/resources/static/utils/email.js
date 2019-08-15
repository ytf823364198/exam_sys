
//这个方法有误，(yeleihong@gx-chem.cn)这样带"-"的邮箱验证错误

function checkEMail( emailAddress  ){
    var strReg=/^\w+((-\w+)|(\.\w+))*\@{1}\w+\.{1}\w{2,4}(\.\-{0,1}\w{2}){0,1}/ig;
	var r=emailAddress.search(strReg); 
	if(r==-1) { 
		return false;
	} 
	return true;
}
/**
function checkEMail( emailAddress  ){
   //  var strReg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;

	if(emailAddress != ""){
    	var strReg =/^[A-Za-zd]+([-_.][A-Za-zd]+)*@([A-Za-zd]+[-.])+[A-Za-zd]{2,5}$/; 
        if(!strReg.test(emailAddress)){
        	return false;
        }
    }
    return true;
}
*/
		    