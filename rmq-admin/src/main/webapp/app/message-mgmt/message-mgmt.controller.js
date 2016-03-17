/**
 * message-mgmt.controller
 */
(function() {

    'use strict';

    SmsSendCtrl.$inject = [ '$scope', '$modal', 'toaster', 'smsSendService' ];

    /**
     * SmsSendCtrl
     */
    function SmsSendCtrl($scope, $modal, toaster, smsSendService) {
        var vm = this;

        vm.mobileNos = '';
        vm.smsContent = '';
        vm.leftWords = 70;
        vm.smsCnt = 0;

        $scope.strlen = strlen;

        $scope.calculate = function() {
            vm.leftWords = 70 - strlen(vm.smsContent) % 70;
            vm.smsCnt = parseInt((strlen(vm.smsContent) + 69) / 70);
            if (vm.smsCnt == 3 && vm.leftWords == 70) {
                vm.leftWords = 0;
            } else if (vm.smsCnt > 3) {
                vm.smsCnt = 3;
                vm.leftWords = 210 - strlen(vm.smsContent);
            }
        };

        $scope.$watch('vm.mobileNos', function(newVal, oldVal) {
            if (newVal) {
                vm.mobileNos = newVal.replace(/，/g, ',').replace(/\r| |\n|\t/g, '');
            }
        });

        $scope.submit = function() {
            $modal.open({
                templateUrl: 'app/message-mgmt/modal/sms-send-confirm.html',
                controller: function ($scope, $modalInstance) {
                    $scope.modal = {};
                    $scope.modal.mobileNos = vm.mobileNos;
                    $scope.modal.smsContent = vm.smsContent;
                    $scope.ok = function () {
                        smsSendService.send($scope.modal.mobileNos, $scope.modal.smsContent)
                            .then(function successCallback(response) {
                                toaster.pop({
                                    type: response.data.code == 200 ? 'success' : 'error',
                                    title: '短信发送',
                                    body: response.data.message,
                                    showCloseButton: true,
                                    timeout: 5000
                                });
                             });
                        $modalInstance.close();
                    };

                    $scope.cancel = function () {
                        $modalInstance.dismiss('cancel');
                    }
                },
                windowClass: 'animated bounceIn'
            });
        };
    }

    angular
        .module('app.message-mgmt')
        .controller('SmsSendCtrl', SmsSendCtrl);

})();