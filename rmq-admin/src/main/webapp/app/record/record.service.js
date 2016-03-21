/**
 * Message Management service
 */

'use strict';

recordService.$inject = [ '$http', '$rootScope', '$filter' ];

function recordService($http, $rootScope, $filter) {
    return {
        query: query,
        view: view
    };

    function query(
        draw,
        topic,
        producerIp,
        birthTimeBegin,
        birthTimeEnd,
        start,
        limit
        ) {

//        return $http.post(
//            'record/query', {
//                draw: draw,
//                topic: topic,
//                producerIp: producerIp,
//                birthTimeBegin: birthTimeBegin,
//                birthTimeEnd: birthTimeEnd,
//                start: start,
//                limit: limit
//            }
//        );

        return $http({
            url:'record/query',
            method: 'POST',
            data: {
                draw: draw,
                topic: topic,
                producerIp: producerIp,
                birthTimeBegin: formatDateTime(birthTimeBegin),
                birthTimeEnd: formatDateTime(birthTimeEnd),
                start: start,
                limit: limit
            },
//            headers: {
//                'Content-Type':''
//            }
        });
    }

    function view(
        id
        ) {
        return $http.post(
            'record/view', {
                id: id
            }
        )
    }

    function formatDateTime(dateTime) {
        return $filter('date')(dateTime, 'yyyy-MM-dd HH:mm:ss');
    }

}

angular
    .module('app.record')
    .factory('recordService', recordService);
