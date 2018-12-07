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
    var s = date.getSeconds(); //秒
    var str = y + "-" + m + "-" + d + " " + h + ":" + M + ":" + s;
    return str;
}

var pageOldMax;
var sliderPage;
var pageMax;

function pageCh(res, curr, count) {
    layui.use(['slider', 'jquery'], function () {
        var slider = layui.slider //滑块
            , $ = layui.$                  //jquery
        pageMax = Math.ceil(count / res.limit);
        if (pageOldMax != pageMax) {
            pageOldMax = pageMax;
            sliderPage = slider.render({
                elem: '#slidePage'  //绑定元素
                , min: 0
                , max: pageMax
                , value: curr
                , showstep: true
                , change: function (value) {
                    if (value == 0) {
                        return;
                    }
                    if (value != $('.layui-input').val()) {
                        $('.layui-input').val(value)
                        $('.layui-laypage-btn').click();
                    }
                }
            });
        } else {
            sliderPage.setValue(curr);
        }
    });
}

layui.config({
    version: '1541881042991' //为了更新 js 缓存，可忽略
});

layui.use(['form', 'laypage', 'layer', 'table', 'slider', 'laytpl', 'jquery', 'laydate'], function () {
    var form = layui.form //表单
        , laypage = layui.laypage //分页
        , layer = layui.layer //弹层
        , table = layui.table //表格
        , laytpl = layui.laytpl //模板
        , slider = layui.slider
        , $ = layui.$//jquery
        , laydate = layui.laydate;


    //向世界问个好
    //layer.msg('Hello World');

    //执行一个 table 实例(加载数据)
    var book;
    var user;
    getFormData = function (elem) {
        var fieldElem = $(elem).find('input,select,textarea'); //获取所有表单域
        var field = {};
        layui.each(fieldElem, function (_, item) {
            if (!item.name) return;
            if (/^checkbox|radio$/.test(item.type) && !item.checked) return;
            field[item.name] = item.value;
        });
        return field;
    };


    //执行一个 table 实例(加载数据)
    //获取查询表单的参数
    var formData = getFormData("#bookLeaseForm");
    tableSx = function () {
        table.render({
            elem: '#bookLeaseTable'
            , url: "/bookLease/list.json"//数据接口
            , id: 'test'
            , response: {
                statusCode: 0 //规定成功的状态码，默认：0
            }
            ,toolbar:"#demo"
            , page: true
            , cols: [
                [ //表头
                    {
                        field: 'bookId', title: '书籍编码', fixed: 'left', templet: function (obj) {
                            $.ajax({
                                url: '/book/select.json',
                                data: {"id": obj.bookId},
                                async: false,
                                type: 'GET',
                                success: function (result) {
                                    if (result.data != null) {
                                        book = result.data;
                                    }
                                }
                            });
                            return book.bookCode;
                        }
                    }
                    , {
                    field: '', title: '书籍名字', templet: function () {
                        return book.bookName;
                    }
                }
                    , {field: 'serialNumber', title: '订单编码',hide:true}
                    , {
                    field: 'userId', title: '用户邮箱', templet: function (obj) {
                        $.ajax({
                            url: '/user/selectUserById.json',
                            data: {"id": obj.userId},
                            async: false,
                            type: 'GET',
                            success: function (result) {
                                if (result.data != null) {
                                    user = result.data;
                                }
                            }
                        });
                        return user.userEmail;
                    }
                }
                    , {field: 'bookPrice', title: '书籍原价',hide:true}
                    , {field: 'discount', title: '折扣',hide:true}
                    , {field: 'status', title: '书籍状态',hide:true}
                    , {field: 'operateTime', title: '租借时间'}
                    , {field: 'price', title: '实际收费'}
                    , {field: 'finalOperateTime', title: '归还时间'}
                    , {field: 'remark', title: '备注'}
                ]
            ],
            where: formData,

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

        //执行一个laydate实例
        laydate.render({
            elem: '#starTime' //指定元素
        });

        //执行一个laydate实例
        laydate.render({
            elem: '#endTime' //指定元素
        });
    }
    tableSx();
    $('#submit').on("click", function () {
        var bookCode = $("#bookCode").val();
        var email = $("#userEmail").val();
        if (bookCode != null && bookCode != "") {
            $.ajax({
                url: '/book/getIdByCode.json',
                data: {"bookCode": bookCode},
                async: false,
                type: 'GET',
                success: function (result) {
                    if (result.data != null) {
                        $("#bookId").val(result.data.id);
                    } else {
                        $("#bookId").val(-1);
                    }
                }
            });
        } else {
            $("#bookId").val("");
        }
        if (email != null && email != "") {
            $.ajax({
                url: '/user/selectUserByEmail.json',
                data: {"email": email},
                async: false,
                type: 'GET',
                success: function (result) {
                    if (result.data != null) {
                        $("#userId").val(result.data.id);
                    } else {
                        $("#userId").val(-1);
                    }
                }
            });
        } else {
            $("#userId").val("");
        }
        formData = getFormData("#bookLeaseForm");
        tableSx();
    });

    $('#reset').on("click", function () {
        $("#bookId").val("");
        $("#userId").val("");
        formData = getFormData("#bookLeaseForm");
        tableSx();
    });

    var editObj;
    //监听头工具栏事件
    table.on('toolbar(test)', function (obj) {


    });

    //监听行工具事件
    table.on('tool(test)', function (obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
            , layEvent = obj.event; //获得 lay-event 对应的值
        if (layEvent === 'detail') {
            var viewdata = { //数据
                "bookTypeName": data.typeName
                , "remark": data.remark
                , "submitType": 0
            }
            showBookTypeInfo("查看", viewdata);
        } else if (layEvent === 'del') {
            layer.confirm('确认删除这一条数据?', function (index) {
                layer.close(index);
                //layer.msg("删除"+data.id);
                //向服务端发送删除指令
                if (deleteBookType(data.id)) {
                    $(".layui-laypage-btn")[0].click();
                    //obj.del(); //删除对应行（tr）的DOM结构
                    spopSucess("删除成功");
                } else {
                    spopFail("删除失败", "该项可能已经不存在");
                }
                ;
            });
        } else if (layEvent === 'edit') {
            editObj = data;
            var viewdata = { //数据
                "bookTypeName": data.typeName
                , "remark": data.remark
                , "submitType": 2
                , "id": data.id
            }
            showBookTypeInfo("编辑", viewdata);
        }
    });

    //分页
    laypage.render({});

    function showBookTypeInfo(showTitleType, viewdata) {
        var getTpl = demo.innerHTML
            , view = document.getElementById('view');
        laytpl(getTpl).render(viewdata, function (html) {
            view.innerHTML = html;
        });
        layer.open({
            title: showTitleType + '图书类型',
            type: 1,
            content: $("#view"),
            shade: 0,
            area: '350px',
            success: function () {
                //监听提交数据
                //submitType 0为查看，=1为新增，2为编辑
                $('#submitBookType').on("click", function () {
                    var submitType = $(this).attr("submitType");
                    $.ajax({
                        url: submitType == 1 ? '/bookType/save.json' : '/bookType/update.json',
                        data: $("#bookTypeForm").serializeArray(),
                        type: 'POST',
                        success: function (result) {
                            if (result.ret) {
                                spopSucess(submitType == 1 ? "添加成功" : "修改成功");
                                layer.close(layer.index);
                                $(".layui-laypage-btn")[0].click();
                                $("#view").html("");
                            } else {
                                spopFail(submitType == 1 ? "添加失败" : "修改失败", result.msg);
                            }
                        }
                    });
                });
            },
            cancel: function () {
                $("#view").html("");
            }
        });
    }


    //删除单条数据
    function deleteBookType(delid) {
        var delSuccess = false;
        $.ajax({
            url: '/bookType/delete.json',
            data: {"id": delid},
            async: false,
            type: 'GET',
            success: function (result) {
                delSuccess = result.ret;
            }
        });
        return delSuccess;
    }

    //批量删除数据
    function batchDeleteBookType(batchStrId) {
        var delSuccessCount = 0;
        $.ajax({
            url: '/bookType/batchDelete.json',
            data: {"batchStrId": batchStrId},
            async: false,
            type: 'GET',
            success: function (result) {
                delSuccessCount = result.msg;
            }
        });
        return delSuccessCount;
    }


});


