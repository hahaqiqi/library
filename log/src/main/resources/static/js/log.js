layui.use(['form', 'layer', 'laytpl', 'jquery', 'table', "laypage"], function () {
    var form = layui.form
        , layer = layui.layer
        , laytpl = layui.laytpl
        , table = layui.table
        , laypage = layui.laypage
        , $ = layui.$;

    //分页
    laypage.render({});

    $(function () {

        loadLog();

        //  加载日志
        function loadLog() {
            $.ajax({
                url: "/log/selectDate.json",
                success: function (result) {
                    if (result.ret) {
                        var date = result.data;
                        if (date || date.length > 0) {
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
                        , {field: 'operateTime', title: '创建时间', width: 200, sort: true, align: 'center'}
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

    });

});