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

    var yzm=null;
    var yzSubmit=false;
    var djs=10;
    var djsdsq;
    $('#getYzm').on("click",function () {
        if($('#getYzm').attr("class")!="layui-btn"){//点击按钮后不执行后续代码
            return;
        }
        var userEmail= $("#userEmail").val();//得到邮箱
        $('#getYzm').attr("class","layui-btn layui-btn-disabled");//禁用按钮
        $.ajax({
            url: '/user/yzEmail.json',
            data: {"userEmail":userEmail},
            type: 'POST',
            async:false,
            success: function (result) {
                if (result.ret) {
                    yzm=result.data
                    spopSucess("验证码已发送")
                } else {
                    spopFail("发送失败", result.msg);
                }
            },
            error:function () {
                spopFail("验证码获取失败");
            }
        });
            djsdsq=setInterval(function() {
                $("#getYzm").text(djs+"秒后重新获取");
                djs--;
                if(djs<0){
                    reset();
                }
            },1000);

    });


    $('#yzmButton').on("click",function () {
        var yzEmail=$("#yzEmail").val();
        if(yzEmail==yzm && yzm!=null){
            yzSubmit=true
            spopSucess("验证成功")
            $('#userEmail').attr("disabled","true");//验证成功后邮箱不可更改
        }else{
            spopFail("验证失败");
        }
    });

    $('#add').on("click",function(){
        if(yzSubmit) {
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
                error: function () {
                    spopFail("日期格式不正确");
                }
            });
        }else{
            spopFail("请先进行验证邮箱");
        }
    });

    //重置按钮
    function reset() {
        $("#getYzm").text("获取验证码");
        $('#getYzm').attr("class","layui-btn");
        djs=10;
        clearInterval(djsdsq);
    }

});