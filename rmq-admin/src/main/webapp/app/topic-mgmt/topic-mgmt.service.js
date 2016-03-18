/**
 * Message Management service
 */

'use strict';

topicMgmtService.$inject = [ '$http', '$rootScope' ];

function topicMgmtService($http, $rootScope) {
    return {
        query: query,
        create: create,
        produce: produce
    };

    function query(
        draw,
        start,
        limit
        ) {

        return $http.post(
            'topic-mgmt/query', {
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
            'topic-mgmt/create', {
                name: name,
                memo: memo
            }
        )
    }

    function produce(
        topic,
        content
        ) {
        return $http.post(
            'topic-mgmt/produce', {
                topic: topic,
                content: content
            }
        )
    }
}

angular
    .module('app.topic-mgmt')
    .factory('topicMgmtService', topicMgmtService);
