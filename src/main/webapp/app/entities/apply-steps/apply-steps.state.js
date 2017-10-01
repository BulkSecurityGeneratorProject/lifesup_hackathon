(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('complete-profile', {
                parent: 'challengeslist',
                url: '/{id}/complete-profile',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'global.menu.account.settings'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/apply-steps/profile.html',
                        controller: 'CompleteProfileController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['UserDetail', function (UserDetail) {
                        return UserDetail.get().$promise;
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('settings');
                        return $translate.refresh();
                    }]
                }
            })
            .state('team', {
                parent: 'complete-profile',
                url: '/team',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'hackathonApp.application.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/apply-steps/team.html',
                        controller: 'TeamController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'UserApplicationByChallengeID', function ($stateParams, UserApplicationByChallengeID) {
                        return UserApplicationByChallengeID.get({ challengeId: $stateParams.id })
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('application');
                        $translatePartialLoader.addPart('applicationStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('success', {
                parent: 'team',
                url: '/success',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'hackathonApp.application.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/apply-steps/success.html',
                        controller: 'SuccessController',
                        controllerAs: 'vm'
                    }
                }
            })
    }
})();
