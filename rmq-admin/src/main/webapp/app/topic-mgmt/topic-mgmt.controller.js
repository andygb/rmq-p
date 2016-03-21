/**
 * message-mgmt.controller
 */
(function() {

    'use strict';

    TopicMgmtCtrl.$inject = [ '$scope','$compile', '$modal', 'toaster', 'topicMgmtService', 'DTOptionsBuilder', 'DTColumnBuilder', '$state', 'permissionCheckService' ];

    /**
     * SmsSendCtrl
     */
    function TopicMgmtCtrl($scope, $compile, $modal, toaster, topicMgmtService, DTOptionsBuilder, DTColumnBuilder, $state, permissionCheckService) {
        var vm = this;

        vm.hasCreatePermission=permissionCheckService.check(6003,'OnCreate');
        var hasProducePermission = permissionCheckService.check(6003, 'OnProduce');

        $scope.dtInstance = {};


        $scope.dtOptions = DTOptionsBuilder
            .fromSource('')
            .withFnServerData(serverData)
            .withDataProp('data')
            .withOption('serverSide', true)
            .withOption('searching', false)
            .withOption('lengthChange', true)
            .withOption('autoWidth', false)
            .withOption('scrollX', false)
            .withOption('lengthMenu', [10, 25, 50, 100, 200])
            .withPaginationType('full_numbers')
//            .withOption('rowCallback', rowCallback)
            .withOption('createdRow', function(row, data, dataIndex) {
                $compile(angular.element(row).contents())($scope);
            });

        $scope.dtColumns = [
            DTColumnBuilder.newColumn('id').withTitle('topicId').notVisible(),
            DTColumnBuilder.newColumn('name').withTitle('名称').notSortable(),
            DTColumnBuilder.newColumn('memo').withTitle('备注').notSortable(),
            DTColumnBuilder.newColumn(null).withTitle('操作').notSortable().renderWith(actionsHtml)
        ];

        $scope.dtChanged = true;

        $scope.create = function () {

            var opener = $scope;

            $modal.open({
                templateUrl: 'app/topic-mgmt/modal/add-topic.html',
                windowClass: 'bounceIn',
                size: 'md',
                controller : function($scope, $modalInstance) {
                    $scope.modal = {};
                    $scope.modal.modalName = "创建Topic";
                    $scope.modal.titleName = "名称";
                    $scope.modal.contentName = "备注";

                    $scope.cancel = function() {
                        $modalInstance.dismiss('cancel');
                    };

                    $scope.ok = function() {
                        $scope.okBtnDisabled = true;

                        topicMgmtService.create($scope.modal.title, $scope.modal.content)
                            .then(function (response) {
                                if(response.data.code == 200) {
                                    toaster.pop({
                                        type : 'success',
                                        title: '操作成功',
                                        showCloseButton: true,
                                        timeout: 5000
                                    });

                                    $modalInstance.close();
                                    opener.dtInstance.reloadData();
                                } else {
                                    toaster.pop({
                                        type: 'error',
                                        title: '出错啦',
                                        body: response.data.message,
                                        showCloseButton: true,
                                        timeout:5000
                                    })
                                }
                            })
                            .finally(function() {
                                $scope.okBtnDisabled = false;
                            })
                    }
                }
            })
        };

        $scope.produce = function(topic) {
            var opener = $scope;
            $modal.open({
                templateUrl: 'app/topic-mgmt/modal/add-topic.html',
                windowClass: 'bounceIn',
                size: 'lg',
                controller : function($scope, $modalInstance) {
                    $scope.modal = {};
                    $scope.modal.modalName = "发送消息";
                    $scope.modal.titleName = "Topic";
                    $scope.modal.contentName = "内容";

                    $scope.modal.title = topic;
                    $scope.cancel = function() {
                        $modalInstance.dismiss('cancel');
                    };

                    $scope.ok = function() {
                        $scope.okBtnDisabled = true;

                        topicMgmtService.produce($scope.modal.title, $scope.modal.content)
                            .then(function (response) {
                                if(response.data.code == 200) {
                                    toaster.pop({
                                        type : 'success',
                                        title: '操作成功',
                                        showCloseButton: true,
                                        timeout: 5000
                                    });

                                    $modalInstance.close();
                                    opener.dtInstance.reloadData();
                                } else {
                                    toaster.pop({
                                        type: 'error',
                                        title: '出错啦',
                                        body: response.data.message,
                                        showCloseButton: true,
                                        timeout:5000
                                    })
                                }
                            })
                            .finally(function() {
                                $scope.okBtnDisabled = false;
                            })
                    }
                }
            })
        };

        $scope.record = function(topic) {
            $state.go('record.query', {topic : topic})
        };

        function serverData(sSource, aoData, fnCallback, oSettings) {
            var draw = aoData[0].value;
            var start = aoData[3].value;
            var limit = aoData[4].value;
            return topicMgmtService.query(
                draw,
                start,
                limit
            ).then(function(resp) {
                    fnCallback(resp.data);
                    vm.tableData = resp.data.data;
                }).finally(function () {
                    $scope.queryBtnDisabled = false;
                })
        }

        function rowCallback(row, data, dataIndex) {
            $('td', row).unbind('dblclick');
            $('td', row).bind('dblclick', function() {
                $scope.getDetail(data);
            });
            return row;
        }

        function actionsHtml(data, type, full, meta) {
            return '<button class="btn btn-xs btn-danger" ng-if="'+ hasProducePermission +'" ng-click="produce(\'' + full.name + '\')">' +
                '    <span class="fa fa-mail-forward"></span>发送消息' +
                '</button>' +
                '<button class="btn btn-xs btn-success" ng-click="record(\'' + full.name + '\')">' +
                '    <span class="fa fa-list"></span>消息记录' +
                '</button>';
        }
//        $scope.dtInstance.reloadData();
    }

    angular
        .module('app.topic-mgmt')
        .controller('TopicMgmtCtrl', TopicMgmtCtrl);

})();