(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('login-signup', {
            parent: 'app',
            url: '/login',
            data: {
                authorities: [],
                pageTitle: "loginsignup.title"
            },
            views: {
                'content@': {
                    templateUrl: 'app/components/login-signup/login-signup.html',
                    controller: 'LoginSignupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('loginsignup');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
