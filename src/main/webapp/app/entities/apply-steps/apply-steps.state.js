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
                parent: 'challengeslist',
                url: '/{id}/team',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'hackathonApp.application.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/apply-steps/team-application.html',
                        controller: 'TeamController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('application');
                        $translatePartialLoader.addPart('applicationStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('success', {
                parent: 'challengeslist',
                url: '/{id}/success',
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
            .state('invitation', {
                parent: 'app',
                url: '/{id}/invitation',
                data: {
                    authorities: [],
                    pageTitle: 'hackathonApp.application.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/apply-steps/team-invite.html',
                        controller: 'TeamInviteController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('loginsignup');
                        $translatePartialLoader.addPart('login');
                        return $translate.refresh();
                    }]
                }
            })
    }
})();
