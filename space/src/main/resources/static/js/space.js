layui.config({
    base: '../js/',
})
layui.use(['treetable','form','jquery'],function(){
    var $=layui.$;
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
                field: 'pid',
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
                    tem.push('<a class="add-child" lay-filter="move">移动至</a>');
                    return tem.join(' <font>|</font> ')
                },
            },
        ]
    });

    treetable.on('treetable(add)',function(data){
        layer.msg('添加操作');
        console.dir(data.item.id);
    })

    treetable.on('treetable(edit)',function(data){
        layer.msg('编辑操作');
        console.dir(data);
    })

    treetable.on('treetable(move)',function(data){
        layer.msg('移动操作');
        console.dir(data);
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

    form.on('switch(status)',function(data){
        layer.msg('监听状态操作');
        console.dir(data);
    })
})