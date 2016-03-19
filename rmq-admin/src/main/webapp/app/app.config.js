/**
 * Admin - 后台管理
 *
 * Use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 */

(function () {

    'use strict';

    function config($stateProvider, $urlRouterProvider,
                    $ocLazyLoadProvider, IdleProvider, KeepaliveProvider) {

        // Configure Idle settings
        IdleProvider.idle(5); // in seconds
        IdleProvider.timeout(120); // in seconds

        $urlRouterProvider.otherwise('/topic-mgmt/query');


        $ocLazyLoadProvider.config({
            // Set to true if you want to see what and when is dynamically loaded
            debug: true,
            events: true,
            modules:[
                {
                    name:"app.topic-mgmt",
                    module: true,
                    files: [
                        'app/topic-mgmt/topic-mgmt.module.js',
                        'app/topic-mgmt/topic-mgmt.service.js',
                        'app/topic-mgmt/topic-mgmt.controller.js'
                    ]
                },{
                    name:"app.record",
                    module: true,
                    files: [
                        'app/record/record.module.js',
                        'app/record/record.service.js',
                        'app/record/record.controller.js',
                    ]
                }
            ]
        });


        $stateProvider
            .state('topic-mgmt', {
                abstract: true,
                url: '/topic-mgmt',
                templateUrl: 'views/common/content.html',
            })
            .state('topic-mgmt.query', {
                url:'/query',
                templateUrl:'app/topic-mgmt/topic-query.html',
                data: {pageTitle: 'Topic配置管理', sysTableId:6003},
                controller:'TopicMgmtCtrl',
                cache:'false',
                controllerAs:'vm'
            })
            .state('record', {
                abstract:true,
                url: '/record',
                templateUrl: 'views/common/content.html',
            })
            .state('record.query', {
                url:'/query',
                templateUrl:'app/record/record.html',
                data: {pageTitle: '消息记录' ,sysTableId:6002},
                controller:'RecordCtrl',
                cache:'false',
                controllerAs:'vm',
                params:{
                    topic: ''
                },
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load("app.record");
                        return $ocLazyLoad.load([{
                            name: 'datePicker',
                            files: ['css/plugins/datapicker/angular-datapicker.css', 'js/plugins/datapicker/angular-datepicker.js']
                        }
                        ]);
                    }
                }
            })
        ;
    }

//

    angular
        .module('app')
        .config(config)
        .run(function ($rootScope, $state, $location, $cookies, $templateCache, toaster) {
            $rootScope.isProductEnv = true;//废弃
            $rootScope.$state = $state;
            $rootScope.user = LG.me;
            $rootScope.appName = LG.appConfig.appName;
            $rootScope.authButtonList = LG.authButtonList;
            //if ($location.search() && $location.search().usertoken && $location.search().usertoken != '') {
            //    $rootScope.user.usertoken = $location.search().usertoken;
            //    $cookies.put('usertoken', $location.search().usertoken);
            //} else {
            //    $rootScope.user.usertoken = $cookies.get('usertoken');
            //}

            $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                var userToken = $cookies.get('usertoken');
                // 如果用户不存在
                if (!$rootScope.user || !userToken) {
                    event.preventDefault();// 取消默认跳转行为
                    window.location.href = LG.appConfig.loginUrl;
                } else {
                    $rootScope.user.usertoken = userToken;

                    var sysTableId = toState.data.sysTableId;//systableid可以认为是menuId,适合menuId是一一对应的
                    if (!sysTableId) {
                        //toast('参数错误','没有找到定义的菜单编号,请删除缓存重试!',3000);
                        //setTimeout(function(){
                        //    event.preventDefault();// 取消默认跳转行为
                        //},3500);
                        //alert('没有找到定义的菜单编号,请删除缓存重试!');
                        event.preventDefault();// 取消默认跳转行为
                        toaster.pop({
                            type: 'error',
                            title: '参数错误',
                            body: '没有找到定义的菜单编号,请删除缓存重试!',
                            showCloseButton: true,
                            timeout: 3000
                        });
                        if (fromState.name) {
                            $state.go(fromState.name);
                        } else {
                            window.location = "/";
                        }
                    }

                    //查询是否有此菜单权限的的列表
                    if (!$rootScope.authButtonList.hasOwnProperty(sysTableId)) {

                        event.preventDefault();

                        toaster.pop({
                            type: 'error',
                            title: '无权限',
                            body: '无菜单【' + toState.data.pageTitle + '】访问权限',
                            showCloseButton: true,
                            timeout: 3000
                        });
                        if (fromState.name) {

                            $state.go(fromState.name);
                        } else {
                            window.location = "/";
                        }

                    }


                }

            });


            $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
                if (toState.data && toState.data.pageTitle) {
                    $rootScope.pageTitle = toState.data.pageTitle;
                }
            });


            //$rootScope.$on('$viewContentLoaded', function() {
            //
            //    alert(JSON.stringify($templateCache));
            //});
        });


})();
