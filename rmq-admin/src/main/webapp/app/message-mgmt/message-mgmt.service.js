/**
 * Message Management service
 */

'use strict';

smsSendService.$inject = [ '$http', '$rootScope' ];

function smsSendService($http, $rootScope) {
    return {
        send: send
    };

    function send(mobileNos, smsContent) {
        return $http.post(
            $rootScope.isProductEnv ? '/boss/message-mgmt/send-sms' : 'message-mgmt/send-sms', {
                mobileNos: mobileNos,
                smsContent: smsContent
            }
        );
    }
}

angular
    .module('app.message-mgmt')
    .factory('smsSendService', smsSendService);
