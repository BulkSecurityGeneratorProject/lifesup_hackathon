(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('login-signup', {
            parent: 'account',
            url: '/login-signup',
            data: {
                authorities: [],
            },
            views: {
                'content@': {
                    templateUrl: 'app/login-signup/login-signup.html',
                    controller: 'LoginSignupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('register');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
