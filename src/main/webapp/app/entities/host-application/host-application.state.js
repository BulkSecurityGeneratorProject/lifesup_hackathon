(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('host-application', {
            parent: 'app',
            url: '/host-application',
            data: {
                authorities: [],
                pageTitle: "Become a Host"
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/host-application/host-application.html',
                    controller: 'HostApplicationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('host-application');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
