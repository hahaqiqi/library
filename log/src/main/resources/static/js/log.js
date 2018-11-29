layui.config({
    base: '/js/',
}).extend({
    timePicker: 'timePicker'
});

layui.use(['form', 'layer', 'laytpl', 'jquery', 'table', "laypage","timePicker"], function () {
    var form = layui.form
        , layer = layui.layer
        , laytpl = layui.laytpl
        , table = layui.table
        , laypage = layui.laypage
        , $ = layui.$
        , timePicker = layui.timePicker;

    //分页
    laypage.render({});

    $(function () {

        loadLog();

        //  加载日志
        function loadLog(date) {
            $("#view").html("");
            $.ajax({
                url: "/log/selectDate.json",
                data:{
                    "date" : date
                },
                success: function (result) {
                    if (result.ret) {
                        var date = result.data;
                        if (date) {
                            if(date.length <= 0){
                                layer.msg("当前时间段没有任何记录");
                                return;
                            }
                            for (var i = 0; i < date.length; i++) {
                                var data;
                                if (date.length - 1 == i) {
                                    data = {
                                        "date": date[i]
                                        , "isEnd": true
                                    };
                                } else {
                                    data = {
                                        "date": date[i]
                                        , "isEnd": false
                                    };
                                }
                                var getTpl = $("#logTemp").html();
                                laytpl(getTpl).render(data, function (html) {
                                    $("#view").append(html);
                                });
                            }
                        }
                    } else {
                        spopFail("加载日期时间", result.msg);
                    }
                }
            });
        }

        //处理日期格式
        function createTime(v) {
            var date = new Date(v.operateTime);
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            m = m < 10 ? '0' + m : m;
            var d = date.getDate();
            var h = date.getHours();
            d = d < 10 ? ("0" + d) : d;
            h = h < 10 ? ("0" + h) : h;
            var M = date.getMinutes();
            M = M < 10 ? ("0" + M) : M;
            var s=date.getSeconds(); //秒
            var str = y + "-" + m + "-" + d + " " + h + ":" + M+":"+s;
            return str;
        }

        //  绑定每个日期点击事件
        $(document).on('click', '.start', function (e) {
            if ($(this).next().children(".myTable").html() == "") {
                $(".start").html("&#xe602;");
                $(".start").next().children(".myTable").html("");
                $(this).html("&#xe61a;");
                var date = $(this).next().children(".layui-timeline-title").text();
                var data = {};
                var getTpl = $("#tableTemp").html()
                    , view = $(this).next().children(".myTable");
                laytpl(getTpl).render(data, function (html) {
                    view.html(html);
                });
                //执行一个 table 实例(加载数据)
                table.render({
                    elem: "#demo",
                    height: 315,
                    url: '/log/page.json?date=' + date,
                    cols: [[ //标题栏
                        {field: 'id', title: 'ID', width: 60, hide: true, sort: true}
                        , {field: 'logTypeName', title: '操作类型', width: 100}
                        , {field: 'targetid', title: '目标ID', width: 100}
                        , {field: 'oldValue', title: '改变前的值', width: 100}
                        , {field: 'newValue', title: '改变后的值', width: 100}
                        , {field: 'operator', title: '操作人', width: 100}
                        , {field: 'operateTime', title: '创建时间', width: 200, sort: true, align: 'center',templet:function(operateTime){return createTime(operateTime)}}
                        , {field: 'remark', title: '备注'}
                    ]],
                    page: true,
                    done: function (res, curr, count) {
                        //如果是异步请求数据方式，res即为你接口返回的信息。
                        //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                        console.log(res);
                        //得到当前页码
                        console.log(curr);
                        //得到数据总量
                        console.log(count);
                    }
                });
                form.render();
            } else {
                $(this).html("&#xe602;");
                $(this).next().children(".myTable").html("");
            }
        });


        $("#submitBtn").click(function() {
            var value = $("#date").val();
            loadLog(value);
        });

        function renderTime() {
            timePicker.render({
                elem: '#date', //定义输入框input对象
                options: { //可选参数timeStamp，format
                    timeStamp: false, //true开启时间戳 开启后format就不需要配置，false关闭时间戳 //默认false
                    format: 'YYYY-MM-DD', //格式化时间具体可以参考moment.js官网 默认是YYYY-MM-DD HH:ss:mm
                },
            });
        }

        $("#date,#seachLable").click(function() {
            $("#date").focus();
            $("#dg").css({
                "width": "340px",
                "transition": "width 0.5s"
            });
            $("#date").attr("unfold", "true");
            $('#seach').hide();
            $('#seachLable').hide();
            $('#submitBtn').show();
            // window.setTimeout(renderTime, 500);
            timePicker.render({
                elem: '#date', //定义输入框input对象
                options: { //可选参数timeStamp，format
                    timeStamp: false, //true开启时间戳 开启后format就不需要配置，false关闭时间戳 //默认false
                    format: 'YYYY-MM-DD', //格式化时间具体可以参考moment.js官网 默认是YYYY-MM-DD HH:ss:mm
                },
            });
        });
        var flag = 0;

        $(document).on('mouseover mouseout', '.timePicker', function(e) {
            if(event.type == "mouseover") {
                flag = 1;
            } else if(event.type == "mouseout") {
                flag = 0;
            }
        });

        $("#date").blur(function() {
            var len = $("#date").val();
            if(len.length > 3) {
                return;
            }
            if(flag == 1) {
                return;
            }
            $("#dg").css({
                "width": "110px",
                "transition": "width 0.5s"
            });
            $('#seach').show();
            $('#seachLable').show();
            $('#submitBtn').hide();
            $(".timePicker").remove();
        });
    });

});