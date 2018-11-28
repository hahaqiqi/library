layui.use(['form', 'laydate', 'layer', 'table', 'slider', 'laytpl','jquery'], function() {
    var form = layui.form //表单
        , laydate = layui.laydate //
        , layer = layui.layer //弹层
        , table = layui.table //表格
        , laytpl = layui.laytpl //模板
        , slider = layui.slider
        , $ = layui.$//jquery


    $('#phone').on("click",function () {
        var p= $("#Phone").val();
        var $code="";
        $.ajax({
            url: '/user/yzPhone.json',
            data: {"userPhone":p},
            type: 'POST',
            success: function (result) {
                alert(result);
                $code=result;
            },
            error:function () {
                alert("cc");
            }
        });

    });



});