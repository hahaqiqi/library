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


layui.config({
    version: '1541881042991' //为了更新 js 缓存，可忽略
});

layui.use(['form', 'laypage', 'layer', 'table', 'slider', 'laytpl', 'jquery'], function () {
    var form = layui.form //表单
        , laypage = layui.laypage //分页
        , layer = layui.layer //弹层
        , table = layui.table //表格
        , laytpl = layui.laytpl //模板
        , slider = layui.slider
        , $ = layui.$//jquery


    form.on('radio(searchType)', function (data) {
        $("#searchInput").attr("placeholder", data.value);
    });

    form.render();

    //回车事件
    $('#searchInput').keyup(function (event) {
        if (event.keyCode == 13) {
            $('#searchBut').click();
        }
    });

    //点击搜索图书
    $('#searchBut').on("click", function () {
        var url = "/book/searchBook.json?searchVal=";
        var searchVal = $("#searchInput").val().replace(/\s+/g, "");
        if (searchVal == "") {
            return;
        }
        //判断输入的是bookCode/userEmail
        if ($("#userEmailRadio").prop("checked")) {
            //选择的是userEmail 需要先得到用户租借的书的编码
            url = "/book/searchBook2.json?searchVal=";
            $.ajax({
                url: '/user/selectUserByEmail.json',
                data: {"email": $("#searchInput").val().replace(/\s+/g, "")},
                async: false,
                type: 'GET',
                success: function (result) {
                    if (result.ret) {
                        if (result.data != null && result.data != "") {
                            user = result.data;
                        } else {
                            spopTs("用户不存在");
                            throw SyntaxError();
                        }
                    } else {
                        spopFail("错误", result.msg);
                        throw SyntaxError();
                    }
                },
                error: function () {
                    spopFail("请求失败", "");
                    throw SyntaxError();
                }
            });

            $.ajax({
                url: '/bookLease/selectByUserId.json',
                data: {"userId": user.id},
                async: false,
                type: 'GET',
                success: function (result) {
                    if (result.ret) {
                        if (result.data != null && result.data != "") {
                            searchVal=result.data;
                        } else {
                            spopTs("无正在租借记录");
                            $("#searchBox").attr("class", "bigSearch");//改变搜索框css
                            $("#radio").addClass("bigRadio");
                            $("#radio").removeClass("smallRadio");
                            $("#body").css("display", "none");
                            throw SyntaxError();
                        }
                    } else {
                        spopFail("错误", result.msg);
                        throw SyntaxError();
                    }
                },
                error: function () {
                    spopFail("请求失败", "");
                    throw SyntaxError();
                }
            });

        }



        $("#searchBox").attr("class", "smallSearch");//改变搜索框css
        $("#radio").addClass("smallRadio");
        $("#body").css("display", "block");
        table.render({
            elem: '#searchTable'
            , url: url + searchVal //数据接口
            , id: 'test'
            , response: {
                statusCode: 0 //规定成功的状态码，默认：0
            }
            , cols: [
                [ //表头
                    {field: 'bookName', title: '书籍名称', fixed: 'left'}
                    , {field: 'authorName', title: '作者'}
                    , {field: 'price', title: '价格'}
                    , {field: 'pressName', title: '出版社'}
                    , {
                    field: 'bookTypeId', title: '书籍类型', templet: function (obj) {
                        var typeName = "没有此类型";
                        $.ajax({
                            url: '/bookType/select.json',
                            data: {"id": obj.bookTypeId},
                            async: false,
                            type: 'GET',
                            success: function (result) {
                                if (result.data != null) {
                                    typeName = result.data.typeName;
                                }
                            }
                        });
                        return typeName;
                    }
                }
                    , {
                    field: 'bookLeaseType', title: '书籍租借类型', templet: function (obj) {
                        var typeName = "无";
                        $.ajax({
                            url: '/bookLeaseType/select.json',
                            data: {"id": obj.bookLeaseType},
                            async: false,
                            type: 'GET',
                            success: function (result) {
                                if (result.data != null) {
                                    typeName = result.data.typeName;
                                }
                            }
                        });
                        return typeName;
                    }
                }
                    , {
                    field: 'bookChcoType', title: '收费方式', templet: function (obj) {
                        if (obj.bookChcoType == 0) {
                            return '免费'
                        } else {
                            return '收费'
                        }
                    }
                }
                    , {field: '', title: '书籍状态', toolbar: '#bookstatus'}
                ]
            ]
        });

        var leasePrice = 0;
        var discount = 1; //租借折扣
        table.on('tool(test)', function (obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
            var data = obj.data //获得当前行数据
                , layEvent = obj.event; //获得 lay-event 对应的值
            if(layEvent != 'borrow'){//除去租借以外需要拉去租借人信息
                //根据bookCode得到userid
                $.ajax({
                    url: '/bookLease/selectByBookIdOne2.json',
                    data: {"bookId": data.id},
                    async: false,
                    type: 'GET',
                    success: function (result) {
                        if (result.ret) {
                            if (result.data != null && result.data != "") {
                                user={
                                    "id":result.data
                                }
                            }
                        } else {
                            spopFail("错误",result.msg);
                            throw SyntaxError();
                        }
                    },
                    error: function () {
                        spopFail("获取数据失败", "");
                        throw SyntaxError();
                    }
                });
            }

            if (layEvent === 'borrow') {
                if (isCode == false) {
                    spopTs("请先验证身份");
                    return;
                }
                if (user.userScore < 60) {//友情提示用户积分低于60分
                    spopTs("该用户积分低于60");
                }

                //根据用户积分，判断是否可以租借
                $.ajax({
                    url: '/bookLeaseType/select.json',
                    data: {"id": data.bookLeaseType},
                    async: false,
                    type: 'GET',
                    success: function (result) {
                        if (result.ret) {
                            if (result.data != null && result.data != "") {
                                if (user.userScore < result.data.score) {
                                    spopFail("借阅该图书需要" + result.data.score + "积分</br>用户积分:" + user.userScore, "");
                                    return;
                                } else {
                                    discount = result.data.discount;
                                }
                            }
                        } else {
                            spopFail("获取数据失败", "");
                            return;
                        }
                    },
                    error: function () {
                        spopFail("获取数据失败", "");
                        return;
                    }
                });

                //是否收费
                if (data.bookChcoType == 1) {
                    //收费
                    //根据收费id得到收费价格
                    $.ajax({
                        url: '/bookCode/selectByPrice.json',
                        data: {"price": data.price},
                        async: false,
                        type: 'GET',
                        success: function (result) {
                            if (result.ret) {
                                if (result.data != null && result.data != "") {
                                    leasePrice = result.data.bookPrice;
                                }
                            } else {
                                spopFail("获取数据失败", "");
                                return;
                            }
                        },
                        error: function () {
                            spopFail("获取数据失败", "");
                            return;
                        }
                    });


                    //根据用户积分得到是否有折扣
                    $.ajax({
                        url: '/userType/selectUsertypeByScore.json',
                        data: {"score": user.userScore},
                        async: false,
                        type: 'GET',
                        success: function (result) {
                            if (result.ret) {
                                if (result.data != null && result.data != "") {
                                    if (result.data.discount < discount) {
                                        discount = result.data.discount;
                                    }
                                }
                            } else {
                                spopFail("获取数据失败", "");
                                return;
                            }
                        },
                        error: function () {
                            spopFail("获取数据失败", "");
                            return;
                        }
                    });
                } else {
                    leasePrice = 0;
                    discount = 1;
                }


                //满足借阅条件
                //加载数据
                var viewdata = { //数据
                    "bookId": data.id
                    , "userId": user.id
                    , "eamil": user.userEmail
                    , "bookName": data.bookName
                    , "bookCode": data.bookCode
                    , "bookAuthor": data.authorName
                    , "price": leasePrice
                    , "discount": discount
                }
                loadLease(viewdata);
            }

            if (layEvent === 'giveBack') {
                var giveBackLease;
                var pricrJs;
                $.ajax({
                    url: '/bookLease/selectByBookIdOne.json',
                    data: {"bookId": data.id},
                    async: false,
                    type: 'GET',
                    success: function (result) {
                        if (result.ret) {
                            if (result.data != null && result.data != "") {
                                giveBackLease = result.data;
                                pricrJs = result.msg;
                            } else {
                                spopFail("获取数据失败", "");
                                return;
                            }
                        } else {
                            spopFail("获取数据失败", "");
                            return;
                        }
                    },
                    error: function () {
                        spopFail("获取数据失败", "");
                        return;
                    }
                });

                //还书
                var viewdata = {
                    "bookId": giveBackLease.bookId
                    , "userId": user.id
                    , "leaseId": giveBackLease.id
                    , "bookName": data.bookName
                    , "bookCode": data.bookCode
                    , "bookAuthor": data.authorName
                    , "price": giveBackLease.price
                    , "priceJs": pricrJs
                    , "remark": giveBackLease.remark
                };
                loadgiveBackLease(viewdata);
            }
                //续租按钮点击事件
            if (layEvent === 'relet') {
                $.ajax({
                    url: '/mainBook/reletBook.json',
                    data: {"bookId": data.id},
                    async: false,
                    type: 'POST',
                    success: function (result) {
                        if (result.ret) {
                            spopSucess("续租操作成功")
                        } else {
                            spopFail("错误", result.msg);
                        }
                    },
                    error: function () {
                        spopFail("获取数据失败", "");
                    }
                });
            }
        });

    });
    //归还书籍订单
    loadgiveBackLease = function (viewdata) {
        var getTpl = bookLease2.innerHTML
            , view = document.getElementById('view');
        laytpl(getTpl).render(viewdata, function (html) {
            view.innerHTML = html;
        });
        //var leaseBookid = viewdata.bookId;
        layer.open({
            title: '确认订单',
            type: 1,
            content: $("#view"),
            shade: 0.5,
            area: '400px',
            success: function () {
                //还书
                $('#submitBookLease2').on("click", function () {
                    $.ajax({
                        url: '/mainBook/returnBook.json',
                        data: $("#bookLeaseFrom2").serializeArray(),
                        //async: false,
                        type: 'POST',
                        success: function (result) {
                            if (result.ret) {
                                layer.close(layer.index);
                                $("#view").html("");
                                $('#searchBut').click();
                            } else {
                                spopFail("", result.msg);
                            }
                        },
                        error: function () {
                            spopFail("请求失败", "");
                        }
                    });
                });

                //输入其他收费事件
                $('#otherPrice').on("input propertychange", function () {
                    var price1 = $("#price1").val();
                    var otherPrice = $("#otherPrice").val();
                    var price2 = Number(price1) + Number(otherPrice);
                    if (price2 < price1) {
                        price2 = price1;
                    }
                    $("#price2").val(price2);
                });

            },
            cancel: function () {
                $("#view").html("");
            }
        });
    };

    //生成订单表
    loadLease = function (viewdata) {
        var getTpl = bookLease.innerHTML
            , view = document.getElementById('view');
        laytpl(getTpl).render(viewdata, function (html) {
            view.innerHTML = html;
        });

        layer.open({
            title: '确认订单',
            type: 1,
            content: $("#view"),
            shade: 0.5,
            area: '400px',
            success: function () {
                //借书
                $('#submitBookLease').on("click", function () {
                    $.ajax({
                        url: '/mainBook/borrowBook.json',
                        data: $("#bookLeaseFrom").serializeArray(),
                        //async: false,
                        type: 'POST',
                        success: function (result) {
                            if (result.ret) {
                                layer.close(layer.index);
                                $("#view").html("");
                                $('#searchBut').click();
                            } else {
                                spopFail("请求失败", "");
                            }
                        },
                        error: function () {
                            spopFail("请求失败", "");
                        }
                    });

                });

            },
            cancel: function () {
                $("#view").html("");
            }
        });

    }


    form.render();


    //点击获取验证码
    var verificationCode = "";
    var isCode = false;
    var djs = 30;
    var djsdsq;
    var user = null;
    $('#getCodeBut').on("click", function () {
        if ($('#getCodeBut').attr("class") != "layui-btn") {
            return;
        }
        var userEmailInput = $("#userEmailInput").val();
        if (userEmailInput == "") {
            return;
        }
        $('#getCodeBut').attr("class", "layui-btn layui-btn-disabled");
        //通过ajax得到验证码
        getCode(userEmailInput);
        djs = 30;

        djsdsq = setInterval(function () {
            $("#getCodeBut").text(djs + "秒后重新获取");
            djs--;
            if (djs < 0) {
                $("#getCodeBut").text("重新获取");
                $('#getCodeBut').attr("class", "layui-btn");
                clearInterval(djsdsq);
            }
        }, 1000);

    });

    //输入图书编码的事件 需重新验证读者身份
    $('#searchInput').on("input propertychange", function () {
        reset();
        $("#userEmailInput").val("");
        $("#codeInput").val("");
        var searchInputLength = $('#searchInput').val().length;
        var searchInputwidth = $('#searchInput').css("width");
        var iaaaaa = 0;
        //searchInput
    });

    //输入验证码的事件
    $('#codeInput').on("input propertychange", function () {
        var inputCode = $("#codeInput").val();
        if (inputCode.length == 6 && !isCode && verificationCode != "") {
            if (inputCode == verificationCode) {
                done();
            } else {
                spopTs("验证码错误", "");
            }
        }
    });

    //输入邮箱事件
    $('#searchInput').on("input propertychange", function () {
        reset();
    });

    //重置按钮
    function reset() {
        $("#getCodeBut").text("获取验证码");
        $('#getCodeBut').attr("class", "layui-btn layui-btn-disabled");
        djs = 30;
        isCode = false;
        verificationCode = "";
        $('#codeInput').val("");
        clearInterval(djsdsq);
    }

    //可验证
    function userYz() {
        $("#getCodeBut").text("获取验证码");
        $('#getCodeBut').attr("class", "layui-btn");
        djs = 30;
        clearInterval(djsdsq);
    }

    //验证完成
    function done() {
        $("#getCodeBut").text("验证成功");
        isCode = true;
        $('#getCodeBut').attr("class", "layui-btn layui-btn-normal");
        clearInterval(djsdsq);
    }

    //回车事件
    $('#userEmailInput').keyup(function (event) {
        if (event.keyCode == 13) {
            $('#searchEmailBut').click();
        }
    });

    //点击搜索用户
    $('#searchEmailBut').on("click", function () {
        //需要得到该用户信息
        var inputCode = $("#userEmailInput").val();
        if (inputCode == "") {
            return;
        }
        $.ajax({
            url: '/user/selectUserByEmail.json',
            data: {"email": inputCode},
            async: false,
            type: 'GET',
            success: function (result) {
                if (result.ret) {
                    if (result.data != null && result.data != "") {
                        user = result.data;
                        spopSucess("用户已登记,正在发送验证码");
                        userYz();
                    } else {
                        spopFail("用户不存在", "");
                    }
                } else {
                    spopFail("请求失败", "");
                }
            },
            error: function () {
                spopFail("请求失败", "");
            }
        });
        $("#getCodeBut").click();
        $("codeInput").focus();
    });

    //获得验证码
    function getCode(inputCode) {
        $.ajax({
            url: '/book/getCode.json',
            data: {"email": inputCode},
            type: 'GET',
            success: function (result) {
                if (result.ret) {
                    spopSucess("验证码已发送");
                    verificationCode = result.data;
                } else {
                    spopFail("", result.msg);
                    userYz();
                }
            },
            error: function () {
                spopFail("获取失败", "");
                userYz();
            }
        });
    }


});