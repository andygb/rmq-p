/**
 * app.core
 */
(function () {

    'use strict';

    angular.module('app.core', [
        'ui.router', // Routing
        'oc.lazyLoad', // ocLazyLoad
        'ui.bootstrap', // Ui Bootstrap
        'pascalprecht.translate', // Angular Translate
        'ngIdle', // Idle timer
        'ngSanitize', // ngSanitize
        'ngCookies', //ngCookies
        'toaster', // toaster
        'datatables'
    ])
    .run(function(DTDefaultOptions) {
        DTDefaultOptions.setLanguage({
            sUrl: 'js/plugins/dataTables/Chinese.json'
        });
        DTDefaultOptions.setDisplayLength(10);
    });

})();