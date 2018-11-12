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
                window.href="/logout";
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
                    title: "空间管理",
                    icon: "",
                    spread: false,
                    children: [{
                        id: "10",
                        title: "子模块1",
                        icon: "",
                        url: "/user.html"
                    }, {
                        id: "11",
                        title: "子模块2",
                        icon: "",
                        url: "/user.html"
                    }]
                }, {
                    id: "2",
                    title: "用户管理",
                    icon: "",
                    spread: false,
                    children: [{
                        id: "12",
                        title: "子模块1",
                        icon: "",
                        url: "/user.html"
                    }, {
                        id: "13",
                        title: "子模块2",
                        icon: "",
                        url: "/user.html"
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
                    $(this).parents('.layui-nav-item').siblings().find('a').removeClass('active');
                    $(this).parents('.layui-nav-item').siblings().removeClass('layui-this');
                });
                //监听点击menu
                element.on('nav(kitNavbar)', function(elem) {
                    $(elem).parent('.layui-nav-child').siblings('a').addClass('active');
                    $(elem).parents('.layui-nav-item').siblings().find('a').removeClass('active');
                    $(elem).siblings().find('a').removeClass('active');
                });
                // 监听点击tab
                $(document).on('click', '.layui-tab li', function() {
                    var pid = $(this).attr('lay-id');
                    refreshTab(pid);
                });
            });
        }
    }
});