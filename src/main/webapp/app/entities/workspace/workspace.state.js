(function(){
    'use strict'
    angular
        .module('hackathonApp')
        .config(stateConfig)

        stateConfig.$inject = ['$stateProvider'];

        function stateConfig($stateProvider){
            $stateProvider
            .state('workspace', {
                parent: 'entity',
                url: '/workspace/{challengeId}',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'content@':{
                        templateUrl: 'app/entities/workspace/workspace.html',
                        controller: 'WorkspaceController',
                        controllerAs: 'vm',
                    }
                },
                resolve:{
                    entity: ['WorkspaceOfChallenge', '$stateParams', function(WorkspaceOfChallenge, $stateParams){
                        return WorkspaceOfChallenge.get({challengeId: $stateParams.challengeId});
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('workspace');
                        return $translate.refresh();
                    }]
                }
            })

            .state('workspace-home', {
                parent: 'workspace',
                url: '/home',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'subcontent':{
                        templateUrl: 'app/entities/workspace/workspace-home.html',
                        controller: 'WorkspaceHomeController',
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
            
            .state('workspace-team', {
                parent: 'workspace',
                url: '/team',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'subcontent':{
                        templateUrl: 'app/entities/workspace/workspace-team.html',
                        controller: 'WorkspaceTeamController',
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

            .state('workspace-feedback', {
                parent: 'workspace',
                url: '/feedback',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'subcontent':{
                        templateUrl: 'app/entities/workspace/workspace-feedback.html',
                        controller: 'WorkspaceFeedbackController',
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

            .state('workspace-terms', {
                parent: 'workspace',
                url: '/terms',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'subcontent':{
                        templateUrl: 'app/entities/workspace/workspace-terms.html',
                        controller: 'WorkspaceTermsController',
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
            
            
        }
})();