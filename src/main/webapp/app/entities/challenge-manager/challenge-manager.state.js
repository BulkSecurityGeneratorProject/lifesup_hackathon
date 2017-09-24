(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('challenge-manager', {
                parent: 'entity',
                url: '/challenge-manager',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'hackathonApp.challenge.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/challenge-manager/challenge-manager.html',
                        controller: 'ChallengeManagerController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('challenge-manager');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('challenge-manager-detail', {
                parent: 'entity',
                url: '/challenge-manager/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'hackathonApp.challenge.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/challenge-manager/challenge-manager-detail.html',
                        controller: 'ChallengeManagerDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('challenge-manager');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Challenge', function ($stateParams, Challenge) {
                        return Challenge.get({ id: $stateParams.id }).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'challenge-manager',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('challenge-manager-detail.edit', {
                parent: 'challenge-manager-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$mdDialog', function ($stateParams, $state, $uibModal, $mdDialog) {
                    $mdDialog.show({
                        templateUrl: 'app/entities/challenge-manager/challenge-manager-dialog.html',
                        controller: 'ChallengeManagerDialogController',
                        controllerAs: 'vm',
                        size: 'lg',
                        clickOutsideToClose: true,
                        fullscreen: true,
                        resolve: {
                            entity: ['Challenge', function (Challenge) {
                                return Challenge.get({ id: $stateParams.id }).$promise;
                            }]
                        }
                    }).then(function () {
                        $state.go('challenge-manager-detail', null, { reload: 'challenge-manager-detail' });
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('challenge-manager.new', {
                parent: 'challenge-manager',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$mdDialog', function ($stateParams, $state, $uibModal, $mdDialog) {
                    $mdDialog.show({
                        templateUrl: 'app/entities/challenge-manager/challenge-manager-dialog.html',
                        controller: 'ChallengeManagerDialogController',
                        controllerAs: 'vm',
                        clickOutsideToClose: true,
                        fullscreen: true,
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
                    }).then(function () {
                        $state.go('challenge-manager', null, { reload: 'challenge-manager' });
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('challenge-manager.edit', {
                parent: 'challenge-manager',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$mdDialog', function ($stateParams, $state, $uibModal, $mdDialog) {
                    $mdDialog.show({
                        templateUrl: 'app/entities/challenge-manager/challenge-manager-dialog.html',
                        controller: 'ChallengeManagerDialogController',
                        controllerAs: 'vm',
                        size: 'lg',
                        clickOutsideToClose: true,
                        fullscreen: true,
                        resolve: {
                            entity: ['Challenge', function (Challenge) {
                                var temp = Challenge.get({ id: $stateParams.id }).$promise;
                                return Challenge.get({ id: $stateParams.id }).$promise;
                            }]
                        }
                    }).then(function () {
                        $state.go('challenge', null, { reload: 'challenge' });
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('challenge-manager.delete', {
                parent: 'challenge-manager',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$mdDialog', function ($stateParams, $state, $uibModal, $mdDialog, ev) {
                    $mdDialog.show({
                        templateUrl: 'app/entities/challenge-manager/challenge-manager-delete-dialog.html',
                        controller: 'ChallengeManagerDeleteController',
                        controllerAs: 'vm',
                        clickOutsideToClose: true,
                        targetEvent: ev,
                        resolve: {
                            entity: ['Challenge', function (Challenge) {
                                return Challenge.get({ id: $stateParams.id }).$promise;
                            }]
                        }
                    }).then(function () {
                        $state.go('challenge-manager', null, { reload: 'challenge-manager' });
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
