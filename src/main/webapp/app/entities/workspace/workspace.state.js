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
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('workspace');
                        return $translate.refresh();
                    }]
                }
            })
            .state('workspace-news', {
                parent: 'app',
                url: '/workspace-news',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'content@':{
                        templateUrl: 'app/entities/workspace/workspace-news.html',
                        controller: 'WorkspaceNewsController',
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
            .state('workspace-question', {
                parent: 'entity',
                url: '/workspace-question',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'content@':{
                        templateUrl: 'app/entities/workspace/workspace-question.html',
                        controller: 'WorkspaceQuestionController',
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
            .state('workspace-answer', {
                parent: 'entity',
                url: '/workspace-answer',
                data: {
                    authorities: [],
                    pageTitle: "Workspace"
                },
                views:{
                    'content@':{
                        templateUrl: 'app/entities/workspace/workspace-answer.html',
                        controller: 'WorkspaceAnswerController',
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