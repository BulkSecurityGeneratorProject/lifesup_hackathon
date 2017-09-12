(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('challenge-info', {
            parent: 'entity',
            url: '/challenge-info',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackathonApp.challengeInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/challenge-info/challenge-infos.html',
                    controller: 'ChallengeInfoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('challengeInfo');
                    $translatePartialLoader.addPart('challengeStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('challenge-info-detail', {
            parent: 'entity',
            url: '/challenge-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackathonApp.challengeInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/challenge-info/challenge-info-detail.html',
                    controller: 'ChallengeInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('challengeInfo');
                    $translatePartialLoader.addPart('challengeStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ChallengeInfo', function($stateParams, ChallengeInfo) {
                    return ChallengeInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'challenge-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('challenge-info-detail.edit', {
            parent: 'challenge-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge-info/challenge-info-dialog.html',
                    controller: 'ChallengeInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChallengeInfo', function(ChallengeInfo) {
                            return ChallengeInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('challenge-info.new', {
            parent: 'challenge-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge-info/challenge-info-dialog.html',
                    controller: 'ChallengeInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                eventStartTime: null,
                                eventEndTime: null,
                                location: null,
                                status: null,
                                prize: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('challenge-info', null, { reload: 'challenge-info' });
                }, function() {
                    $state.go('challenge-info');
                });
            }]
        })
        .state('challenge-info.edit', {
            parent: 'challenge-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge-info/challenge-info-dialog.html',
                    controller: 'ChallengeInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChallengeInfo', function(ChallengeInfo) {
                            return ChallengeInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('challenge-info', null, { reload: 'challenge-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('challenge-info.delete', {
            parent: 'challenge-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/challenge-info/challenge-info-delete-dialog.html',
                    controller: 'ChallengeInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChallengeInfo', function(ChallengeInfo) {
                            return ChallengeInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('challenge-info', null, { reload: 'challenge-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
