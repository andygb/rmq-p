/**
 * Message Management service
 */

'use strict';

topicMgmtService.$inject = [ '$http', '$rootScope' ];

function topicMgmtService($http, $rootScope) {
    return {
        query: query,
        create: create
    };

    function query(
        draw,
        start,
        limit
        ) {

        return $http.post(
            '/rmq-admin/topic-mgmt/query', {
                draw: draw,
                start: start,
                limit: limit
            }
        );
    }

    function create(
        name,
        memo
        ) {
        return $http.post(
            '/rmq-admin/topic-mgmt/create', {
                name: name,
                memo: memo
            }
        )
    }
}

angular
    .module('app.topic-mgmt')
    .factory('topicMgmtService', topicMgmtService);
