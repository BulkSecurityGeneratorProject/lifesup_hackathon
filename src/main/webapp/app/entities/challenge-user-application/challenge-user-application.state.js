(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('challenge-user-application', {
            parent: 'entity',
            url: '/challenge-user-application',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackathonApp.challengeUserApplication.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/challenge-user-application/challenge-user-applications.html',
                    controller: 'ChallengeUserApplicationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('challengeUserApplication');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('challenge-user-application-detail', {
            parent: 'entity',
            url: '/challenge-user-application/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackathonApp.challengeUserApplication.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/challenge-user-application/challenge-user-application-detail.html',
                    controller: 'ChallengeUserApplicationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('challengeUserApplication');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ChallengeUserApplication', function($stateParams, ChallengeUserApplication) {
                    return ChallengeUserApplication.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'challenge-user-application',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('challenge-user-application-detail.edit', {
            parent: 'challenge-user-application-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge-user-application/challenge-user-application-dialog.html',
                    controller: 'ChallengeUserApplicationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChallengeUserApplication', function(ChallengeUserApplication) {
                            return ChallengeUserApplication.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('challenge-user-application.new', {
            parent: 'challenge-user-application',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge-user-application/challenge-user-application-dialog.html',
                    controller: 'ChallengeUserApplicationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                challengeId: null,
                                userId: null,
                                applicationId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('challenge-user-application', null, { reload: 'challenge-user-application' });
                }, function() {
                    $state.go('challenge-user-application');
                });
            }]
        })
        .state('challenge-user-application.edit', {
            parent: 'challenge-user-application',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge-user-application/challenge-user-application-dialog.html',
                    controller: 'ChallengeUserApplicationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChallengeUserApplication', function(ChallengeUserApplication) {
                            return ChallengeUserApplication.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('challenge-user-application', null, { reload: 'challenge-user-application' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('challenge-user-application.delete', {
            parent: 'challenge-user-application',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge-user-application/challenge-user-application-delete-dialog.html',
                    controller: 'ChallengeUserApplicationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChallengeUserApplication', function(ChallengeUserApplication) {
                            return ChallengeUserApplication.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('challenge-user-application', null, { reload: 'challenge-user-application' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
