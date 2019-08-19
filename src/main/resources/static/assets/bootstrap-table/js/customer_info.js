var CustomerInfo = function (config) {
        var self = this;
        /**
        *  时间格式转化为"yyyy-mm-dd hh:MM:ss"
        **/
        var formatDate = function (date, type) {
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
        /**
          *  初始化四个日期控件的格式；
          *  最大日期：为当前日期；
          *  最小日期：为当前日前00：00：00
          **/
        self.initLayerdate = function () {
            var endDefaultDate = formatDate(new Date());
            var startDefaultDate = endDefaultDate.substring(0, endDefaultDate.indexOf(' ')) + ' 00:00:00';
            $("#dateSearch").find('.startDate').attr('onclick', "laydate({ istime: true, istoday: true, format: 'YYYY-MM-DD hh:mm:ss', min: '2000-01-01 00:00:00', max:laydate.now()})").val(startDefaultDate);
            $("#dateSearch").find('.endDate').attr('onclick', "laydate({ istime: true, istoday: true, format: 'YYYY-MM-DD hh:mm:ss', min: '2000-01-01 00:00:00', max:laydate.now() })").val(endDefaultDate);
        }
       
        var queryParams = function (params) {
            var param = {
                pageIndex: Math.ceil(params.offset / params.limit) + 1,
                pageSize: params.limit,
                order: params.order,
                ordername: params.sort,
                startDateTime: $("#dateSearch .startDate").val(),
                endDateTime: $("#dateSearch .endDate").val(),
                search: $("#dateSearch .imuserid").val()
            };
            return param;
        }

        var responseHandler = function (e) {
            console.log(e);
            if (e.data && e.data.length > 0) {
                return { "rows": e.data, "total": e.count };
            }
            else {
                return { "rows": [], "total": 0 };
            }
           
        }
        var uidHandle = function (res) {
            var html = "<a href='#'>"+ res + "</a>";
            return html;
        }
        var operateFormatter = function (value, row, index) {//赋予的参数
            return [
                '<button class="btn btn-info btn-sm rightSize detailBtn" type="button"><i class="fa fa-paste"></i>&nbsp;详情</button>',
                '<button class="btn btn-danger btn-sm rightSize packageBtn" type="button"><i class="fa fa-envelope"></i>&nbsp;通知</button>'
            ].join('');
        }
        var rowStyle = function (row, index) {
            var classesArr = ['success', 'info'];
            if (index % 2 === 0) {//偶数行
                return { classes: classesArr[0] };
            } else {//奇数行
                return { classes: classesArr[1] };
            }
        }
        self.dataInit = function (name) {
            var url, tableName;
            var columns = [
                {
                    checkbox: true
                },
                {
                    field: 'uid',
                    title: '用户编号',
                    align: 'center',
                    formatter: uidHandle,//自定义方法设置uid跳转链接
                    width: 300,
                    /*colspan: 2*/
                }, {
                    field: 'name',
                    title: '姓名',
                    align: 'center',
                    sortable: false //本列不可以排序
                }, {
                    field: 'sex',
                    title: '性别',
                    align: 'center'
                }, {
                    field: 'age',
                    title: '年龄',
                    align: 'center',
                    sortable: true,
                    clickToSelect: false,
                    sortName: "age",
                    order: "asc"
                }, {
                    field: 'area',
                    title: '户籍所在地',
                    align: 'left',
                    halign: 'center' //设置表头列居中对齐
                }, {
                    field: 'loginWay',
                    title: '登录方式',
                    align: 'center'
                }, {
                    field: 'status',
                    title: '状态',
                    align: 'center'
                }, {
                    field: 'createTime',
                    title: '登录时间',
                    align: 'center',
                    width: 90
                }, {
                    field: 'orderService',
                    title: '购买服务',
                    align: 'center'
                }, {
                    field: 'connectorIP',
                    title: '连接器IP',
                    align: 'center'
                }, {
                    field: 'connectorPort',
                    title: '连接器端口',
                    align: 'center'
                }, {
                    field: 'operate',
                    title: '操作',
                    align: 'center',
                    valign: 'middle',
                    formatter: operateFormatter //自定义方法，添加操作按钮
                }
            ];

            switch (name) {
                case 'loginLogTab':
                    tableName = "loginLog-table";
                    break;
                case 'receiveLogTab':
                    //省略
                    break;
                case 'socketInputTab':
                    tableName = "sockctInput-table";
                    $.ajax({
                        url: '../data/login_info2.json',
                        method: 'get',
                        dataType: 'json',
                        contentType: "application/json",
                        success: function (res) {
                            console.log(res);
                            $('#' + tableName).bootstrapTable('destroy').bootstrapTable({
                                data: res.data,
                                toolbar: "#toolbar",             
                                uniqueId: "id",                    
                                height: document.body.clientHeight - 205,  
                                cache: false,                    
                                striped: true,
                                sortOrder: 'asc',
                                sortStable: false,
                                columns: columns,
                                onSort: function (a,b) {
                                    return a - b;
                                }
                            })
                            return;
                        },
                        error: function (res) {
                            console.log(res);
                        }
                    })
                    //省略
                    break;
                case 'socketOutputTab':
                    //省略
                    break;
            }

            $('#' + tableName).bootstrapTable('destroy').bootstrapTable({
                url: '../data/login_info2.json',   //url一般是请求后台的url地址,调用ajax获取数据。此处我用本地的json数据来填充表格。
                method: "get",                     //使用get请求到服务器获取数据 
                dataType: "json",
                contentType: 'application/json,charset=utf-8',
                toolbar: "#toolbar",                //一个jQuery 选择器，指明自定义的toolbar 例如:#toolbar, .toolbar.
                uniqueId: "id",                    //每一行的唯一标识，一般为主键列
                height: document.body.clientHeight-205,   //动态获取高度值，可以使表格自适应页面
                cache: false,                       // 不缓存
                striped: true,                      //是否显示行间隔色
                queryParamsType: "limit",           //设置为"undefined",可以获取pageNumber，pageSize，searchText，sortName，sortOrder  
                                                    //设置为"limit",符合 RESTFul 格式的参数,可以获取limit, offset, search, sort, order  
                queryParams: queryParams,
                sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
               // sortable: true,                     //是否启用排序;意味着整个表格都会排序
                sortName: 'uid',                    // 设置默认排序为 name
                sortOrder: "asc",                   //排序方式
                pagination: true,                   //是否显示分页（*）
                search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
                strictSearch: true,
                showColumns: true,                  //是否显示所有的列
                showRefresh: true,                  //是否显示刷新按钮
                showToggle:true,                    //是否显示详细视图和列表视图
                clickToSelect: true,                //是否启用点击选中行
                minimumCountColumns: 2,          //最少允许的列数 clickToSelect: true, //是否启用点击选中行
                pageNumber: 1,                   //初始化加载第一页，默认第一页
                pageSize: 10,                    //每页的记录行数（*）
                pageList: [10, 25, 50, 100],     //可供选择的每页的行数（*）
                paginationPreText: "Previous",
                paginationNextText: "Next",
                paginationFirstText: "First",
                paginationLastText: "Last",
                responseHandler: responseHandler,
                columns: columns,
                rowStyle:rowStyle,
                onLoadSuccess: function (data) { //加载成功时执行
                    console.log(data);
                },
                onLoadError: function (res) { //加载失败时执行
                    console.log(res);
                }
            });
        }
    }

window.CustomerInfo = new CustomerInfo();

$(function () {

    window.onload = function () {
        CustomerInfo.initLayerdate();
        $("#navList li a").eq(0).click();
    }

    $("#navList li").on('click', 'a', function () {
        var name = $(this).parent('li').data('name');
        localStorage.setItem('tabName', name);
        CustomerInfo.dataInit(name);//点击每个tab页面，异步获取数据
    });

    $("#dateSearch").on('click', '.search', function () {
        var tabName = localStorage.getItem('tabName');
        if (tabName == null) {
            tabName = "loginLogTab";
        }
        CustomerInfo.dataInit(tabName); //初始化获取"登录日志"的数据
    });

    function operateFormatter(value, row, index) {//赋予的参数
        return [
            '<button class="btn btn-info btn-sm rightSize detailBtn" type="button"><i class="fa fa-paste"></i>&nbsp;详情</button>',
            '<button class="btn btn-danger btn-sm rightSize packageBtn" type="button"><i class="fa fa-envelope"></i>&nbsp;通知</button>'
        ].join('');
    }
  
    var rowStyle = function (row, index) {
        var classesArr = ['success', 'info'];
        if (index % 2 === 0) {//偶数行
            return { classes: classesArr[0] };
        } else {//奇数行
            return { classes: classesArr[1] };
        }
    }
    window.operateFormatter = operateFormatter;
    window.rowStyle = rowStyle;
});
  
