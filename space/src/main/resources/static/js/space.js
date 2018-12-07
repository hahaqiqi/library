layui.config({
    base: '../js/',
})
layui.use(['treetable','form','jquery','layer', 'laytpl','table'],function(){
    var $=layui.$
        ,layer = layui.layer //弹层
        ,laytpl = layui.laytpl //模板
        ,table=layui.table
    var bookspaceid;
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
        is_checkbox: false,
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
            /*{
                field: 'parentId',
                title: '父ID',
                width: '20%',
            },*/
            {
                field: 'actions',
                title: '操作',
                width: '30%',
                template: function(item){
                    var tem = [];
                    //tem.push('<a lay-filter="edit">编辑</a>');
                    tem.push('<button class="layui-btn layui-btn-xs" lay-filter="edit"><i class="layui-icon">&#xe642;</i></button>');
                    //tem.push('<a class="add-child" lay-filter="add">添加子级</a>');
                    tem.push('<button class="layui-btn layui-btn-xs" lay-filter="add"><i class="layui-icon">&#xe654;</i></button>');
                    //tem.push('<a class="add-child" lay-filter="del">删除</a>');
                    tem.push('<button class="layui-btn layui-btn-xs" lay-filter="del"><i class="layui-icon">&#xe640;</i></button>');
                    //tem.push('<a class="add-child" lay-filter="move">移动至</a>');
                    tem.push('<button class="layui-btn layui-btn-xs" lay-filter="move"><i class="layui-icon">&#xe609;</i></button>');
                    //查看书籍
                    tem.push('<button class="layui-btn layui-btn-xs" lay-filter="sel"><i class="layui-icon">&#xe656;</i></button>');
                    return tem.join('')
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
        showSpaceTypeInfo("添加子空间",viewdata);
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
        showSpaceTypeInfo("编辑空间",viewdata);
    })
    //移动
    treetable.on('treetable(move)',function(data){
        console.dir(data);
        var viewdata = {//数据
            "pid":""
            ,"id":data.item.id
        }
        moveSpace("移动空间",viewdata);
    })
    //删除
    treetable.on('treetable(del)',function (data) {
        layer.confirm('你确认删除这一项吗?',function (index) {
            layer.close(index);
            deleteSpace("删除",data.item.id);
        })

    })
    //查询书籍
    treetable.on('treetable(sel)',function(data){
        console.dir(data);
        bookspaceid=data.item.id
            $.ajax({
                url: '/space/spacelist.json',
                data: {"spaceid":data.item.id},
                type: 'GET',
                async:false,
                success: function (result) {
                    if (result.ret) {
                        selectBookinSpace(data.item.spaceName+"存放的书籍",result.data);
                        /*spopSucess("获取书籍信息成功");*/
                    } else {
                        spopSucess("获取书籍信息失败",result.msg);
                    }
                },
                error:function () {
                    spopFail("未知错误","")
                }
            });

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
        showSpaceTypeInfo("添加父空间",viewdata);
    })

    form.on('switch(status)',function(data){
        layer.msg('监听状态操作');
        console.dir(data);
    })

    function showSpaceTypeInfo(showTitleType,viewdata) {
        var getTpl = demospace.innerHTML
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
            cancel: function(){
                $("#view2").html("");
            }
        });
    };

    function selectBookinSpace(showTitleType,spaceid) {
        table.render({
            elem: '#test1'
            ,url: '/spacebook/selectBook.json?bookspaceid='+spaceid //数据接口
            ,response: {
                statusCode: 0 //规定成功的状态码，默认：0
            }
            ,page:true
            ,toolbar: '#headTemplate'
            ,cols: [
                [ //表头
                    {type: 'checkbox', fixed: 'left'}
                    ,{field: 'bookName',width:161, title: '书籍名称'}
                    ,{field: 'authorName',width:124, title: '作者'}
                    ,{field: 'price',width:115, title: '价格'}
                    ,{field: 'pressName',width:231, title: '出版社'}
                    ,{field: 'bookTypeId',width:121, title: '书籍类型'}
                    ,{field: 'bookLeaseType',width:118, title: '书籍租借类型'}
                    ,{field: 'bookChcoType',width:88, title: '收费方式',templet:function(obj){if(obj.bookChcoType==0){return '免费'}else{ return '收费' } }}
                    /*,{field: '',width:230, title: '书籍状态'}*/
                    ,{field:'remark',width:135, title: '备注'}
                    ,{fixed: 'right',toolbar:'#barDemo', width:223,align:'center'}
                ]
            ]
        });

        //监听头工具栏事件
        table.on('toolbar(test)', function(obj){
            var checkStatus = table.checkStatus(obj.config.id)
                ,data = checkStatus.data; //获取选中的数据
            switch(obj.event) {
                 case 'add':
                     $("#SpaceAddBook").css("display", "block");
                     layer.open({
                         type: 1,
                         title:'添加图书到本空间',
                         shade :0.4,
                         closeBtn:0,
                         shadeClose:true,
                         content: $("#SpaceAddBook"),
                         area: '600px',
                         cancel:function(){
                             $("#SpaceAddBook").html("");
                         },
                         end:function () {
                             $("#SpaceAddBook").css("display","none");
                         }
                     });
                     form.render();
                     break;
                 case 'refresh':
                     $(".layui-laypage-btn")[0].click();
                    break;
            }
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
                layer.confirm('确认从当前空间移除这本书?', function(index){
                    layer.close(index);
                    //layer.msg("删除"+data.id);
                    //向服务端发送删除指令
                    deleteBookSpaceType(data.id,obj);
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

        var index=layer.open({
            title: showTitleType,
            type: 1,
            content: $("#test0"),
            shade: 0,
            end:function(){
             $("#test0").hide();
            },
            success:function () {

            },
        });
        layer.full(index);

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

        //移除单条数据
        function deleteBookSpaceType(delid,obj) {
            $.ajax({
                url: '/spacebook/spaceBookremove.json',
                data: {"bookId":delid},
                async:false,
                type: 'GET',
                success: function (result) {
                    if(result.ret){
                        spopSucess("移除成功");
                        obj.del();
                    }else{
                        spopFail("移除失败",result.msg);
                    }
                }
            });
        }
        //空间中书籍的添加
        $('#submitSpaceBook').on("click",function () {
            $.ajax({
                url: '/spacebook/spaceBookAdd.json',
                data: {'bookpid':bookspaceid,
                       'bookCode':$("#bookCodes").val(),
                        'status':$("input[name='status']:checked").val(),},
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        spopSucess("加载成功");
                        layer.close(layer.index);
                        selectBookinSpace(showTitleType,bookspaceid);
                    } else {
                        spopFail("错误",result.msg);
                    }
                },
                error:function () {
                    spopFail("错误","请检查参数");
                }
            });
        })

        submitTest=function(){
            $("#bookExcelFile").click();
        }
        upExcel=function(){
            var status = $("input[name='status']:checked").val();
            var formData = new FormData($("#spaceBookForm")[0]);
            formData.append("bookspaceid",bookspaceid);
            formData.append("status",status);
            $.ajax({
                url: '/spacebook/spaceBookAddList.json',
                type: 'post',
                data: formData,
                async:false,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.ret == false) {
                        spopFail("加载失败",result.msg);
                    }else {
                        spopSucess("加载成功");
                        $("#bookExcelFile").val("");
                        layer.close(layer.index);
                        selectBookinSpace(showTitleType,bookspaceid)
                    }
                },
                error:function () {
                    $("#bookExcelFile").val("");
                }
            });
        };
       /* $('#submitSpaceBooks').on("click",function () {
            $.ajax({
                url: '/spacebook/spaceBookAddList.json',
                data: {},
                type: 'POST',
                success: function (result) {
                    if (result.ret) {
                        $(".layui-laypage-btn")[0].click();
                        spopSucess("获取成功");
                    } else {
                        spopFail("错误",result.msg);
                    }
                },
                error:function () {
                    spopFail("错误","请检查参数");
                }
            });
        })*/
    }
});