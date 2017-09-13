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
            views: {
                'content@': {
                    templateUrl: 'app/account/login-signup/login-signup.html',
                    controller: 'RegisterController',
                    controllerAs: 'vm'
                }
            },
        });
    }
})();
