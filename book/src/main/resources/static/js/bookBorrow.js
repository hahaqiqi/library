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

    $('#searchBut').on("click",function(){
        var searchVal=$("#searchInput").val();
        table.render({
            elem: '#searchTable'
            ,url: '/book/searchBook.json?searchVal='+searchVal //数据接口
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
                ,{field: '', title: '书籍状态',templet:function(obj){
                        if(obj.status==0){
                            return "未上架";
                        }
                        if(obj.bookLeaseId==null || obj.bookLeaseId==0){
                            return "可借阅";
                        }
                        return "被借阅";
                    }

                }
            ]
            ]
        });
    });

    form.render();

});


