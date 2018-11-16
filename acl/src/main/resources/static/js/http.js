/**
 * author: javan
 * date: 2017-09-29 09:40
 * desc: 全局控制http请求
 */
;
layui.define(['jquery'], function(exports) {
    var $ = layui.jquery,
        layer = layui.layer,
        config = {
            api: '',
            token: localStorage.getItem('xtoken') || ''
        };
    var ERROR_LIST = {
        '404': '接口地址不对'
    };

    var http = {
        /** url: 请求接口地址,
            type: 请求类型 POST GET,
            json: 数据请求方式,
            mask: 是否有loading,
            data: 请求参数
         */
        ajax: function(options) {
            var loadding = '';
            let def = $.Deferred();
            if (options.mask) {
                loadding = layer.msg('加载中', {
                    icon: 16,
                    shade: 0.01,
                    time: 0
                });
            }
            $.ajax({
                url: config.api + options.url,
                data: options.data,
                type: options.type,
                headers: {
                    'x-auth-token': config.token
                },
                contentType: options.json ? 'application/json;charset=UTF-8' : 'application/x-www-form-urlencoded'
            }).then(function(rsp) {
                def.resolve(rsp);
                setTimeout(function(){
                    layer.close(loadding);
                },100)
            }, function(error) {
                if(error.status==504){
                    layer.msg('请求超时，请重试', {
                        icon: 5
                    });
                }else if(error.responseText){
                    var err = JSON.parse(error.responseText);
                    var code = err.code;
                    var emsg = err.message;
                    switch (code) {
                        case 2022:
                            localStorage.removeItem ('userInfo');
                            localStorage.removeItem ('roleMenu');
                            localStorage.removeItem ('xtoken');
                            layer.msg(emsg, {
                                icon: 5
                            }, function() {
                                top.location = '/login.html';
                            });
                            break;
                    }
                }
                def.reject(error);
                setTimeout(function(){
                    layer.close(loadding);
                },100)
            });
            return def;
        },
        resStatus: function(code) {
            var msg = ERROR_LIST[code];
            return msg;
        },
        getUrlParam: function(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
            var r = window.location.search.substr(1).match(reg); //匹配目标参数
            if (r != null){
                return unescape(r[2]);
            };
            return null; 
        }
    }
    exports('http', http);

})