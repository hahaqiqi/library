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

var pageOldMax;
var sliderPage;
var pageMax;
function pageCh(res, curr, count) {
    layui.use(['slider','jquery'], function() {
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
                ,showstep: true
                , change: function (value) {
                    if(value==0){
                        return;
                    }
                    if(value!=$('.layui-input').val()) {
                        $('.layui-input').val(value)
                        $('.layui-laypage-btn').click();
                    }
                }
            });
        }else{
            sliderPage.setValue(curr);
        }
    });
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


    //向世界问个好
    //layer.msg('Hello World');

    //执行一个 table 实例(加载数据)
    table.render({
        done: function(res, curr, count){
            //如果是异步请求数据方式，res即为你接口返回的信息。
            //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
            console.log(res);
            //得到当前页码
            console.log(curr);
            //得到数据总量
            console.log(count);
        }
    });

    var editObj;
    //监听头工具栏事件
    table.on('toolbar(test)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id)
            ,data = checkStatus.data; //获取选中的数据
        switch(obj.event){
            case 'add':
                var viewdata = { //数据
                    "bookTypeName":""
                    ,"remark":""
                    ,"submitType":1
                }
                showBookTypeInfo("新增",viewdata);
                break;
            case 'update':
                if(data.length === 0){
                    layer.msg('请选择一行');
                } else if(data.length > 1){
                    layer.msg('只能同时编辑一个');
                } else {
                    //layer.alert('编辑 [id]：'+ checkStatus.data[0].id);
                    editObj=checkStatus.data[0];
                    var viewdata = { //数据
                        "bookTypeName":data[0].typeName
                        ,"remark":data[0].remark
                        ,"submitType":2
                        ,"id":data[0].id
                    }
                    showBookTypeInfo("编辑",viewdata);
                }
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
                            spopBatchHint(fk);
                        }else{
                            spopFail("操作错误","");
                        }
                        $(".layui-laypage-btn")[0].click();
                    });

                }
                break;
            case 'refresh': //刷新
                $(".layui-laypage-btn")[0].click();
                break;
        };
    });

    //监听行工具事件
    table.on('tool(test)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
            ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'detail'){
            var viewdata = { //数据
                "bookTypeName":data.typeName
                ,"remark":data.remark
                ,"submitType":0
            }
            showBookTypeInfo("查看",viewdata);
        } else if(layEvent === 'del'){
            layer.confirm('确认删除这一条数据?', function(index){
                layer.close(index);
                //layer.msg("删除"+data.id);
                //向服务端发送删除指令
                if(deleteBookType(data.id)){
                    $(".layui-laypage-btn")[0].click();
                    //obj.del(); //删除对应行（tr）的DOM结构
                    spopSucess("删除成功");
                }else{
                    spopFail("删除失败","该项可能已经不存在");
                };
            });
        } else if(layEvent === 'edit'){
            editObj=data;
            var viewdata = { //数据
                "bookTypeName":data.typeName
                ,"remark":data.remark
                ,"submitType":2
                ,"id":data.id
            }
            showBookTypeInfo("编辑",viewdata);
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
            title: showTitleType+'图书类型',
            type: 1,
            content: $("#view"),
            shade: 0,
            area: '350px',
            success:function(){
                //监听提交数据
                //submitType 0为查看，=1为新增，2为编辑
                $('#submitBookType').on("click",function(){
                    var submitType=$(this).attr("submitType");
                    $.ajax({
                        url: submitType==1 ? '/bookType/save.json' : '/bookType/update.json',
                        data: $("#bookTypeForm").serializeArray(),
                        type: 'POST',
                        success: function (result) {
                            if (result.ret) {
                                spopSucess(submitType==1?"添加成功":"修改成功");
                                layer.close(layer.index);
                                $(".layui-laypage-btn")[0].click();
                                $("#view").html("");
                            } else {
                                spopFail(submitType==1?"添加失败":"修改失败",result.msg);
                            }
                        }
                    });
                });
            },
            cancel: function(){
                $("#view").html("");
            }
        });
    }


    //删除单条数据
    function deleteBookType(delid) {
        var delSuccess=false;
        $.ajax({
            url: '/bookType/delete.json',
            data: {"id":delid},
            async:false,
            type: 'GET',
            success: function (result) {
                delSuccess=result.ret;
            }
        });
        return delSuccess;
    }

    //批量删除数据
    function batchDeleteBookType(batchStrId) {
        var delSuccessCount=0;
        $.ajax({
            url: '/bookType/batchDelete.json',
            data: {"batchStrId":batchStrId},
            async:false,
            type: 'GET',
            success: function (result) {
                delSuccessCount=result.msg;
            }
        });
        return delSuccessCount;
    }



});


