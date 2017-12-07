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
                url: '/workspace-manager/cid={challengeId}',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'content@':{
                        templateUrl: 'app/entities/workspace-manager/workspace-manager.html',
                        controller: 'WorkspaceManagerController',
                        controllerAs: 'vm',
                    }
                },
                resolve:{
                    entity: ['WorkspaceOfChallenge', '$stateParams', function(WorkspaceOfChallenge, $stateParams){
                        return WorkspaceOfChallenge.get({challengeId: $stateParams.challengeId});
                    }],
                    application: ['ApplicationByChallengeId', '$stateParams', function(ApplicationByChallengeId, $stateParams){
                        return ApplicationByChallengeId.get({challengeId: $stateParams.challengeId});
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('workspace');
                        return $translate.refresh();
                    }]
                }
            })

            .state('workspace-manager-home', {
                parent: 'workspace-manager',
                url: '/home',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'subcontent':{
                        templateUrl: 'app/entities/workspace-manager/workspace-manager-home.html',
                        controller: 'WorkspaceManagerHomeController',
                        controllerAs: 'vm',
                    }
                },
                resolve:{
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                        $translatePartialLoader.addPart('global');
                        // $translatePartialLoader.addPart('workspace');
                        return $translate.refresh();
                    }]
                }
            })
            
            .state('workspace-manager-feedback', {
                parent: 'workspace-manager',
                url: '/feedback',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'subcontent':{
                        templateUrl: 'app/entities/workspace-manager/workspace-manager-feedback.html',
                        controller: 'WorkspaceManagerFeedbackController',
                        controllerAs: 'vm',
                    }
                },
                resolve:{
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                        $translatePartialLoader.addPart('global');
                        // $translatePartialLoader.addPart('workspace');
                        return $translate.refresh();
                    }]
                }
            })

            .state('workspace-manager-terms', {
                parent: 'workspace-manager',
                url: '/terms',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'subcontent':{
                        templateUrl: 'app/entities/workspace-manager/workspace-manager-terms.html',
                        controller: 'WorkspaceManagerTermsController',
                        controllerAs: 'vm',
                    }
                },
                resolve:{
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                        $translatePartialLoader.addPart('global');
                        // $translatePartialLoader.addPart('workspace');
                        return $translate.refresh();
                    }]
                }
            })

            .state('workspace-manager-submit', {
                parent: 'workspace-manager',
                url: '/submit',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'subcontent':{
                        templateUrl: 'app/entities/workspace-manager/workspace-manager-submit.html',
                        controller: 'WorkspaceManagerSubmitController',
                        controllerAs: 'vm',
                    }
                },
                resolve:{
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                        $translatePartialLoader.addPart('global');
                        // $translatePartialLoader.addPart('workspace');
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
                parent: 'workspace-manager-terms',
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
                        $state.go('workspace-manager-terms', null, { reload: true });
                    }, function () {
                        $state.go('workspace-manager-terms');
                    });
                }]
            })
            
            
        }
})();