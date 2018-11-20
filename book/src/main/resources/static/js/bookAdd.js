var submit=true;
layui.use(['form','jquery'], function(){
    var form = layui.form //表单
        ,$=layui.$;//jquery
    form.render();
    $("#loading").addClass("static");
    $('#submitBook').on("click",function(){
        if(submit) {
            disableButton();
            $.ajax({
                url: '/book/save.json',
                data: $("#bookForm").serializeArray(),
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        spopSucess("添加成功")
                    } else {
                        spopFail("添加失败", result.msg);
                    }
                    enableButton();
                    //清空
                    $("#bookForm")[0].reset();
                },
                error: function () {
                    spopFail("添加失败,请检查你输入的数据是否合法", "");
                    enableButton();
                }
            });
        }
    });
    function disableButton() {
        submit=false;
        $("#submitBook").attr("class","layui-btn layui-btn-disabled");
        $("#loading").removeClass("static");
        $("#loading").addClass("loading");
    }
    function enableButton() {
        submit=true;
        $("#submitBook").attr("class","layui-btn");
        $("#loading").removeClass("loading");
        $("#loading").addClass("static");
    }
    //ewpowjjjyuglechd
});