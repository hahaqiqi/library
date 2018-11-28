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


layui.config({
    version: '1541881042991' //为了更新 js 缓存，可忽略
});

layui.use(['form', 'laypage', 'layer', 'table', 'slider', 'laytpl','jquery'], function(){
    var form = layui.form //表单
        ,laypage = layui.laypage //分页
        ,layer = layui.layer //弹层
        ,table = layui.table //表格
        ,laytpl = layui.laytpl //模板
        ,slider = layui.slider
        ,$=layui.$//jquery

    form.render();


    //点击搜索图书
    $('#searchBut').on("click",function(){
        var searchVal=$("#searchInput").val();
        table.render({
            elem: '#searchTable'
            ,url: '/book/searchBook.json?searchVal='+searchVal //数据接口
            ,id:'test'
            ,response: {
                statusCode: 0 //规定成功的状态码，默认：0
            }
            ,cols: [
                [ //表头
                    {type: 'checkbox', fixed: 'left'}
                    ,{field: 'bookName', title: '书籍名称'}
                    ,{field: 'authorName', title: '作者'}
                    ,{field: 'price', title: '价格'}
                    ,{field: 'pressName', title: '出版社'}
                    ,{field: 'bookTypeId', title: '书籍类型',templet:function(obj){
                        var typeName="没有此类型";
                        $.ajax({
                            url: '/bookType/select.json',
                            data: {"id": obj.bookTypeId},
                            async: false,
                            type: 'GET',
                            success: function (result) {
                                if(result.data!=null){
                                    typeName= result.data.typeName;
                                }
                            }
                        });
                        return typeName;
                    }
                }
                    ,{field: 'bookLeaseType', title: '书籍租借类型',templet:function(obj){
                        var typeName="无";
                        $.ajax({
                            url: '/bookLeaseType/select.json',
                            data: {"id": obj.bookLeaseType},
                            async: false,
                            type: 'GET',
                            success: function (result) {
                                if(result.data!=null){
                                    typeName= result.data.typeName;
                                }
                            }
                        });
                        return typeName;
                    }
                }
                    ,{field: 'bookChcoType', title: '收费方式',templet:function(obj){if(obj.bookChcoType==0){return '免费'}else{ return '收费' } }}
                    ,{field: '', title: '书籍状态',toolbar:'#bookstatus'}
                ]
            ]
        });


        table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
            var data = obj.data //获得当前行数据
                ,layEvent = obj.event; //获得 lay-event 对应的值
            if(layEvent === 'borrow'){

            }
        });

    });


    form.render();


    //点击获取验证码
    var verificationCode="";
    var isCode=false;
    var djs=30;
    var djsdsq;
    $('#getCodeBut').on("click",function(){
        if($('#getCodeBut').attr("class")!="layui-btn"){
            return;
        }
        var userEmailInput=$("#userEmailInput").val();
        if(userEmailInput==""){
            return;
        }
        //通过ajax得到验证码
        verificationCode=getCode(userEmailInput);


        $('#getCodeBut').attr("class","layui-btn layui-btn-disabled");

        if(verificationCode!=""){//获取成功
            djsdsq=setInterval(function() {
                $("#getCodeBut").text(djs+"秒后重新获取");
                djs--;
                if(djs<0){
                    $("#getCodeBut").text("重新获取");
                    $('#getCodeBut').attr("class","layui-btn");
                }
            },1000);
        }
    });

    //输入图书编码的事件 需重新验证读者身份
    $('#searchInput').on("input propertychange",function(){
        reset();
        $("#userEmailInput").val("");
        $("#codeInput").val("");
        var searchInputLength=$('#searchInput').val().length;
        var searchInputwidth=$('#searchInput').css("width");
        var iaaaaa=0;
        //searchInput
    });

    //输入验证码的事件
    $('#codeInput').on("input propertychange",function(){
        var inputCode=$("#codeInput").val();
        if(inputCode.length==6 && !isCode && verificationCode!=""){
            if(inputCode==verificationCode){
                done();
            }else{
                spopTs("验证码错误","");
            }
        }
    });

    //输入邮箱事件
    $('#searchInput').on("input propertychange",function(){
        reset();
    });

    //重置按钮
    function reset() {
        $("#getCodeBut").text("获取验证码");
        $('#getCodeBut').attr("class","layui-btn layui-btn-disabled");
        djs=30;
        isCode=false;
        verificationCode="";
        $('#codeInput').val("");
        clearInterval(djsdsq);
    }
    //可验证
    function userYz() {
        $("#getCodeBut").text("获取验证码");
        $('#getCodeBut').attr("class","layui-btn");
    }
    //验证完成
    function done() {
        $("#getCodeBut").text("验证成功");
        isCode=true;
        $('#getCodeBut').attr("class","layui-btn layui-btn-normal");
        clearInterval(djsdsq);
    }

    //点击搜索用户
    $('#searchEmailBut').on("click",function(){
        //需要得到该用户信息
        var inputCode=$("#userEmailInput").val();
        $.ajax({
            url: '/user/selectUserByEmail.json',
            data: {"email":inputCode},
            async:false,
            type: 'GET',
            success: function (result) {
                if(result.ret){
                    if(result.data!=null && result.data!=""){
                        spopSucess("用户存在");
                        userYz();
                    }else{
                        spopFail("用户不存在","");
                    }
                }else{
                    spopFail("请求失败","");
                }
            },
            error:function () {
                spopFail("请求失败","");
            }
        });

    });

    //获得验证码
    function getCode(inputCode) {
        var code="";
        $.ajax({
            url: '/book/getCode.json',
            data: {"email":inputCode},
            async:false,
            type: 'GET',
            success: function (result) {
                if(result.ret){
                    spopSucess("验证码已发送");
                    code=result.data;
                }else{
                    spopFail("获取失败","");
                }
            },
            error:function () {
                spopFail("获取失败","");
            }
        });
        return code;
    }

});