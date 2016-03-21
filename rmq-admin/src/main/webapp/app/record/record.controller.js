/**
 * message-mgmt.controller
 */
(function () {

    'use strict';

    RecordCtrl.$inject = [ '$scope', '$compile', '$filter', '$modal', 'toaster', 'recordService', 'DTOptionsBuilder', 'DTColumnBuilder', '$stateParams', 'permissionCheckService'];

    /**
     * SmsSendCtrl
     */

    angular
        .module('app.record')
        .controller('RecordCtrl', RecordCtrl);
    function RecordCtrl($scope, $compile, $filter, $modal, toaster, recordService, DTOptionsBuilder, DTColumnBuilder, $stateParams, permissionCheckService) {
        var vm = this;

        vm.topic = $stateParams.topic ? $stateParams.topic : "";
        vm.producerIp = "";
        vm.birthTimeBegin = "";
        vm.birthTimeEnd = "";

        var hasViewPermission = permissionCheckService.check(6002, 'OnView');

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
            .withOption('createdRow', function (row, data, dataIndex) {
                $compile(angular.element(row).contents())($scope);
            });

        $scope.dtColumns = [
            DTColumnBuilder.newColumn('id').withTitle('id').notVisible(),
            DTColumnBuilder.newColumn('messageId').withTitle("消息ID").notSortable(),
            DTColumnBuilder.newColumn('topic').withTitle("Topic").notSortable(),
            DTColumnBuilder.newColumn('producerIp').withTitle("发送方IP").notSortable(),
            DTColumnBuilder.newColumn('birthTime').withTitle("产生时间").renderWith(timeHtml).notSortable(),
            DTColumnBuilder.newColumn(null).withTitle('操作').renderWith(actionsHtml).notSortable()
        ];

        $scope.submit = function () {
            $scope.queryBtnDisabled = true;
            $scope.dtInstance.reloadData();
        };

        $scope.view = function(id) {
            recordService.view(id)
                .then(function (resp) {
                    if (resp.data.code == 200) {
                        var record = resp.data.data;


                        $modal.open({
                            templateUrl: 'app/record/modal/view.html',
                            windowClass: 'bounceIn',
                            size: 'lg',
                            controller: function ($scope, $modalInstance) {
                                $scope.modal = {};

                                $scope.modal.messageId = record.messageId;
                                $scope.modal.topic = record.topic;
                                $scope.modal.producerIp = record.producerIp;
                                $scope.modal.birthTime = formatTime(record.birthTime);
                                $scope.modal.content = record.content;
                            }
                        })
                    } else {
                    }
                })
        }

        function serverData(sSource, aoData, fnCallback, oSettings) {
            var draw = aoData[0].value;
            var start = aoData[3].value;
            var limit = aoData[4].value;
            return recordService.query(
                draw,
                vm.topic,
                vm.producerIp,
                vm.birthTimeBegin,
                vm.birthTimeEnd,
                start,
                limit
            ).then(function (resp) {
                    fnCallback(resp.data);
                    vm.tableData = resp.data.data;
                }).finally(function () {
                    $scope.queryBtnDisabled = false;
                })
        }

        function rowCallback(row, data, dataIndex) {
            $('td', row).unbind('dblclick');
            $('td', row).bind('dblclick', function () {
                $scope.getDetail(data);
            });
            return row;
        }

        function timeHtml(data, type, full, meta) {

            if (data == null || data == '') {
                return "";
            }

            return $filter('date')(data, 'yyyy-MM-dd HH:mm:ss.sss');
        }

        function actionsHtml(data, type, full, meta) {
            return '<button class="btn btn-xs btn-success" ng-if="'+ hasViewPermission +'"  ng-click="view(' + full.id + ')">' +
                '    <span class="glyphicon glyphicon-th-large"></span>查看' +
                '</button>';
        }

        function formatTime(date) {
            return $filter('date')(date, 'yyyy-MM-dd HH:mm:ss.sss');
        }

//        $scope.dtInstance.reloadData();
    }

})();