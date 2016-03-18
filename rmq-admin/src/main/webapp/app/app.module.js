/**
 * Admin - 管理后台
 */
(function () {

    'use strict';

    angular.module('app', [
        'app.core',
        'app.topic-mgmt',
        'app.record'
    ]);

})();

// Other libraries are loaded dynamically in the config.js file using the library ocLazyLoad