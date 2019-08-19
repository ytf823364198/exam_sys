/**
 * Created by huan on 09/03/2017.
 */

var monitor = {};
monitor.BusinessUrl = '/proxy/ImMonitor/Business/';
monitor.AuthUrl = '/proxy/ImMonitor/Auth/';


//登陆
monitor.Login = function (data, success, error) {

    if (!data || !success || !error) {
        console.log('Login  传入参数为空');

        return;
    }
    $.ajax({
        url: monitor.AuthUrl + 'Login',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,

        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//1.1、从数据库接口拉取用户登录日志
monitor.GetUserLoginLogFromInterface = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('GetUserLoginLogFromInterface ()传入参数为空');
        return;
    }
    $.ajax({
        url: monitor.BusinessUrl + 'GetUserLoginLogFromInterface',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
            console.log(res);
        },
        error: function (res) {
            error(res);
            console.log(res);
        }
    });
};

//1.2、获取用户信息
monitor.GetUserInfo = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetUserInfo ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetUserInfo',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//1.3、从数据库接口拉取用户消息收发日志
monitor.GetUserConnectorContextLogFromDB = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetUserConnectorContextLogFromDB ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetUserConnectorContextLogFromDB',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//1.4、从数据库接口拉取用户Socket输入流日志
monitor.GetUserConnectorInputStreamLogFromDB = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetUserConnectorInputStreamLogFromDB ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetUserConnectorInputStreamLogFromDB',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//1.5、从数据库接口拉取用户Socket输出流日志
monitor.GetUserConnectorOutputStreamLogFromDB = function (data, userId, token, success, error) {

    if (!data || !userId || !token || !success || !error) {
        console.log('GetUserConnectorOutputStreamLogFromDB ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetUserConnectorOutputStreamLogFromDB',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//1.6、获取指定PC的性能
monitor.GetSystemInfoEntityFromMemory = function (data, userId, token, success, error) {
    if (!data || !userId || !token || !success || !error) {
        console.log('GetSystemInfoEntityFromMemory  ()传入参数为空');
        return;
    }
    $.ajax({
        url: monitor.BusinessUrl + 'GetSystemInfoEntityFromMemory',
        method: 'POST',
        cache: false,
        async:false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//1.6、获取指定PC的性能:批量
monitor.GetSystemInfoEntitysFromMemory = function (data, userId, token, success, error) {
    if (!data || !userId || !token || !success || !error) {
        console.log('GetSystemInfoEntitysFromMemory  ()传入参数为空');
        return;
    }
    $.ajax({
        url: monitor.BusinessUrl + 'GetSystemInfoEntitysFromMemory',
        method: 'POST',
        cache: false,
        async: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//1.7、获取接口调用情况
monitor.getLiveLogFromDB = function (data, userId, token, success, error) {
    if (!data || !userId || !token || !success || !error) {
        console.log('getLiveLogFromDB  ()传入参数为空');
        return;
    }
    $.ajax({
        url: monitor.BusinessUrl + 'GetLiveLogFromDB ',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//1.8、获取服务器分配情况
monitor.GetServerDistributionFromDB = function (data, userId, token, success, error) {
    if (!data || !userId || !token || !success || !error) {
        console.log('GetServerDistributionFromDB  ()传入参数为空');
        return;
    }
    $.ajax({
        url: monitor.BusinessUrl + 'GetServerDistributionFromDB ',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//1.9、获取指定模块的接口调用统计
monitor.GetInterfaceCallTimesFromMemory = function (data, userId, token, success, error) {
    if (!data || !userId || !token || !success) {
        console.log('GetInterfaceCallTimesFromMemory  ()传入参数为空');
        return;
    }
    $.ajax({
        url: monitor.BusinessUrl + 'GetInterfaceCallTimesFromMemory ',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error&&error(res);
        }
    });
};

monitor.GetInterfaceCallTimesFromDB = function (data, userId, token, success, error) {
    if (!data || !userId || !token || !success) {
        console.log('GetInterfaceCallTimesFromDB  ()传入参数为空');
        return;
    }
    $.ajax({
        url: monitor.BusinessUrl + 'GetInterfaceCallTimesFromDB ',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error && error(res);
        }
    });
};

//获取处理器实时/7天的数据
monitor.GetProcesssFromDB = function (data, userId, token, success, error) {
    if (!data || !userId || !token || !success) {
        console.log('GetProcesssFromDB()传入参数为空');
        return;
    }
    $.ajax({
        url: monitor.BusinessUrl + 'GetProcesssFromDB',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error && error(res);
        }
    });
};
//处理异常消息-数据库
monitor.SetExceptionMessageToDB = function (data, userId, token, success, error) {
    if (!data || !userId || !token || !success) {
        console.log('SetExceptionMessageToDB()传入参数为空');
        return;
    }
    $.ajax({
        url: monitor.BusinessUrl + 'SetExceptionMessageToDB',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error && error(res);
        }
    });
};
//获取数据库调度员/数据库接收的数据
monitor.GetDBInfo = function (data, userId, token, success, error) {
    if (!data || !userId || !token || !success) {
        console.log('GetDBInfo()传入参数为空');
        return;
    }
    $.ajax({
        url: monitor.BusinessUrl + 'GetDBInfo',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error && error(res);
        }
    });
};
//1.9、获取代理器信息
monitor.GetProxyInfoFromMemory = function (data, userId, token, success, error) {
    if (!data || !userId || !token || !success || !error) {
        console.log('GetProxyInfoFromMemory  ()传入参数为空');
        return;
    }
    $.ajax({
        url: monitor.BusinessUrl + 'GetProxyInfoFromMemory ',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};


//2.1、帐户注册
monitor.Regist = function (data, userId, token, success, error) {

    if (!data || !userId || !token || !success || !error) {
        console.log('Regist ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'Regist',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userId,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//2.2、更新用户信息
monitor.UpdateAcount = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('UpdateAcount ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'UpdateAcount',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//2.3、获取用户信息
monitor.GetAcountInfo = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetAcountInfo ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetAcountInfo',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//2.4、获取用户列表
monitor.GetAcountList = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetAcountList ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetAcountList',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//2.3、添加用户
monitor.AddAcount = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('GetAcountInfo ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'AddAcount',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//2.5、用户是否已存在
monitor.IsAcountExist = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('IsAcountExist ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'IsAcountExist',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//2.6、移除用户
monitor.DeleteAcount = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('DeleteAcount ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'DeleteAcount',
        method: 'POST',
        cache: false,
       /* async:false,*/
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//3.1、添加角色
monitor.InsertRole = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('InsertRole ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'InsertRole',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//3.2、获取角色信息
monitor.GetRoleInfo = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetRoleInfo ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetRoleInfo',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//3.3、获取角色列表
monitor.GetRoleList = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetRoleList ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetRoleList',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//3.4、角色是否存在
monitor.IsRoleExist = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('IsRoleExist ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'IsRoleExist',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//3.4、发送打包命令
monitor.SendPackageCMD = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('SendPackageCMD ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'SendPackageCMD',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//3.4、发送发布命令
monitor.SendPublishCMD = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('SendPublishCMD ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'SendPublishCMD',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//3.4、获取已发布的包信息列表
monitor.GetPackageList = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('GetPackageList ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetPackageList',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//获取异常消息
monitor.GetExceptionMessageFromDB = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('GetExceptionMessageFromDB ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetExceptionMessageFromDB',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//获取空消息
monitor.GetNullMessageFromDB = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('GetNullMessageFromDB ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetNullMessageFromDB',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//获取所有当前在用节点
monitor.getpcsFromMemory = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('getpcsFromMemory ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'getpcsFromMemory',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//获取连接器实时/7天的数据
monitor.GetConnectorsFromDB = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('GetConnectorsFromDB ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetConnectorsFromDB',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//获取代理器信息
monitor.GetProxyInfosFromDB = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('GetProxyInfosFromDB ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetProxyInfosFromDB',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//获取指定模块的节点启停历史列表
monitor.GetAppointModularNodeStates = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('GetAppointModularNodeStates ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetAppointModularNodeStates',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//3.4、获取打包记录列表
monitor.GetPackageCMDList = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('GetPackageCMDList ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetPackageCMDList',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//3.4、获取发布记录列表
monitor.GetPublishCMDList = function (data, userID, token, success, error) {
    if (!data || !userID || !token || !success || !error) {
        console.log('GetPackageCMDList ()传入参数为空');
        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetPublishCMDList',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};
//3.5、更新角色信息
monitor.UpdateRole = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('UpdateRole ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'UpdateRole',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//3.6、移除角色信息
monitor.DeleteRole = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('DeleteRole ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'DeleteRole',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//4.1、添加权限项
monitor.InsertAuthorityMapping = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('InsertAuthorityMapping ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'InsertAuthorityMapping',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//4.2、获取权限项信息
monitor.GetAuthorityMappingInfo = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetAuthorityMappingInfo ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetAuthorityMappingInfo',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//4.3、获取权限项列表
monitor.GetAuthorityMappingList = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetAuthorityMappingList ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetAuthorityMappingList',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//4.4、获取某个角色的权限项
monitor.GetAuthorityMappingListByRole = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetAuthorityMappingListByRole ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetAuthorityMappingListByRole',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//4.5、获取某个用的权限项
monitor.GetAuthorityMappingListByAcount = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetAuthorityMappingListByAcount ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetAuthorityMappingListByAcount',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//4.6、检查当前用户的权限
monitor.HasPermission = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('HasPermission ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'HasPermission',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//4.7、清除某角色的权限选项
monitor.DeleteByRole = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('DeleteByRole ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'DeleteByRole',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//4.8、清除某用户的权限选项
monitor.DeleteByAcount = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('DeleteByAcount ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'DeleteByAcount',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//5.1、添加菜单
monitor.InsertMenu = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('InsertMenu ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'InsertMenu',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//5.2、更新菜单
monitor.UpdateMenu = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('UpdateMenu ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'UpdateMenu',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//5.3、移除菜单
monitor.DeleteMenu = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('DeleteMenu ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'DeleteMenu',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//5.4、获取全部菜单列表
monitor.GetMenuList = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetMenuList ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetMenuList',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

//5.5、根据权限配置，获取菜单列表
monitor.GetMenuListByAcount = function (data, userID, token, success, error) {

    if (!data || !userID || !token || !success || !error) {
        console.log('GetMenuListByAcount ()传入参数为空');

        return;
    }
    $.ajax({
        url: this.BusinessUrl + 'GetMenuListByAcount',
        method: 'POST',
        cache: false,
        contentType: 'application/json',
        data: data,
        headers: {
            "userID": userID,
            "token": token
        },
        success: function (res) {
            success(res);
        },
        error: function (res) {
            error(res);
        }
    });
};

