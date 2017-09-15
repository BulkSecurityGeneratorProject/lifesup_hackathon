(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('challenges', {
            parent: 'app',
            url: '/challenges',
            data: {
                authorities: [],
                pageTitle: ''
            },
            views: {
                'content@': {
                    templateUrl: 'app/challenges/challenges.html',
                    controller: 'ChallengesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('challenges');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
