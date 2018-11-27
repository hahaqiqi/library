layui.use(['form', 'laydate', 'layer', 'table', 'slider', 'laytpl','jquery'], function() {
    var form = layui.form //表单
        , laydate = layui.laydate //
        , layer = layui.layer //弹层
        , table = layui.table //表格
        , laytpl = layui.laytpl //模板
        , slider = layui.slider
        , $ = layui.$//jquery
        laydate.render({
            elem: '#date' //指定元素
        });

    $('#yzmButton').on("click",function () {
        var p= $("#userPhone").val();
        $.ajax({
            url: '/user/yzPhone.json',
            data: {"userPhone":p},
            type: 'POST',
            success: function (result) {
                if (result.ret) {
                    spopSucess("")
                } else {
                    spopFail("添加失败", result.msg);
                }
            },
            error:function () {
                alert("cc");
            }
        });
        
    });

    $('#add').on("click",function(){
        $.ajax({
            url: '/user/save.json',
            data: $("#saveForm").serializeArray(),
            type: 'POST',
            success: function (result) {
                if (result.ret) {
                    spopSucess("添加成功")
                } else {
                    spopFail("添加失败", result.msg);
                }
            },
            error:function () {
                alert("cc");
            }
        });
    });

});