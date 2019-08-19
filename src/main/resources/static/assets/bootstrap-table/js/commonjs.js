var monitorInterval, lineoption;
var Common = function () {
    var self = this;

    /**
      *  初始化四个日期控件的格式；
      *  最大日期：为当前日期；
      *  最小日期：为当前日前00：00：00
      **/
    self.initLayerdate = function () {
        var endDefaultDate = commonsdk.formatDate(new Date());
        var startDefaultDate = endDefaultDate.substring(0, endDefaultDate.indexOf(' ')) + ' 00:00:00';
        $("#interSearch,#dateSearch").find('.startDate').attr('onclick', "laydate({ istime: true, istoday: true, format: 'YYYY-MM-DD hh:mm:ss', min: '2000-01-01 00:00:00', max:laydate.now()})").val(startDefaultDate);
        $("#interSearch,#dateSearch").find('.endDate').attr('onclick', "laydate({ istime: true, istoday: true, format: 'YYYY-MM-DD hh:mm:ss', min: '2000-01-01 00:00:00', max:laydate.now() })").val();
    }

    /**
    *  时间格式转化为"yyyy-mm-dd hh:MM:ss"
    **/
    self.formatDate = function (date, type) {
        var year = date.getFullYear();//年
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;//月
        var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();//日
        var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();//时
        var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();//分
        var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();//秒
        if (type == "") {
            var milliseconds = date.getMilliseconds() < 10 ? "0" + date.getMilliseconds() : date.getMilliseconds(); //毫秒
            var milltime = milliseconds.toString().substring(0, 2);
            return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds + "." + milltime;
        } else {
            return year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
        }

    }














    self.dataList = {
        value: []
    };
    self.showLayerLoad = function () {
        layer.load(2, {
            shade: [0.1, '#fff'] //0.1透明度的白色背景
        });
    };
    self.showLayerTtips = function (title, id) {
        layer.tips(title, id, {
            tips: [1, '#3595CC'],
            time: 4000
        });
    }
    self.showLayerMsgs = function (title, type) {
        if (typeof type == 'undefined') {
            type = 2;
        }
        layer.msg(title, { time: 3000, icon: type, skin: 'layui-layer-rim' });
    }
    //询问框
    self.showLayerConfirm = function (title, fun) {
        layer.confirm(title, {
            btn: ['确定', '取消'] //按钮
        }, function () {
            fun();
            layer.closeAll();
        }, function () {
            layer.closeAll();
        });
    }
    self.setDataWidth = function(data) {
        return "<div style='width:auto;max-width:600px;height:auto;max-height:120px;overflow:auto'>"+data+"</div>";
    }
    //设置localStorage();
    self.setLocalStorage = function (name, value) {
        localStorage.setItem(name, value);
    }
    //获取localStorage()
    self.getLocalStorage = function (name) {
        return localStorage.getItem(name);
    }

    //删除localStorage()
    self.delLocalStorage = function (name) {
        localStorage.removeItem(name);
    }

    //结束循环调用
    //self.clearInterval = function (id) {
    //    window.clearInterval(id);
    //}
    self.clearInterval = function () {
        clearInterval(monitorInterval);
        monitorInterval = null;
    }

    self.clearData = function () {
        for (var i = 0; i < lineoption.series.length; i++) {
            lineoption.series[i].data = cmsdk.getEmptyArr();
        }
    }
    self.clearBarData = function() {
        var timeOption = commonsdk.getBarOption('', [0], [0]);
        realTimeChart.setOption(timeOption);
        sevenDayChart.setOption(timeOption);
    }
    self.showLineErrorTip = function(res){
        layer.closeAll();
        commonsdk.clearData();
        commonsdk.showLayerMsgs(res.message || '获取的数据为空，请稍后重试~');
        commonsdk.clearInterval(monitorInterval);
    }
    self.showBarErrorTip = function (res) {
        layer.closeAll();
        commonsdk.clearBarData();
        commonsdk.showLayerMsgs(res.message || '获取的数据为空，请稍后重试~');
        commonsdk.clearInterval(monitorInterval);
    }
  
    self.drawlineChart = function (res) {
        if (res.result == 1) {
            if (res.data == null) {
                layer.closeAll();
                commonsdk.showLayerMsgs('获取的数据为空，请稍后重试~');
                commonsdk.clearData();
                commonsdk.clearInterval();
                systemChart.setOption(lineoption, true);
                return;
            }
            layer.closeAll();
            var data = res.data;
            var cpuPercent = data.CpuPercent;
            var memoryPercent = data.MemoryPercent;
            var netPercent = data.NetPercent;

            //2、加载监控echarts
            // 使用刚指定的配置项和数据显示图表。
            var data0, data1, data2;
            data0 = lineoption.series[0].data;
            data0.shift(); //12.6
            data0.push(cpuPercent.toFixed(1));

            data1 = lineoption.series[1].data;
            data1.shift(); //12.6
            data1.push(memoryPercent.toFixed(1));

            data2 = lineoption.series[2].data;
            data2.shift(); //12.6
            data2.push(netPercent.toFixed(1));

            var axisData = cmsdk.otherFormatDate(new Date());
            lineoption.xAxis.data.shift(); //去掉第一个数
            lineoption.xAxis.data.push(axisData); //追加，"8:11:47"

            if (lineoption && typeof lineoption === "object") {
                systemChart.setOption(lineoption, true);
            }

        } else {
            layer.closeAll();
            commonsdk.showLayerMsgs(res.message || '获取的数据为空，请稍后重试~');
            commonsdk.clearInterval();
        }
    }
    self.drawbarChart = function(res) {
        console.log(res);
        if (res.result == 1) {
            if (res.data == null || res.data.length == 0) {
                layer.closeAll();
                commonsdk.clearBarData();
                commonsdk.showLayerMsgs('获取的数据为空，请稍后重试~');
                commonsdk.clearInterval(monitorInterval);
                return;
            }
            layer.closeAll();
            var data = res.data;
            var i, num, time;
            var xAxisArr = [];
            var seriesArr = [];
            var name = localStorage.getItem("nameType");
            for (i = 0; i < data.length; i++) {
                num = data[i].Num;
                xAxisArr.push(data[i].APIName);
                seriesArr.push(num);
            }
            var timeOption = commonsdk.getBarOption('连接器/连接数:', xAxisArr, seriesArr);
            if (name == 'realTimeTab') {
                realTimeChart.setOption(timeOption);
            } else {
                sevenDayChart.setOption(timeOption);
            }

        } else {
            layer.closeAll();
            commonsdk.clearBarData();
            commonsdk.showLayerMsgs(res.message || '获取的数据为空，请稍后重试~');
            commonsdk.clearInterval(monitorInterval);
        }
    }
   
    //改变后台传递过来的'/Date(456465456456+4445)/'的格式
    self.changeDateFormat = function (jsondate) {
        if (jsondate == null) {
            return "";
        }
        jsondate = jsondate.replace("/Date(", "").replace(")/", "");
        if (jsondate.indexOf("+") > 0) {
            jsondate = jsondate.substring(0, jsondate.indexOf("+"));
        }
        else if (jsondate.indexOf("-") > 0) {
            jsondate = jsondate.substring(0, jsondate.indexOf("-"));
        }
        var date = new Date(parseInt(jsondate, 10));
        var time = commonsdk.formatDate(date, '');
        return time;
    }
    self.getBeforeSevenDayDate = function (value) {
        var date = new Date(value);
        var year = date.getFullYear();//年
        var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;//月
        var day = date.getDate() - 6 < 10 ? "0" + date.getDate() - 6 : date.getDate() - 6;//日
        return year + "-" + month + "-" + day + ' 00:00:00';
    }
    //模拟点击事件
    self.onClick = function (id) {
        if (document.all) { // IE
            document.getElementById(id).click();
        }
        else {  // 其它浏览器
            var e = document.createEvent("MouseEvents");
            e.initEvent("click", true, true);
            document.getElementById(id).dispatchEvent(e);
        }
    }

    self.getEmptyArr = function (max) {
        if (!max) max = 30;
        var res = [];
        var len = 0;
        while (len < max) {
            res.push(0);
            len++;
        }
        return res;
    }

    self.getTimeArr = function () {
        var now = new Date();
        var res = [];
        var len = 30;
        while (len--) {
            res.unshift(self.formatDateSimple(now));
            //res.unshift(now.toLocaleTimeString().replace(/^\D*/, ''));
            now = new Date(now - 1000);
        }
        return res;
    }
   
    
    self.getmd5Data = function () {
        var reqData = {};
        var userId = cmsdk.getUserId();
        var token = cmsdk.getToken();
        monitor.GetPackageList(JSON.stringify(reqData), userId, token, function (res) {
            console.log(res);
            if (res.result == 1) {
                if (res.data == null) {
                    layer.closeAll();
                    commonsdk.showLayerMsgs('获取的数据为空，请稍后重试~');
                    return;
                }
                for (var i = 0; i < res.data.length; i++) {
                    cmsdk.dataList.value.push({
                        ID: res.data[i].ID,
                        Type: res.data[i].Type,
                        Version: res.data[i].Version,
                        FileName: res.data[i].FileName,
                        MD5: res.data[i].MD5
                    });
                }
            } else {
                commonsdk.showLayerMsgs(res.message);
            }
        }, function (res) {
            commonsdk.showLayerMsgs(res.message);
        });
    }
    /**
    *  时间格式转化为"yyyy-mm-dd HH:MM:ss"
    *  24小时制
    **/
    self.formatDateSimple = function (date) {
        var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();//时
        var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();//分
        var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();//秒
        return hour + ":" + minutes + ":" + seconds;
    }
    self.otherFormatDate = self.formatDateSimple;
    self.cupModule = function () {
        return [
            {
                name: 'cup使用情况',
                type: 'line',
                data: self.getEmptyArr()
            },
            {
                name: '内存使用情况',
                type: 'line',
                data: self.getEmptyArr()
            }, {
                name: '网络使用情况',
                type: 'line',
                data: self.getEmptyArr()
            }
        ];
    }
    self.getUserId = function () {
        return JSON.parse(self.getLocalStorage('uTokenUserID')).UserID;
    }
    self.getToken = function () {
        return JSON.parse(self.getLocalStorage('uTokenUserID')).UToken;
    }
    //start0 表示这一天是否是从00点开始。
    self.getBeforeDayStr = function (day, start0) {
        if (!day) day = 0;
        var now = new Date();
        now.setTime(now.getTime() - day * 24 * 60 * 60 * 1000);
        var date = self.formatDate(now);
        if (!start0)
            return date;
        return date.substring(0, date.indexOf(' ')) + ' 00:00:00';
    }
    self.showBar = function (barDto) {
        var defs={
            id: "monitorbar",
            title: "柱状图",
            xAxis: ["消息数"],
            arry: [10]
        }

        barDto = $.extend(defs, barDto);

        var mbarChart = echarts.init(document.getElementById(barDto.id));
        var baroptionm = {
            title: {
                text: barDto.title,
                subtext: ''
            },
            color: ['#3398DB'],
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: barDto.xAxis,
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '直接访问',
                    type: 'bar',
                    barWidth: '14%',
                    data: barDto.arry
                }
            ]
        };
        mbarChart.setOption(baroptionm);
    }
    self.operateFormatter = function (value, row, index) {
        return [
            '<button class="btn btn-info btn-sm rightSize detailBtn" type="button"><i class="fa fa-paste"></i>&nbsp;详情</button>',
            '<button class="btn btn-info btn-sm rightSize packageBtn" type="button"><i class="fa fa-envelope"></i>&nbsp;打包</button>'

        ].join('');
    }
    self.getcolumns = function () {
        return [
                    {
                        checkbox: true
                    },
                    {
                        field: 'ID',
                        title: 'ID',
                        align: 'center',
                        sortable: true
                    },
                   {
                       field: 'ServiceName',
                       title: '应用名称',
                       align: 'center',
                       sortable: true
                   }, {
                       field: 'ClientIp',
                       title: '节点ip',
                       align: 'center'
                   },
                   {
                       field: 'ClientPort',
                       title: '节点端口',
                       align: 'center'
                   },
                    {
                        field: 'ServerIp',
                        title: '服务器ip',
                        align: 'center'
                    }, {
                        field: 'ServerPort',
                        title: '服务器端口',
                        align: 'center'
                    }, {
                        field: 'State',
                        title: '状态',
                        align: 'center',
                        formatter:function(s) {
                            if (s == 1) return "在线";
                            if (s == 2) return "离线";
                            return s;
                        }
                    }, {
                        field: 'CreateTime',
                        title: '更新时间',
                        align: 'center',
                        formatter: self.changeDateFormat
                    }, {
                        field: 'operate',
                        title: '操作',
                        align: 'center',
                        valign: 'middle',
                        formatter: self.operateFormatter
                    }
        ];
    }
    self.initTable = function (dto) {
        var defs= {
            url: "GetNodeStates",
            serviceName: "IM.API",
            environmentIdent: 1,
            tableName: "#apiTest-table"
        }
        dto = $.extend(defs,dto);
        var queryParams = function (params) {
            var param = {
                pageIndex: Math.ceil(params.offset / params.limit) + 1,
                pageSize: params.limit,
                serviceName: dto.serviceName,
                environmentIdent: dto.environmentIdent//1：测试，2：正式
            }
            return param;
        }
       
        var url = monitor.BusinessUrl + dto.url;//获取服务器分配情况
        var $tableName = $(dto.tableName);
        var columns = self.getcolumns();

        $tableName.bootstrapTable('destroy').bootstrapTable({
            url: url,
            method: "post",  //使用get请求到服务器获取数据 
            dataType: "json",
            toolbar: "#toolbar",
            uniqueId: "ID", //每一行的唯一标识，一般为主键列
            height: document.body.clientHeight - 200,
            cache: false, // 不缓存
            striped: true, // 隔行加亮
            pagination: true, //是否显示分页（*）
            sortable: true,                     //是否启用排序
            sortName: "ID",         //初始化的时候排序的字段
            sortOrder: "asc",                   //排序方式
            /*   search: true,                       //搜索
               strictSearch: true,*/
            /*  showColumns: true,
            showRefresh: true,*/
            queryParams: queryParams,
            queryParamsType: "limit",
            sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
            minimumCountColumns: 2, //最少允许的列数 clickToSelect: true, //是否启用点击选中行
            clickToSelect: false, //是否启用点击选中行
            pageNumber: 1, //初始化加载第一页，默认第一页
            pageSize: 50, //每页的记录行数（*）
            pageList: [10, 15, 20, 30],       //可供选择的每页的行数（*）
            paginationPreText: "Previous",
            paginationNextText: "Next",
            paginationFirstText: "First",
            paginationLastText: "Last",
            responseHandler: function (res) {
                $(".fixed-table-header").addClass('hidden');
                return {
                    "rows": res.data,
                    "total": res.count
                };
            },
            columns: columns,
            onLoadSuccess: function (data) { //加载成功时执行
                $(".layui-layer-loading,.layui-layer-shade").remove();
                console.log('success');
            },
            onLoadError: function (res) { //加载失败时执行
                $(".layui-layer-loading,.layui-layer-shade").remove();
                console.log('fail');
            }
        });

    }
    self.getLineOption=function(title) {
        return {
            title: {
                text: title,
                subtext: ''
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['cup使用情况', '内存使用情况', '网络使用情况']
                // data: ['处理器/消息数', '处理器/处理速度']
            },
            grid: {
                left: '2%',
                right: '2%',
                bottom: '2%',
                containLabel: true
            },
            toolbox: {
                show: true,
                feature: {
                    dataView: { readOnly: false },
                    saveAsImage: {} //工具，提供几个按钮，例如动态图表转换，数据视图，保存为图片等  
                }
            },
            dataZoom: {
                show: false,
                start: 0,
                end: 100
            },
            xAxis:
            {
                type: 'category',
                boundaryGap: false,
                data: self.getTimeArr()
            },
            yAxis:
            {
                type: 'value',
                scale: true,
                name: '',
                max: 100,
                min: 0,
                boundaryGap: [0.2, 0.2]
            },
            series: self.cupModule()
        };
    }
    self.getBarOption = function (title, xAxisArr, seriesArr) {
        return {
            title: {
                text: title
            },
            color: ['#3398DB'],
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: xAxisArr,
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '消息数',
                    type: 'bar',
                    barWidth: '30%',
                    data: seriesArr
                }
            ]
        };
    }
 
    self.bindEvent = function (processor) {
        $(document).on('click', '#sevenDayTabLi', function () {
            processor.showSeven();
        });
        $(document).on('click', '#toDayTabLi', function () {
            processor.showBar();
        });
        $(document).on('click', '#realTimeTabLi', function () {
            var paramid = $("#detailModal").data("ip");
            processor.initMonitor(paramid);//循环调用获取ip对应的数据;
        });
        // 查看详情
        $("#apiTest-table").on('click', '.detailBtn', function () {
            //此ip对应折线图，正常显示之后，弹框才出现;
            var paramid = $(this).parents('tr').children('td').eq(3).text();
            var servicename = $(this).parents('tr').children('td').eq(2).text();
            $("#modalTitle").empty().append('监控详情,' + servicename + "," + paramid);
            $("#detailModal").modal('show');
            $("#detailModal").data("ip", paramid);
            commonsdk.clearData();
            $("#realTimeTabLi").click();
        });
        /**
    * api监控，测试和线上 tab切换
       **/
        $("#navList").on('click', 'li', function () {
            var name = $(this).data('name');
            if (name == 'onlineTab') {
                processor.initData("#apiOnline-table", 2);
            }
        });
    }
    self.bindDetailEvent = function (processor) {
       $("#detailTab li").on('click', 'a', function () {
           commonsdk.showLayerLoad();
          var name = $(this).parent('li').data('name');
          if (name == 'realTimeTab' || name == 'sevenDayTab' || name == 'detailDataTab') {//获取实时的数据
              processor.showMonitorCharts(name);
            } else {
              processor.initMonitor();
           }
        });
        // 查看详情
        $("#apiTest-table").on('click', '.detailBtn', function () {
            //此ip对应折线图，正常显示之后，弹框才出现;
            var paramid = $(this).parents('tr').children('td').eq(3).text();
            var servicename = $(this).parents('tr').children('td').eq(2).text();
            //$("#realTimeTabLi").click();
            $("#detailTab li a").eq(0).click();
            $("#modalTitle").empty().append('监控详情,' + servicename + "," + paramid);
            $("#detailModal").modal('show');
            //$("#detailModal").data("ip", paramid);
            $("#detailId").val(paramid);
           // processor.initMonitor(paramid);
            commonsdk.clearData();
        });
    }
}
window.commonsdk = new Common();
window.cmsdk = window.commonsdk;


$(function() {
    //打包
    $("#apiTest-table").on('click', '.packageBtn', function () {
        //找到选中的节点
        var checkeds = $("#apiTest-table td input[type='checkbox']:checked");
        var ips = "";
        var append = "";
        checkeds.each(function (n) {
            var $tr = $(this).parents("tr");
            if (n != checkeds.length - 1) {
                append = ",";
            } else {
                append = "";
            }
            ips += $tr.find("td").eq(3).html() + append;
        });
        if (ips == "") {
            ips = $(this).parents('tr').find('td').eq(3).text();
        }
        $("#serverip").val('').val(ips);
        $("#servername,#appname,#version,#comboFileName").val('');
        $("#packageModal").modal('show');
    });
    //打包，提交按钮
    $("#packageModal").on('click', '#packageSubmit', function () {
        var ips = $("#serverip").val();
        var servername = $("#servername").val();
        var appname = $("#appname").val();
        var version = $("#version").val();
        var comboFileName = $("#comboFileName").val();
        if (servername == "") {
            $("#servername-error").removeClass('hidden');
            return;
        }
        if (appname == "") {
            $("#appname-error").removeClass('hidden');
            return;
        }
        if (version == "") {
            $("#version-error").removeClass('hidden');
            return;
        }
        if (comboFileName == "") {
            $("#comboFileName-error").removeClass('hidden');
            return;
        }
        //ajax调取接口
        var reqData = {
            ips: ips,
            serviceName: servername,
            appName: appname,
            version: version,
            comboFileName: comboFileName
        }
        var userId = cmsdk.getUserId()
        var token = cmsdk.getToken();
        monitor.SendPackageCMD(JSON.stringify(reqData), userId, token, function (res) {
            console.log(res);
            if (res.result == 1) {
                if (res.data == null) {
                    layer.closeAll();
                    commonsdk.showLayerMsgs('获取的数据为空，请稍后重试~');
                    return;
                }
                //var data = res.data;
                commonsdk.showLayerMsgs('打包成功', 1);
                $("#packageModal").modal('hide');
            } else {
                commonsdk.showLayerMsgs(res.message);
            }

        }, function (res) {
            commonsdk.showLayerMsgs(res.message);


        });

    });

    $('#detailModal').on('hidden.bs.modal', function (e) {
        commonsdk.clearInterval();
        cmsdk.clearInterval();
    });

  
    // 发布,查看详情
    $("#apiOnline-table").on('click', '.detailBtn', function () {
        //此ip对应折线图，正常显示之后，弹框才出现;
        var paramid = $(this).parents('tr').children('td').eq(3).text();
        var servicename = $(this).parents('tr').children('td').eq(2).text();
        //$("#realTimeTabLi").click();
        $("#detailTab li a").eq(0).click();
        $("#modalTitle").empty().append('监控详情,' + servicename + "," + paramid);
        $("#detailModal").modal('show');
        $("#detailModal").data("ip", paramid);
        $("#detailId").val(paramid);
        commonsdk.clearData();
    });
    //发布
    $("#apiOnline-table").on('click', '.publishBtn', function () {
        var ip = $(this).parents('tr').find('td').eq(3).text();
        $("#pserverip").val('').val(ip);
        $("#pservername,#pappname,#pversion,#md5Name").val('');
        //获取md5下拉列表
        commonsdk.getmd5Data();
        $("#publishModal").modal('show');
    });
    //发布，提交按钮
    $("#publishModal").on('click', '#publishSubmit', function () {
        var ips = $("#pserverip").val();
        var servername = $("#pservername").val();
        var appname = $("#pappname").val();
        var version = $("#pversion").val();
        var md5Name = $("#md5Name").val();
        if (servername == "") {
            $("#pservername-error").removeClass('hidden');
            return;
        }
        if (appname == "") {
            $("#pappname-error").removeClass('hidden');
            return;
        }
        if (version == "") {
            $("#pversion-error").removeClass('hidden');
            return;
        }
        if (md5Name == "") {
            $("#md5Name-error").removeClass('hidden');
            return;
        }
        //ajax调取接口
        var reqData = {
            ips: ips,
            serviceName: servername,
            appName: appname,
            version: version,
            md5: md5Name
        }
        var userId = cmsdk.getUserId();
        var token = cmsdk.getToken();
        monitor.SendPublishCMD(JSON.stringify(reqData), userId, token, function (res) {
            console.log(res);
            if (res.result == 1) {
                if (res.data == null) {
                    layer.closeAll();
                    commonsdk.showLayerMsgs('获取的数据为空，请稍后重试~');
                    return;
                }
                commonsdk.showLayerMsgs('发布成功', 1);
                $("#publishModal").modal('hide');
            } else {
                commonsdk.showLayerMsgs(res.message);
            }

        }, function (res) {
            commonsdk.showLayerMsgs(res.message);
        });
    });

    /**
    *  代理器 监控详情  详情记录 历史栏 点击详情按钮
    **/
    $("#detailDataTable").on('click', '.detaiPassBtn', function () {
        var reqData = {
            serviceName: 'IM.Proxy',
            clientIp: $("#detailId").val()
        }
        var userId = cmsdk.getUserId();
        var token = cmsdk.getToken();
        var datas = {};
        monitor.GetAppointModularNodeStates(JSON.stringify(reqData), userId, token, function (res) {
            console.log(res);
            if (res.result == 1) {
                if (res.data == null) {
                    layer.closeAll();
                    commonsdk.showLayerMsgs('获取的数据为空，请稍后重试~');
                    return;
                }
                var data = res.data;
                $("#detailBtnBody").empty();
                for (var i = 0; i < data.length; i++) {
                    datas.ID = data[i].ID;
                    datas.ClientIp = data[i].ClientIp;
                    datas.ClientPort = data[i].ClientPort;
                    datas.State = data[i].State;
                    datas.CreateTime = commonsdk.changeDateFormat(data[i].CreateTime);
                    var source = $("#detailBtn-templete").html();
                    var template = doT.template(source);
                    var htmlstr = template(datas);
                    $("#detailBtnBody").append(htmlstr);
                }
                $("#detailBtnModal").modal('show');

            } else {
                commonsdk.showLayerMsgs(res.message);
            }
        }, function (res) {
            commonsdk.showLayerMsgs(res.message);
        });
    });
    /**
    *  发布详情，点击md5下拉框，获取md5列表
    **/

    /*  $("#md5Name").bsSuggest({
            data: cmsdk.dataList,                       //提示所用的数据
            effectiveFields: ["ID", "Type", "Version", "FileName", "MD5"],
            searchFields: ["MD5"],
            ignorecase: true,
            showHeader: true,
            showBtn: false,     //不显示下拉按钮
            delayUntilKeyup: true, //获取数据的方式为 firstByUrl 时，延迟到有输入/获取到焦点时才请求数据
            idField: "ID",
            keyField: "MD5",
            clearable: true
        }).on('onDataRequestSuccess', function (e, result) {
            console.log('onDataRequestSuccess: ', result);
        }).on('onSetSelectValue', function (e, keyword, data) {
            console.log('onSetSelectValue: ', keyword, data);
        }).on('onUnsetSelectValue', function () {
            console.log("onUnsetSelectValue");
        });*/
})
