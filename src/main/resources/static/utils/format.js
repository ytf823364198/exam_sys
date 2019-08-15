//标准格式化 日期 时间
//var time1 = new Date().formatDateStd("yyyy-MM-dd");
//var time2 = new Date().formatDateStd("yyyy-MM-dd HH:mm:ss");  
Date.prototype.formatDateStd = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

//将long 转为时间
//formatDateLong(long,'date'),返回yyyy-MM-dd
//formatDateLong(long,'time'),返回yyyy-MM-dd HH:mm:ss

function formatDateLong(longTypeDate,fmt){  
    var dateTypeDate = "";  
    var date = new Date();  
    date.setTime(longTypeDate);  
    dateTypeDate += date.getFullYear();   //年  
    var month = date.getMonth() + 1; //getMonth()得到的月份是0-11  
    if(month < 10){  
        month = "0" + month;  
    }  
    dateTypeDate += "-" + month ; //月
    
    var day = date.getDate();  
    if(day < 10){  
        day = "0" + day;  
    }  
    dateTypeDate += "-" + day;   //日  
    
    if("time" == fmt ){
    	var hours = date.getHours();  
        if(hours < 10){  
        	hours = "0" + hours;  
        }  
    	dateTypeDate += " "+hours;   //
    	
    	var Minute = date.getMinutes();  
        if(Minute < 10){  
        	Minute = "0" + Minute;  
        }  
    	dateTypeDate += "-" + Minute; // 
    	
    	var Second = date.getSeconds();  
        if(Second < 10){  
        	Second = "0" + Second;  
        }  
    	dateTypeDate += "-" + Second;   // 
    }
    
    return dateTypeDate;  
}  






function formatNumRound(number, decimals) {
    /*
    * 参数说明：
    * number：要格式化的数字
    * decimals：保留几位小数
    * dec_point：小数点符号
    * thousands_sep：千分位符号
    * */
	
	var dec_point = ".";
	var thousands_sep =",";
	
	
    number = (number + '').replace(/[^0-9+-Ee.]/g, '');
    var n = !isFinite(+number) ? 0 : +number,
        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
        s = '',
        toFixedFix = function (n, prec) {
            var k = Math.pow(10, prec);
            return '' + Math.ceil(n * k) / k;
        };
 
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    var re = /(-?\d+)(\d{3})/;
    while (re.test(s[0])) {
        s[0] = s[0].replace(re, "$1" + sep + "$2");
    }
 
    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}


//舍去小数后面的位数
function formatNumGive(number, decimals) {
    /*
    * 参数说明：
    * number：要格式化的数字
    * decimals：保留几位小数
    * dec_point：小数点符号
    * thousands_sep：千分位符号
    * */
	var dec_point = ".";
	var thousands_sep =",";
    number = (number + '').replace(/[^0-9+-Ee.]/g, '');
    var n = !isFinite(+number) ? 0 : +number,

        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
        s = '',
        toFixedFix = function (n, prec) {
            var k = Math.pow(10, prec);
            return '' + Math.floor(n * k) / k;
        };
    s = (prec ? toFixedFix(n, prec) : '' + Math.floor(n)).split('.');
    var re = /(-?\d+)(\d{3})/;
    console.log(s)
    while (re.test(s[0])) {
        s[0] = s[0].replace(re, "$1" + sep + "$2");
    }

    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}

