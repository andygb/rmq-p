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

        $urlRouterProvider.otherwise('/user-mgmt/user-level-mgmt');


        $ocLazyLoadProvider.config({
            // Set to true if you want to see what and when is dynamically loaded
            debug: false
        });


        //for (var i = 0; i < LG.authMenuList.length; i++) {
        //    var menuName= LG.authMenuList[i].
        //    $stateProvider
        //        .state('user-mgmt', {
        //            abstract: true,
        //            url: '/user-mgmt',
        //            cache:'false',
        //            templateUrl: 'views/common/content.html',
        //        });
        //};

        $stateProvider
            .state('user-mgmt', {
                abstract: true,
                url: '/user-mgmt',
                cache: 'false',
                templateUrl: 'views/common/content.html',
            });
        $stateProvider.state('user-mgmt.user-level-mgmt', {
            url: '/user-level-mgmt',
            templateUrl: 'app/user-mgmt/user-level-mgmt.html',
            data: {pageTitle: '用户等级管理'},
            controller: 'UserLevelMgmtCtrl',
            cache: 'false',
            controllerAs: 'vm'
        })
            .state('user-mgmt.shop-level-mgmt', {
                url: '/shop-level-mgmt',
                templateUrl: 'app/user-mgmt/shop-level-mgmt.html',
                data: {pageTitle: '店铺等级管理'},
                controller: 'ShopLevelMgmtCtrl',
                controllerAs: 'vm',
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            {
                                insertBefore: '#loadBefore',
                                name: 'localytics.directives',
                                files: ['css/plugins/chosen/chosen.css', 'js/plugins/chosen/chosen.jquery.js', 'js/plugins/chosen/chosen.js']
                            }
                        ]);
                    }
                }
            })
            .state('coupon-mgmt', {
                abstract: true,
                url: '/coupon-mgmt',
                templateUrl: 'views/common/content.html',
            })
            .state('coupon-mgmt.coupon-query', {
                url: '/coupon-query',
                templateUrl: 'app/coupon-mgmt/coupon-query.html',
                data: {pageTitle: '优惠券查询'},
                controller: 'CouponMgmtCtrl',
                controllerAs: 'vm',
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        return $ocLazyLoad.load([{
                            name: 'datePicker',
                            files: ['css/plugins/datapicker/angular-datapicker.css', 'js/plugins/datapicker/angular-datepicker.js']
                        }, {
                            serie: true,
                            files: ['js/plugins/moment/moment.min.js', 'js/plugins/daterangepicker/daterangepicker.zh.js', 'css/plugins/daterangepicker/daterangepicker-bs3.css']
                        }, {
                            name: 'daterangepicker',
                            files: ['js/plugins/daterangepicker/angular-daterangepicker.js']
                        }]);
                    }
                }
            })
            .state('message-mgmt', {
                abstract: true,
                url: '/message-mgmt',
                templateUrl: 'views/common/content.html'
            })
            .state('message-mgmt.sms-send', {
                url: '/sms-send',
                templateUrl: 'app/message-mgmt/sms-send.html',
                data: {pageTitle: '短信发送'},
                controller: 'SmsSendCtrl',
                controllerAs: 'vm'
            })
            .state('shop-mgmt', {
                abstract: true,
                url: '/shop-mgmt',
                templateUrl: 'views/common/content.html',
            })

            .state('dic-mgmt', {
                abstract: true,
                url: '/dic-mgmt',
                templateUrl: 'views/common/content.html',
            })
            .state('dic-mgmt.dic-query', {
                url: '/dic-query',
                templateUrl: 'app/dic-mgmt/dic-query.html',
                data: {pageTitle: '字典管理'},
                controller: 'DicMgmtCtrl',
                controllerAs: 'vm'
            })

            .state('trade-mgmt', {
                abstract: true,
                url: '/trade-mgmt',
                templateUrl: 'views/common/content.html',
            })
            .state('trade-mgmt.trade-query', {
                url: '/trade-query',
                templateUrl: 'app/trade-mgmt/trade-query.html',
                data: {pageTitle: '订单查询'},
                controller: 'TradeMgmtCtrl',
                controllerAs: 'vm',
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        return $ocLazyLoad.load([{
                            name: 'datePicker',
                            files: ['css/plugins/datapicker/angular-datapicker.css', 'js/plugins/datapicker/angular-datepicker.js']
                        }, {
                            serie: true,
                            files: ['js/plugins/moment/moment.min.js', 'js/plugins/daterangepicker/daterangepicker.zh.js', 'css/plugins/daterangepicker/daterangepicker-bs3.css']
                        }, {
                            name: 'daterangepicker',
                            files: ['js/plugins/daterangepicker/angular-daterangepicker.js']
                        }]);
                    }
                }
            })
            .state('trade-mgmt.trade-item-query', {
                url: '/trade-item-query',
                templateUrl: 'app/trade-mgmt/trade-item-query.html',
                data: {pageTitle: '订单商品'},
                controller: 'TradeItemMgmtCtrl',
                controllerAs: 'vm',
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        return $ocLazyLoad.load([{
                            name: 'datePicker',
                            files: ['css/plugins/datapicker/angular-datapicker.css', 'js/plugins/datapicker/angular-datepicker.js']
                        }, {
                            serie: true,
                            files: ['js/plugins/moment/moment.min.js', 'js/plugins/daterangepicker/daterangepicker.zh.js', 'css/plugins/daterangepicker/daterangepicker-bs3.css']
                        }, {
                            name: 'daterangepicker',
                            files: ['js/plugins/daterangepicker/angular-daterangepicker.js']
                        }]);
                    }
                }
            })
            .state('trade-mgmt.trade-period-query', {
                url: '/trade-period-query',
                templateUrl: 'app/trade-mgmt/trade-period-query.html',
                data: {pageTitle: '分期订单'},
                controller: 'TradePeriodMgmtCtrl',
                controllerAs: 'vm',
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        return $ocLazyLoad.load([{
                            name: 'datePicker',
                            files: ['css/plugins/datapicker/angular-datapicker.css', 'js/plugins/datapicker/angular-datepicker.js']
                        }, {
                            serie: true,
                            files: ['js/plugins/moment/moment.min.js', 'js/plugins/daterangepicker/daterangepicker.zh.js', 'css/plugins/daterangepicker/daterangepicker-bs3.css']
                        }, {
                            name: 'daterangepicker',
                            files: ['js/plugins/daterangepicker/angular-daterangepicker.js']
                        }]);
                    }
                }
            })
            .state('coupon-mgmt.coupon-userecord-query', {
                url: '/coupon-userecord-query',
                templateUrl: 'app/coupon-mgmt/coupon-userecord-query.html',
                data: {pageTitle: '优惠券使用记录'},
                controller: 'CouponUseMgmtCtrl',
                controllerAs: 'vm',
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        return $ocLazyLoad.load([{
                            name: 'datePicker',
                            files: ['css/plugins/datapicker/angular-datapicker.css', 'js/plugins/datapicker/angular-datepicker.js']
                        }, {
                            serie: true,
                            files: ['js/plugins/moment/moment.min.js', 'js/plugins/daterangepicker/daterangepicker.zh.js', 'css/plugins/daterangepicker/daterangepicker-bs3.css']
                        }, {
                            name: 'daterangepicker',
                            files: ['js/plugins/daterangepicker/angular-daterangepicker.js']
                        }]);
                    }
                }
            })
            .state('shop-mgmt.shop-query', {
                url: '/shop-query',
                templateUrl: 'app/shop-mgmt/shop-query.html',
                data: {pageTitle: '店铺审核'},
                controller: 'ShopMgmtCtrl',
                controllerAs: 'vm',
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        return $ocLazyLoad.load([{
                            name: 'datePicker',
                            files: ['css/plugins/datapicker/angular-datapicker.css', 'js/plugins/datapicker/angular-datepicker.js']
                        }, {
                            serie: true,
                            files: ['js/plugins/moment/moment.min.js', 'js/plugins/daterangepicker/daterangepicker.zh.js', 'css/plugins/daterangepicker/daterangepicker-bs3.css']
                        }, {
                            name: 'daterangepicker',
                            files: ['js/plugins/daterangepicker/angular-daterangepicker.js']
                        }]);
                    }
                }
            });
    }

    angular
        .module('app')
        .config(config)
        .run(function ($rootScope, $state, $location, $cookies, $templateCache) {
            $rootScope.isProductEnv = true;//废弃
            $rootScope.$state = $state;
            $rootScope.user = LG.me;
            $rootScope.appName = LG.appConfig.appName;
            //$rootScope.authMenuList = LG.authMenuList;

            //alert(JSON.stringify($rootScope.user));
            if ($location.search() && $location.search().usertoken && $location.search().usertoken != '') {

                $rootScope.user.usertoken = $location.search().usertoken;
                $cookies.put('usertoken', $location.search().usertoken);
            } else {
                $rootScope.user.usertoken = $cookies.get('usertoken');
            }

            //$rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
            //    var userToken = $cookies.get('usertoken');
            //    // 如果用户不存在
            //    if (!$rootScope.user || !userToken) {
            //        event.preventDefault();// 取消默认跳转行为
            //        window.location.href = LG.appConfig.loginUrl;
            //    } else {
            //        $rootScope.user.usertoken = userToken;
            //
            //    }
            //
            //});

            $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
                if (toState.data && toState.data.pageTitle) {
                    $rootScope.pageTitle = toState.data.pageTitle;
                    //$templateCache.remove(toState.templateUrl);

                }
            });


            //$rootScope.$on('$viewContentLoaded', function() {
            //
            //    alert(JSON.stringify($templateCache));
            //});
        });

})();
