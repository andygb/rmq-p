'use strict';

permissionCheckService.$inject = [ '$rootScope' ];

function permissionCheckService($rootScope) {
    return {
        check: check
    };

    function check(sysTableId, btnScript) {
        var isHavePermission=false;
        if(!$rootScope.authButtonList.hasOwnProperty(sysTableId)){
            return false;
        }

        for(var i=0;i<$rootScope.authButtonList[sysTableId].length;i++){
            if($rootScope.authButtonList[sysTableId][i].btnscript===btnScript){
                isHavePermission=true;
                break;
            }

        }
        return isHavePermission;
    }
}

angular
    .module('app')
    .factory('permissionCheckService', permissionCheckService);