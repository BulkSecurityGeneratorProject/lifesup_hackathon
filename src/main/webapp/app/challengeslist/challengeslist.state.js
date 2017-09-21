(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('challengeslist', {
            parent: 'app',
            url: '/challengeslist',
            data: {
                authorities: [],
                title: "challengeslist.title"
            },
            views: {
                'content@': {
                    templateUrl: 'app/challengeslist/challengeslist.html',
                    controller: 'ChallengesListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('challengeslist');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
