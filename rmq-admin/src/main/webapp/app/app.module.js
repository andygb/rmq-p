/**
 * Admin - 管理后台
 */
(function () {

    'use strict';

    angular.module('app', [
        'app.core',
        'app.user-mgmt',
        'app.message-mgmt',
        'app.trade-mgmt',
        'app.coupon-mgmt',
        'app.shop-mgmt',
        'app.trade-mgmt',
        'app.dic-mgmt'
    ]);

})();

// Other libraries are loaded dynamically in the config.js file using the library ocLazyLoad