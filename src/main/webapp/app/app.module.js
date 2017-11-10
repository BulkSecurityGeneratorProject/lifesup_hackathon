(function () {
    'use strict';

    angular
        .module('hackathonApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'ngMaterial',
            'ngMessages',
            'lfNgMdFileInput',
            'naif.base64',
            'ngAnimate',
            'md.data.table',
            'timer'
        ]).config(['$mdDateLocaleProvider', function ($mdDateLocaleProvider) {
         
        }]  )
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
