(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('challengeslist', {
                parent: 'entity',
                url: '/challengeslist',
                data: {
                    authorities: [],
                    pageTitle: 'hackathonApp.challenge.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/challengeslist/challengeslist.html',
                        controller: 'ChallengesListController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('challenge');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('challengeslist');
                        return $translate.refresh();
                    }]
                }
            })
            .state('challengeslist-detail', {
                parent: 'entity',
                url: '/challengeslist/{id}',
                data: {
                    authorities: [],
                    pageTitle: 'hackathonApp.challenge.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/challengeslist/challengeslist-detail.html',
                        controller: 'ChallengesListDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('challengeslist');
                        $translatePartialLoader.addPart('challenge');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Challenge', function ($stateParams, Challenge) {
                        return Challenge.get({ id: $stateParams.id }).$promise;
                    }],
                    challenge: ['$http', '$stateParams', function($http, $stateParams) {
                     $http({
                            url: 'i18n/en/challengeslist.json',
                            method: 'GET',
                            params: {id: $stateParams.id}
                        })
                            .then(function(response) {
                                return response.data;
                            })
                    }]
                }
            })
    }

})();
