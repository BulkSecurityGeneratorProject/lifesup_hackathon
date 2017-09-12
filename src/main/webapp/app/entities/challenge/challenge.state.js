(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('challenge', {
            parent: 'entity',
            url: '/challenge',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackathonApp.challenge.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/challenge/challenges.html',
                    controller: 'ChallengeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('challenge');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('challenge-detail', {
            parent: 'entity',
            url: '/challenge/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackathonApp.challenge.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/challenge/challenge-detail.html',
                    controller: 'ChallengeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('challenge');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Challenge', function($stateParams, Challenge) {
                    return Challenge.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'challenge',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('challenge-detail.edit', {
            parent: 'challenge-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge/challenge-dialog.html',
                    controller: 'ChallengeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Challenge', function(Challenge) {
                            return Challenge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('challenge.new', {
            parent: 'challenge',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge/challenge-dialog.html',
                    controller: 'ChallengeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                introduction: null,
                                challengeText: null,
                                resourceText: null,
                                rewardsText: null,
                                timelineText: null,
                                rulesText: null,
                                bannerUrl: null,
                                additionalText: null,
                                maxTeamNumber: null,
                                minTeamNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('challenge', null, { reload: 'challenge' });
                }, function() {
                    $state.go('challenge');
                });
            }]
        })
        .state('challenge.edit', {
            parent: 'challenge',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge/challenge-dialog.html',
                    controller: 'ChallengeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Challenge', function(Challenge) {
                            return Challenge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('challenge', null, { reload: 'challenge' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('challenge.delete', {
            parent: 'challenge',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge/challenge-delete-dialog.html',
                    controller: 'ChallengeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Challenge', function(Challenge) {
                            return Challenge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('challenge', null, { reload: 'challenge' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
