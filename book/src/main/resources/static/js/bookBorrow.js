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

    //回车事件
    $('#searchInput').keyup(function(event){
        if(event.keyCode ==13){
            $('#searchBut').click();
        }
    });

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
                    {field: 'bookName', title: '书籍名称' ,fixed: 'left'}
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

        var leasePrice=0;
        var discount=1; //租借折扣
        table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
            var data = obj.data //获得当前行数据
                ,layEvent = obj.event; //获得 lay-event 对应的值
            if(layEvent === 'borrow'){
                if(isCode==false){
                    spopTs("请先验证身份");
                    return;
                }
                if(user.userScore<60){//友情提示用户积分低于60分
                    spopTs("该用户积分低于60");
                }

                //根据用户积分，判断是否可以租借
                $.ajax({
                    url: '/bookLeaseType/select.json',
                    data: {"id":data.bookLeaseType},
                    async:false,
                    type: 'GET',
                    success: function (result) {
                        if(result.ret){
                            if(result!=null && result!=""){
                                if(user.userScore < result.data.score){
                                    spopFail("借阅该图书需要"+result.data.score+"积分</br>用户积分:"+user.userScore,"");
                                    return;
                                }else{
                                    discount=result.data.discount;
                                }
                            }
                        }else{
                            spopFail("获取数据失败","");
                            return;
                        }
                    },
                    error:function () {
                        spopFail("获取数据失败","");
                        return;
                    }
                });

                //是否收费
                if(data.bookChcoType==1){
                    //收费
                    //根据收费id得到收费价格
                    $.ajax({
                        url: '/bookCode/selectByPrice.json',
                        data: {"price":data.price},
                        async:false,
                        type: 'GET',
                        success: function (result) {
                            if(result.ret){
                                if(result!=null && result!=""){
                                    leasePrice=result.data.bookPrice;
                                }
                            }else{
                                spopFail("获取数据失败","");
                                return;
                            }
                        },
                        error:function () {
                            spopFail("获取数据失败","");
                            return;
                        }
                    });


                    //根据用户积分得到是否有折扣
                    $.ajax({
                        url: '/userType/selectUsertypeByScore.json',
                        data: {"score":user.userScore},
                        async:false,
                        type: 'GET',
                        success: function (result) {
                            if(result.ret){
                                if(result!=null && result!=""){
                                    if(result.data.discount< discount){
                                        discount=result.data.discount;
                                    }
                                }
                            }else{
                                spopFail("获取数据失败","");
                                return;
                            }
                        },
                        error:function () {
                            spopFail("获取数据失败","");
                            return;
                        }
                    });
                }else{
                    leasePrice=0;
                    discount=1;
                }



                //满足借阅条件
                //加载数据
                var viewdata = { //数据
                    "bookId":data.id
                    ,"userId":user.id
                    ,"eamil":user.userEmail
                    ,"bookName":data.bookName
                    ,"bookCode":data.bookCode
                    ,"bookAuthor":data.authorName
                    ,"price":leasePrice
                    ,"discount":discount
                }
                loadLease(viewdata);
            }
        });

    });

    //生成订单表
    loadLease= function (viewdata){
        var getTpl = bookLease.innerHTML
            ,view = document.getElementById('view');
        laytpl(getTpl).render(viewdata, function(html){
            view.innerHTML = html;
        });

        layer.open({
            title: '确认订单',
            type: 1,
            content: $("#view"),
            shade: 0.5,
            area: '400px',
            success:function(){
                $('#submitBookLease').on("click",function(){
                    var leaseId=0;//返回的订单id
                    $.ajax({
                        url: '/bookLease/save.json',
                        data:$("#bookLeaseFrom").serializeArray(),
                        async:false,
                        type: 'POST',
                        success: function (result) {
                            if(result.ret){
                                layer.close(layer.index);
                                leaseId=result.data;
                                //$('#searchBut').click();
                            }else{
                                spopFail("请求失败","");
                            }
                        },
                        error:function () {
                            spopFail("请求失败","");
                        }
                    });
                    if(leaseId!=0){
                        //新增记录成功
                        //改变书籍状态
                        $.ajax({
                            url: '/book/updateLeaseId.json',
                            data:{"bookId":viewdata.bookId,"leaseId":leaseId},
                            async:false,
                            type: 'POST',
                            success: function (result) {
                                if(result.ret){
                                    layer.close(layer.index);
                                    $("#view").html("");
                                    $('#searchBut').click();
                                }else{
                                    spopFail("请求失败","");
                                }
                            },
                            error:function () {
                                spopFail("请求失败","");
                            }
                        });

                    }


                });

            },
            cancel: function(){
                $("#view").html("");
            }
        });
    }


    form.render();


    //点击获取验证码
    var verificationCode="";
    var isCode=false;
    var djs=30;
    var djsdsq;
    var user=null;
    $('#getCodeBut').on("click",function(){
        if($('#getCodeBut').attr("class")!="layui-btn"){
            return;
        }
        var userEmailInput=$("#userEmailInput").val();
        if(userEmailInput==""){
            return;
        }
        $('#getCodeBut').attr("class","layui-btn layui-btn-disabled");
        //通过ajax得到验证码
        getCode(userEmailInput);


        djsdsq=setInterval(function() {
            $("#getCodeBut").text(djs+"秒后重新获取");
            djs--;
            if(djs<0){
                $("#getCodeBut").text("重新获取");
                $('#getCodeBut').attr("class","layui-btn");
            }
        },1000);

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

    //回车事件
    $('#userEmailInput').keyup(function(event){
        if(event.keyCode ==13){
            $('#searchEmailBut').click();
        }
    });

    //点击搜索用户
    $('#searchEmailBut').on("click",function(){
        //需要得到该用户信息
        var inputCode=$("#userEmailInput").val();
        if(inputCode==""){
            return;
        }
        $.ajax({
            url: '/user/selectUserByEmail.json',
            data: {"email":inputCode},
            async:false,
            type: 'GET',
            success: function (result) {
                if(result.ret){
                    if(result.data!=null && result.data!=""){
                        user=result.data;
                        spopSucess("用户已登记");
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
        $.ajax({
            url: '/book/getCode.json',
            data: {"email":inputCode},
            type: 'GET',
            success: function (result) {
                if(result.ret){
                    spopSucess("验证码已发送");
                    verificationCode=result.data;
                }else{
                    spopFail("获取失败","");
                }
            },
            error:function () {
                spopFail("获取失败","");
            }
        });
    }


});