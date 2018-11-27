layui.config({
    base: '../js/',
})
layui.use(['treetable','form','jquery','layer', 'laytpl'],function(){
    var $=layui.$
        ,layer = layui.layer //弹层
        ,laytpl = layui.laytpl //模板
    var data;
    $.ajax({
        url: '/space/list.json',
        data: $("#bookTypeForm").serializeArray(),
        type: 'POST',
        async:false,
        success: function (result) {
            if (result.ret) {
                data=result.data;
            }
        },
    });
    var o = layui.$,treetable = layui.treetable;
    var form = layui.form,layer = layui.layer;
    treetable.render({
        elem: '#test-tree-table',
        data: data,
        field: 'spaceName',
        is_checkbox: true,
        /*icon_val: {
            open: "&#xe619;",
            close: "&#xe61a;"
        },
        space: 4,*/
        cols: [
            {
                field: 'spaceName',
                title: '标题',
                width: '30%',
            },
            {
                field: 'id',
                title: 'ID',
                width: '20%'
            },
            {
                field: 'parentId',
                title: '父ID',
                width: '20%',
            },
            {
                field: 'actions',
                title: '操作',
                width: '30%',
                template: function(item){
                    var tem = [];
                    tem.push('<a lay-filter="edit">编辑</a>');
                    tem.push('<a class="add-child" lay-filter="add">添加子级</a>');
                    tem.push('<a class="add-child" lay-filter="del">删除</a>');
                    tem.push('<a class="add-child" lay-filter="move">移动至</a>');
                    return tem.join(' <font>|</font> ')
                },
            },
        ]
    });
    //编辑子空间
    treetable.on('treetable(add)',function(data){
        console.dir(data.item.id);
        var viewdata = { //数据
            "spaceName":""
            ,"remark":""
            ,"submitType":1
            ,"parentId":data.item.id

        }
        showBookTypeInfo("添加子空间",viewdata);
    })

    //编辑空间
    treetable.on('treetable(edit)',function(data){
        console.dir(data);
        var viewdata = { //数据
            "spaceName":data.item.spaceName
            ,"remark":data.item.remark
            ,"submitType":2
            ,"id":data.item.id

        }
        showBookTypeInfo("编辑空间",viewdata);
    })

    treetable.on('treetable(move)',function(data){
        console.dir(data);
        var viewdata = {//数据
            "pid":""
            ,"id":data.item.id
        }
        moveSpace("移动空间",viewdata);
    })

    treetable.on('treetable(del)',function (data) {
        layer.confirm('你确认删除这一项吗?',function (index) {
            layer.close(index);
            deleteSpace("删除",data.item.id);
        })

    })



    o('.up-all').click(function(){
        treetable.all('up');
    })

    o('.down-all').click(function(){
        treetable.all('down');
    })

    o('.get-checked').click(function(){
        console.dir(treetable.all('checked'));
    })
    //添加父空间
    o('.add-parentsapce').click(function(){
        //
        var viewdata = { //数据
            "spaceName":""
            ,"remark":""
            ,"submitType":1

        }
        showBookTypeInfo("添加父空间",viewdata);
    })

    form.on('switch(status)',function(data){
        layer.msg('监听状态操作');
        console.dir(data);
    })

    function showBookTypeInfo(showTitleType,viewdata) {
        var getTpl = demo.innerHTML
            ,view = document.getElementById('view');
        laytpl(getTpl).render(viewdata, function(html){
            view.innerHTML = html;
        });
        layer.open({
            title: showTitleType,
            type: 1,
            content: $("#view"),
            shade: 0,
            area: '350px',
            success:function(){
                //监听提交数据
                //submitType，=1为新增，2为编辑
                $('#submitSpace').on("click",function(){
                    var submitType=$(this).attr("submitType");
                    $.ajax({
                        url: submitType==1 ? '/space/save.json' : '/space/update.json',
                        data: $("#SpaceForm").serializeArray(),
                        type: 'POST',
                        success: function (result) {
                            if (result.ret) {
                                spopSucess(submitType==1?"添加成功":"修改成功");
                                layer.close(layer.index);
                                window.location.reload();
                                $("#view").html("");
                            } else {
                                spopFail(submitType==1?"添加失败":"修改失败",result.msg);
                            }
                        },
                        error:function () {
                            spopFail("未知错误","")
                        }
                    });
                });
            },
            cancel: function(){
                $("#view").html("");
            }
        });
    };
    //删除单个数据
    function deleteSpace(Showtitle,id) {
        $.ajax({
            title:Showtitle,
            url: '/space/delete.json',
            data: {"id":id},
            type: 'GET',
            success: function (result) {
                if(result.ret){
                    spopSucess("删除成功");
                    window.location.reload();
                }else{
                    spopFail("删除失败","该项可能已经不存在");
                    window.location.reload();
                }
            }
        });

    }

    function moveSpace(showTitleType,viewdata) {
        var getTpl = demo2.innerHTML
            ,view = document.getElementById('view2');
        laytpl(getTpl).render(viewdata, function(html){
            view.innerHTML = html;
        });
        layer.open({
            title: showTitleType,
            type: 1,
            content: $("#view2"),
            shade: 0,
            area: '350px',
            success:function () {
                $('#submitSpace2').on("click",function(){
                    $.ajax({
                        url: '/space/move.json',
                        data: $("#SpaceForm2").serializeArray(),
                        type: 'POST',
                        success: function (result) {
                            if (result.ret) {
                                spopSucess("移动成功");
                                layer.close(layer.index);
                                window.location.reload();
                                $("#view2").html("");
                            } else {
                                spopFail("移动失败",result.msg);
                            }
                        },
                        error:function () {
                            spopFail("未知错误","")
                        }
                    });
                });
            },
        });
    };

})