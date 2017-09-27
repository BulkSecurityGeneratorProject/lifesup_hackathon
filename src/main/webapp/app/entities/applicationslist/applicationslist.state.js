(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('applicationslist', {
                parent: 'entity',
                url: '/applicationslist',
                data: {
                    authorities: [],
                    pageTitle: 'hackathonApp.challenge.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/applicationslist/applicationslist.html',
                        controller: 'ApplicationsListController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('application');
                        return $translate.refresh();
                    }]
                }
            })
            // .state('applicationslist-detail', {
            //     parent: 'entity',
            //     url: '/applicationslist/{id}',
            //     data: {
            //         authorities: [],
            //         pageTitle: 'hackathonApp.challenge.detail.title'
            //     },
            //     views: {
            //         'content@': {
            //             templateUrl: 'app/entities/applicationslist/applicationslist-detail.html',
            //             controller: 'ApplicationsListDetailController',
            //             controllerAs: 'vm'
            //         }
            //     },
            //     resolve: {
            //         translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            //             $translatePartialLoader.addPart('applicationslist');
            //             $translatePartialLoader.addPart('application');
            //             return $translate.refresh();
            //         }],
            //         entity: ['$stateParams', 'Challenge', function ($stateParams, Challenge) {
            //             return Challenge.get({ id: $stateParams.id }).$promise;
            //         }],
            //         challenge: ['$http', '$stateParams', function($http, $stateParams) {
            //          $http({
            //                 url: 'i18n/en/applicationslist.json',
            //                 method: 'GET',
            //                 params: {id: $stateParams.id}
            //             })
            //                 .then(function(response) {
            //                     return response.data;
            //                 })
            //         }]
            //     }
            // })
    }

})();
