(function(){
    'use strict'
    angular
        .module('hackathonApp')
        .config(stateConfig)

        stateConfig.$inject = ['$stateProvider'];

        function stateConfig($stateProvider){
            $stateProvider
            .state('workspace-manager', {
                parent: 'challenge-manager',
                url: '/{challengeId}/workspace-manager',
                data: {
                    authorities: [],
                    pageTitle: "workspace-manager"
                },
                views:{
                    'content@':{
                        templateUrl: 'app/entities/workspace-manager/workspace-manager.html',
                           controller: 'WorkspaceManagerController',
                        controllerAs: 'vm',
                    }
                },
                resolve:{
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('workspace');
                        return $translate.refresh();
                    }]
                }
            })

            .state('workspace-manager.new', {
                parent: 'challenge-manager',
                url: '/{challengeId}/new-workspace',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_HOST']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$mdDialog', function ($stateParams, $state, $uibModal, $mdDialog) {
                    $mdDialog.show({
                        templateUrl: 'app/entities/workspace-manager/workspace-manager-dialog.html',
                        controller: 'WorkspaceManagerDialogController',
                        controllerAs: 'vm',
                        fullscreen: true,
                        resolve: {
                            entity: function () {
                                return {
                                    id: null,
                                    termsAndConditions: null,
                                    challengeId: null,
                                };
                            },
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                                $translatePartialLoader.addPart('global');
                                $translatePartialLoader.addPart('workspace');
                                return $translate.refresh();
                            }]
                        }
                    }).then(function () {
                        $state.go('challenge-manager', null, { reload: 'challenge-manager' });
                    }, function () {
                        $state.go('challenge-manager');
                    });
                }]
            })
            .state('workspace-manager.edit', {
                parent: 'workspace-manager',
                url: '/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_HOST']
                },
                onEnter: ['$stateParams', '$state', 'WorkspaceOfChallenge', '$mdDialog', function ($stateParams, $state, WorkspaceOfChallenge, $mdDialog) {
                    $mdDialog.show({
                        templateUrl: 'app/entities/workspace-manager/workspace-manager-dialog.html',
                        controller: 'WorkspaceManagerDialogController',
                        controllerAs: 'vm',
                        fullscreen: true,
                        resolve: {
                            entity: ['WorkspaceOfChallenge', '$stateParams', function(WorkspaceOfChallenge, $stateParams){
                                return WorkspaceOfChallenge.get({challengeId: $stateParams.challengeId}).$promise;
                            }],
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                                $translatePartialLoader.addPart('global');
                                $translatePartialLoader.addPart('workspace');
                                return $translate.refresh();
                            }]
                        }
                    }).then(function () {
                        $state.go('workspace-manager', null, { reload: 'workspace-manager' });
                    }, function () {
                        $state.go('workspace-manager');
                    });
                }]
            })

            .state('workspace-manager-news', {
                parent: 'app',
                url: '/workspace-manager-news',
                data: {
                    authorities: [],
                    pageTitle: "workspace-manager"
                },
                views:{
                    'content@':{
                        templateUrl: 'app/entities/workspace-manager/workspace-manager-news.html',
                        controller: 'WorkspaceManagerNewsController',
                        controllerAs: 'vm',
                    }
                },
                resolve:{
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('workspace');
                        return $translate.refresh();
                    }]
                }
            })

            .state('workspace-manager-answer', {
                parent: 'entity',
                url: '/workspace-manager-answer',
                data: {
                    authorities: [],
                    pageTitle: "workspace-manager"
                },
                views:{
                    'content@':{
                        templateUrl: 'app/entities/workspace-manager/workspace-manager-answer.html',
                        controller: 'WorkspaceManagerAnswerController',
                        controllerAs: 'vm',
                    }
                },
                resolve:{
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('workspace');
                        return $translate.refresh();
                    }]
                }
            })
        }
})();