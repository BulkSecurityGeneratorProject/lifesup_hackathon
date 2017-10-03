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
                url: '/applicationslist/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'hackathonApp.application.home.title'
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
                    }],
                    entity: ['$stateParams' ,'ApplicationsList', function($stateParams ,ApplicationsList){
                        return ApplicationsList.query({id:$stateParams.id}).$promise;
                    }]
                }
            })
            .state('applicationslist-detail', {
                parent: 'entity',
                url: '/applicationslist-detail/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'hackathonApp.application.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/applicationslist/applicationslist-detail.html',
                        controller: 'ApplicationsListDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('application');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ApplicationsListDetails', function ($stateParams, ApplicationsListDetails) {
                        return ApplicationsListDetails.query({id: $stateParams.id}).$promise;
                    }]
                }
            })
    }

})();
