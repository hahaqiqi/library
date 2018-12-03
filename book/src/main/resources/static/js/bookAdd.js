var submit = true;
layui.use(['form', 'jquery'], function () {
    var form = layui.form //表单
        , $ = layui.$;//jquery
    //得到所有书籍类型
    $.ajax({
        url: '/bookType/listAll.json',
        async: false,
        type: 'GET',
        success: function (result) {
            if (result.ret == true) {
                for (var i = 0; i < result.data.length; i++) {
                    $("#bookType").append("<option value=" + result.data[i].id + ">" + result.data[i].typeName + "</option>");
                }
            } else {
                $("#bookType").append("<option value='null'>加载失败</option>");
            }
        },
        error: function () {
            $("#bookType").append("<option value='null'>接口异常</option>");
        }
    });

    form.render();
    $("#loading").addClass("static");
    $('#submitBook').on("click", function () {
        if (submit) {
            disableButton();
            $.ajax({
                url: '/book/save.json',
                data: $("#bookForm").serializeArray(),
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        spopSucess("添加成功")
                        //清空
                        $("#bookForm")[0].reset();
                    } else {
                        spopFail("添加失败", result.msg);
                    }
                    enableButton();

                },
                error: function () {
                    spopFail("添加失败,请检查你输入的数据是否合法", "");
                    enableButton();
                }
            });
        }
    });

    function disableButton() {
        submit = false;
        $("#submitBook").attr("class", "layui-btn layui-btn-disabled");
        $("#loading").removeClass("static");
        $("#loading").addClass("loading");
    }

    function enableButton() {
        submit = true;
        $("#submitBook").attr("class", "layui-btn");
        $("#loading").removeClass("loading");
        $("#loading").addClass("static");
    }

    //选择文件
    $('#submitBookExcel').on("click", function () {
        $("#bookExcelFile").click();
    });
    $('input[name=backImageFile]').change(function() {
        var formData = new FormData($("#bookExcelFrom")[0]);
        $.ajax({
            url: '/book/getBookExcel.json',
            type: 'post',
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                if (result.ret == false) {
                    spopFail("加载失败",result.msg);
                    return;
                }
                spopSucess("...");
                $("#bookExcelFile").val("");
                return;
            },
            error:function () {
                $("#bookExcelFile").val("");
            }
        });
    });

});