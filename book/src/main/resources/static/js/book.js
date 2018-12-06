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
function getBookType(bid) {
    var typeName="没有此类型";
    layui.use('jquery', function() {
        var $=layui.$;
        $.ajax({
            url: '/bookType/select.json',
            data: {"id": bid.bookTypeId},
            async: false,
            type: 'GET',
            success: function (result) {
                if(result.data!=null){
                    typeName= result.data.typeName;
                }
            }
        });
    });
    return typeName;
}

var pageOldMax;
var sliderPage;
var pageMax;
function pageCh(res, curr, count) {
    /*layui.use(['slider','jquery'], function() {
        var slider = layui.slider //滑块
        ,$=layui.$                  //jquery
        pageMax=Math.ceil(count/res.limit);
        if(pageOldMax!=pageMax) {
            pageOldMax=pageMax;
            sliderPage=slider.render({
                elem: '#slidePage'  //绑定元素
                , min: 0
                , max: pageMax
                , value: curr
                ,disabled:true
                , change: function (value) {
                    if(value==0){
                        return;
                    }
                    if(value!=$('.layui-input').val()) {
                        $('.layui-input').val(value);
                        $('.layui-laypage-btn').click();
                    }
                }
            });
        }else{
            sliderPage.setValue(curr);
        }
    });*/
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

    //得到所有书籍类型
    $.ajax({
        url: '/bookType/listAll.json',
        async: false,
        type: 'GET',
        success: function (result) {
            if(result.ret==true){
                for(var i=0;i<result.data.length;i++){
                    $(".bookType").append("<option value="+result.data[i].id+">"+result.data[i].typeName+"</option>");
                }
            }else{
                $(".bookType").append("<option value='null'>加载失败</option>");
            }
        },
        error:function () {
            $(".bookType").append("<option value='null'>接口异常</option>");
        }
    });
    //得到所有租借规则
    $.ajax({
        url: '/bookLeaseType/listAll.json',
        async: false,
        type: 'GET',
        success: function (result) {
            if(result.ret==true){
                if(result.data.length<=0){
                    $("#updateBookLeaseType").append("<option value=''>无</option>");
                }else {
                    for (var i = 0; i < result.data.length; i++) {
                        $("#updateBookLeaseType").append("<option value=" + result.data[i].id + ">" + result.data[i].typeName + "</option>");
                    }
                }
            }else{
                $("#updateBookLeaseType").append("<option value='null'>加载失败</option>");
            }
        },
        error:function () {
            $(".bookType").append("<option value='null'>接口异常</option>");
        }
    });



    form.render();

    //向世界问个好
    //layer.msg('Hello World');
    getFormData = function (elem)
    {
        var fieldElem = $(elem).find('input,select,textarea'); //获取所有表单域
        var field ={};
        layui.each(fieldElem, function(_, item){
            if(!item.name) return;
            if(/^checkbox|radio$/.test(item.type) && !item.checked) return;
            field[item.name] = item.value;
        });
        return field;
    };
    //执行一个 table 实例(加载数据)
    //获取查询表单的参数
    var formData = getFormData("#filtrateFrom");
    tableSx=function() {
        table.render({
            elem: '#bbbbb'
            , url: "/book/list.json"
            , id: 'test'
            , page: true
            , toolbar: '#headTemplate'
            , response: {
                statusCode: 0 //规定成功的状态码，默认：0
            }
            , cols: [
                [ //表头
                    {type: 'checkbox', width: 40},
                    , {field: 'id', width: 60, hide: true, sort: true, title: ""}
                    , {field: 'bookName', sort: true}
                    , {field: 'bookCode', hide: true, sort: true}
                    , {field: 'authorName', sort: true}
                    , {field: 'price', sort: true}
                    , {field: 'pressName', sort: true}
                    , {
                    field: 'bookTypeId', sort: true, templet: function (bookTypeId) {
                        return getBookType(bookTypeId)
                    }
                }
                    , {
                    field: 'bookLeaseId', hide: true, templet: function (obj) {
                        if (obj.bookLeaseId == 0 || obj.bookLeaseId == null) {
                            return '未被租借'
                        } else {
                            return '租借中'
                        }
                    }
                }
                    , {field: 'bookLeaseType', hide: true, sort: true}
                    , {
                    field: 'bookChcoType', sort: true, templet: function (obj) {
                        if (obj.bookChcoType == 0) {
                            return '免费'
                        } else {
                            return '收费'
                        }
                    }
                }
                    , {field: 'status', toolbar: '#bookstatus', align: 'center'}
                    , {field: 'bookSpaceId', sort: true, toolbar: '#bookSpace', hide: true, align: 'center'}
                    , {field: 'operator', hide: true, sort: true}
                    , {
                    field: 'operateTime', sort: true, hide: true, align: 'center', templet: function (operateTime) {
                        return createTime(operateTime)
                    }
                }
                    , {field: 'remark'}
                    , {fixed: 'right', toolbar: '#barDemo', width: 175, align: 'center'}
                ]
            ],
            where: formData
            , done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                console.log(res);
                //得到当前页码
                console.log(curr);
                //得到数据总量
                console.log(count);
            }
        });
    };
    tableSx();
    var editObj;
    //监听头工具栏事件
    table.on('toolbar(test)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id)
            ,data = checkStatus.data; //获取选中的数据
        switch(obj.event){
           /* case 'add':
                var viewdata = { //数据
                    "bookTypeName":""
                    ,"remark":""
                    ,"submitType":1
                }
                showBookTypeInfo("新增",viewdata);
                break;*/
            case 'update':
                break;
            case 'delete':
                if(data.length === 0){
                    layer.msg('请选择一行');
                } else {
                    //批量删除
                    // checkStatus.data[c].id 为要删除的id
                    layer.confirm('确认删除所选中的 '+data.length+" 条数据吗?", function(index){
                        layer.close(index);
                        var batchStrId="";
                        for(var c=0;c<data.length;c++){
                            //layer.alert('删除'+checkStatus.data[c].id);
                            if(c==0){
                                batchStrId=checkStatus.data[c].id;
                                continue;
                            }
                            batchStrId=batchStrId+","+checkStatus.data[c].id;
                        }
                        var delSuccessCount=batchDeleteBookType(batchStrId);
                        if(delSuccessCount>0) {
                            var fk = "删除数据" + delSuccessCount + "/" + data.length + "成功</br>删除数据" + (data.length - delSuccessCount) + "/" + data.length + "失败";
                            if(data.length - delSuccessCount>0){
                                fk=fk+"</br>书籍可能在被租借中,无法删除";
                            }
                            spopBatchHint(fk);
                        }else{
                            spopFail("操作错误","");
                        }
                        $(".layui-laypage-btn")[0].click();
                    });

                }
                break;
            case 'refresh': //刷新
                $('#refiltrateSubmitBookType').click();
                break;
            case 'filtrate':
                $("#filtrate").css("display", "block");
                layer.open({
                    type: 1,
                    title:'高级搜索和修改',
                    shade :0.4,
                    closeBtn:0,
                    shadeClose:true,
                    content: $("#filtrate"),
                    area: '900px',
                    cancel:function(){

                    },
                    end:function () {
                        $("#filtrate").css("display","none");
                    }
                });



                /*if($("#filtrate").css("display")=="block"){
                    $("#filtrate").css("display","none");
                }else {
                    $("#filtrate").css("display", "block");
                }*/
                form.render();
                break;

        };
    });


    //提交筛选
    $('#filtrateSubmitBookType').on("click",function(){
        formData = getFormData("#filtrateFrom");
        tableSx();
        layer.close(layer.index);
    });

    //清空条件
    $('#refiltrateSubmitBookType').on("click",function(){
        $("#filtrateFrom")[0].reset();
        $("#updateFiltrateFrom")[0].reset();
        tableSx();
        layer.close(layer.index);
    });
    //更改筛选
    $('#updateFiltrateSubmitBookType').on("click",function(){
        $.ajax({
            url: '/book/batchUpdate.json',
            data: $("#updateFiltrateFrom").serializeArray(),
            type: 'POST',
            success: function (result) {
                if (result.ret) {
                    spopSucess("修改成功");
                    layer.close(layer.index);
                    $(".layui-laypage-btn")[0].click();
                } else {
                    spopFail("错误",result.msg);
                }
            },
            error:function () {
                spopFail("错误","请检查参数");
            }
        });
    });

    //监听行工具事件
    table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
            ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'detail'){
            var viewdata = { //数据
                "bookName":data.bookName
                ,"bookCode":data.bookCode
                ,"authorName":data.authorName
                ,"price":data.price
                ,"pressName":data.pressName
                ,"bookTypeId":data.bookTypeId
                ,"bookChcoType":data.bookChcoType
                ,"bookSpaceId":data.bookSpaceId
                ,"remark":data.remark
                ,"submitType":0
            }
            showBookTypeInfo("查看",viewdata);
        } else if(layEvent === 'del'){
            layer.confirm('确认删除这一条数据?', function(index){
                layer.close(index);
                //layer.msg("删除"+data.id);
                //向服务端发送删除指令
                deleteBookType(data.id);
            });
        } else if(layEvent === 'edit'){
            editObj=data;
            var viewdata = { //数据
                "bookName":data.bookName
                ,"bookCode":data.bookCode
                ,"authorName":data.authorName
                ,"price":data.price
                ,"pressName":data.pressName
                ,"bookTypeId":data.bookTypeId
                ,"bookChcoType":data.bookChcoType
                ,"bookSpaceId":data.bookSpaceId
                ,"remark":data.remark
                ,"id":data.id
                ,"submitType":1
            }
            showBookTypeInfo("编辑",viewdata);
        }else if(layEvent === 'browse'){//查看书籍存放地点
            alert("browse");
        }else if(layEvent === 'changeStatus'){//下架书籍或恢复书籍
            $.ajax({
                url: '/book/changeBookStatus.json',
                data: {"id": data.id,"statusId":data.status==0?1:0},
                type: 'GET',
                success: function (result) {
                    if (result.ret) {
                        $(".layui-laypage-btn")[0].click();
                    } else {
                        spopFail("修改失败", result.msg);
                    }
                },
                error: function () {
                    spopFail("修改失败", "未知错误");
                }
            });
        }

    });

    //分页
    laypage.render({

    });

    function showBookTypeInfo(showTitleType,viewdata) {
        var getTpl = demo.innerHTML
            ,view = document.getElementById('view');
        laytpl(getTpl).render(viewdata, function(html){
            view.innerHTML = html;
        });
        layer.open({
            title: showTitleType+'图书',
            type: 1,
            content: $("#view"),
            shade: 0,
            area: '380px',
            success:function(){
                //监听提交数据
                $('#submitBookType').on("click",function(){
                    $.ajax({
                        url: '/book/update.json',
                        data: $("#bookForm").serializeArray(),
                        type: 'POST',
                        success: function (result) {
                            if (result.ret) {
                                spopSucess("修改成功");
                                layer.close(layer.index);
                                $(".layui-laypage-btn")[0].click();
                                $("#view").html("");
                            } else {
                                spopFail("修改失败",result.msg);
                            }
                        },
                        error:function () {
                            spopFail("修改失败","请检查参数");
                        }
                    });
                });
            },
            cancel: function(){
                $("#view").html("");
            }
        });
        //得到所有书籍类型
        $.ajax({
            url: '/bookType/listAll.json',
            async: false,
            type: 'GET',
            success: function (result) {
                if(result.ret==true){
                    for(var i=0;i<result.data.length;i++){
                        if(viewdata.bookTypeId==result.data[i].id){
                            $("#bookType").append("<option value="+result.data[i].id+" selected>"+result.data[i].typeName+"</option>");
                            continue;
                        }
                        $("#bookType").append("<option value="+result.data[i].id+">"+result.data[i].typeName+"</option>");
                    }
                }else{
                    $("#bookType").append("<option value='null'>加载失败</option>");
                }
            },
            error:function () {
                $("#bookType").append("<option value='null'>接口异常</option>");
            }
    });
        form.render();
    }

    //删除单条数据
    function deleteBookType(delid) {
        $.ajax({
            url: '/book/delete.json',
            data: {"id":delid},
            async:false,
            type: 'GET',
            success: function (result) {
                if(result.ret){
                    spopSucess("删除成功");
                }else{
                    spopFail("删除失败",result.msg);
                }
            }
        });
    }

    //批量删除数据
    function batchDeleteBookType(batchStrId) {
        var delSuccessCount=0;
        $.ajax({
            url: '/book/batchDelete.json',
            data: {"batchStrId":batchStrId},
            async:false,
            type: 'GET',
            success: function (result) {
                delSuccessCount=result.msg;
            }
        });
        return delSuccessCount;
    }

    table.on('look', function(obj){
        alert("6379");
    });

    //导出excel
    $('#downloadBookExcel').on("click",function(){
        var fileName=$("#fileName").val();
        if(fileName===""){
            fileName="table";
        }
        window.open("/book/downloadBookExcel.json?fileName="+fileName);
    });


});


/*
if(data.length === 0){
    layer.msg('请选择一行');
} else if(data.length > 1){
    layer.msg('只能同时编辑一个');
} else {
    //layer.alert('编辑 [id]：'+ checkStatus.data[0].id);
    editObj=checkStatus.data[0];
    var viewdata = { //数据
        "bookName":data[0].bookName
        ,"bookCode":data[0].bookCode
        ,"authorName":data[0].authorName
        ,"price":data[0].price
        ,"pressName":data[0].pressName
        ,"bookTypeId":data[0].bookTypeId
        ,"bookChcoType":data[0].bookChcoType
        ,"bookSpaceId":data.bookSpaceId
        ,"remark":data[0].remark
        ,"id":data[0].id
        ,"submitType":1
    }
    showBookTypeInfo("编辑",viewdata);
}*/
