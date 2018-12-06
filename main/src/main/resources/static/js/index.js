var http = '',
    form = '';
$ = '';
var addTab,
    layerCRBoat;
new Vue({
    el: '#layout',
    data: {
        isOpen: true,
        userInfo: {}
    },
    created() {
        this.init();
    },
    methods: {
        showMessage() {
            var options = {
                url: 'views/message.html',
                icon: '&#xe63a;',
                title: '消息管理',
                id: '22'
            }
            window.parent.addTab(options)
        },
        closeDialog() {
            layer.close(layerCRBoat);
        },
        //退出登录
        exit() {
            var vm = this;
            layer.confirm('您确定要退出登录吗？', {
                btn: ['确定', '取消'] //按钮
            }, function() {
                window.location.href="/logout.html";
            }, function() {

            });
        },
        init() {
            var vm = this;
            var message;

            layui.config({
                base: '/js/'
            }).use(['app', 'message', 'element', 'http', 'form'], function() {
                var app = layui.app,
                    element = layui.element,
                    navbar = layui.navbar,
                    tab = layui.tab,
                    layer = layui.layer;

                http = layui.http;
                $ = layui.jquery;
                form = layui.form;

                //将message设置为全局以便子页面调用
                message = layui.message;
                //主入口
                app.set({
                    type: 'iframe'
                }).init();

                var menu = [{
                    "id": -1,
                    "children": [],
                    "spread": true,
                    "title": "主页",
                    "url": "",
                    "icon": "&#xe68e;"
                },{
                    id: "1",
                    title: "用户管理",
                    icon: "&#xe770;",
                    spread: false,
                    children: [{
                        id: "11",
                        title: "用户页",
                        icon: "&#xe671;",
                        url: "/user/user.html"
                    }, {
                        id: "12",
                        title: "用户添加页",
                        icon: "&#xe671;",
                        url: "/user/tosave.html"
                    },{
                        id: "13",
                        title: "用户类型页",
                        icon: "&#xe671;",
                        url: "/userType/userType.html",
                    }]
                },{
                    id: "2",
                    title: "空间管理",
                    icon: "&#xe632;",
                    spread: false,
                    children: [{
                        id: "21",
                        title: "空间页",
                        icon: "&#xe671;",
                        url: "/space/space.html"
                    }]
                }, {
                    id: "3",
                    title: "书籍管理",
                    icon: "&#xe705;",
                    spread: false,
                    children: [ {
                        id: "31",
                        title: "书籍租借页",
                        icon: "&#xe671;",
                        url: "/book/bookBorrow.html"
                    },{
                        id: "32",
                        title: "书籍类型页",
                        icon: "&#xe671;",
                        url: "/bookType/bookType.html"
                    }, {
                        id: "33",
                        title: "租借类型页",
                        icon: "&#xe671;",
                        url: "/bookLeaseType/bookLeaseType.html"
                    }, {
                        id: "34",
                        title: "收费类型页",
                        icon: "&#xe671;",
                        url: "/bookCode/bookCode.html"
                    }, {
                        id: "35",
                        title: "书籍租借管理页",
                        icon: "&#xe671;",
                        url: "/bookLease/bookLease.html"
                    }, {
                        id: "36",
                        title: "书籍添加页",
                        icon: "&#xe671;",
                        url: "/book/bookAdd.html"
                    }, {
                        id: "37",
                        title: "书籍管理页",
                        icon: "&#xe671;",
                        url: "/book/book.html"
                    }]
                },{
                    id: "4",
                    title: "权限管理",
                    icon: "&#xe672;",
                    spread: false,
                    children: [{
                        id: "41",
                        title: "权限页",
                        icon: "&#xe671;",
                        url: "/role/role.html"
                    }]
                },{
                    id: "5",
                    title: "管理员管理",
                    icon: "&#xe857;",
                    spread: false,
                    children: [{
                        id: "51",
                        title: "管理员页",
                        icon: "&#xe671;",
                        url: "/admin/admin.html"
                    }]
                },{
                    id: "6",
                    title: "日志管理",
                    icon: "&#xe655;",
                    spread: false,
                    children: [{
                        id: "61",
                        title: "日志页",
                        icon: "&#xe671;",
                        url: "/log/log.html"
                    }]
                }];

                menu.push({
                    id: "",
                    title: "",
                    icon: "",
                    url: ""
                });

                navbar.set({
                    data: menu
                }).render(function(data) {
                    refreshTab(data.id);
                    tab.tabAdd(data);
                });

                //添加指定Tab项
                addTab = function(options) {
                    element.tabDelete('kitTab', options.id);
                    $('li.layui-nav-item:last').find('a').data('options', JSON.stringify(options));
                    $('li.layui-nav-item:last').find('a').trigger('click');
                }

                //删除指定Tab项
                delTab = function(pid) {
                    element.tabDelete('kitTab', pid);
                }

                //刷新指定Tab项
                refreshTab = function(pid) {
                    var item = $('.layui-tab-item[lay-item-id=' + pid + ']');
                    if (!item.length) {
                        return false;
                    }
                    var iframe = $(item).children("iframe");
                    iframe.attr("src", iframe.attr("src"));
                }

                //切换到指定Tab项
                changeTab = function(pid) {
                    element.tabChange('kitTab', pid); //切换到：用户管理
                }

                //隐藏最后一个菜单，用来装载页面添加的菜单
                setTimeout(function() {
                    $('li.layui-nav-item:last').css('display', 'none');
                }, 100);

                $(document).on('click', '.layui-nav-item a', function() {
                    $(this).addClass('active');
                    $(this).parents().siblings().removeClass('active');
                    $(this).parents().siblings().find('a').removeClass('active');
                    $(this).parents('.layui-nav-item').siblings().removeClass('layui-this');
                });
                //监听点击menu
                element.on('nav(kitNavbar)', function(elem) {
                    $(elem).parent('.layui-nav-child').siblings('a').addClass('active');
                    $(elem).parents('.layui-nav-item').siblings().find('a').removeClass('active');
                    $(elem).siblings().find('a').removeClass('active');
                    $(elem).parent().siblings().removeClass('layui-nav-itemed');
                });
                // 监听点击tab
                $(document).on('dblclick', '.layui-tab li', function() {
                    var pid = $(this).attr('lay-id');
                    refreshTab(pid);
                });
            });
        }
    }
});