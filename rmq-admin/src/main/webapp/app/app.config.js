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
                data: {pageTitle: 'Topic配置管理'},
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
                data: {pageTitle: '消息记录'},
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
        .run(function($rootScope, $state, $location, $cookies) {
            $rootScope.isProductEnv = false;
            $rootScope.$state = $state;
            $rootScope.user = {};
            if ($location.search() && $location.search().usertoken && $location.search().usertoken != '') {
                $rootScope.user.usertoken = $location.search().usertoken;
                $cookies.put('usertoken', $location.search().usertoken);
            } else {
                $rootScope.user.usertoken = $cookies.get('usertoken');
            }
        });

})();
