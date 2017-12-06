(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('challengeslist', {
                parent: 'entity',
                url: '/challengeslist',
                data: {
                    authorities: [],
                    pageTitle: 'hackathonApp.challengeslist.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/challengeslist/challengeslist.html',
                        controller: 'ChallengesListController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('challengeslist');
                        $translatePartialLoader.addPart('challenge-manager');
                        return $translate.refresh();
                    }]
                }
            })
            
            .state('challengeslist-detail', {
                parent: 'entity',
                url: '/challengeslist/{id}',
                data: {
                    authorities: [],
                    pageTitle: 'hackathonApp.challengesDetails.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/challengeslist/challengeslist-detail.html',
                        controller: 'ChallengesListDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('challengeslist');
                        $translatePartialLoader.addPart('challenge-manager');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Challenge', function ($stateParams, Challenge) {
                        return Challenge.get({ id: $stateParams.id }).$promise;
                    }]
                }
            })

            .state('challengeslist-team', {
                parent: 'challengeslist-detail',
                url: '/team/{applicationId}',
                data: {
                    authorities: []
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$mdDialog', function ($stateParams, $state, $uibModal, $mdDialog, ev) {
                    $mdDialog.show({
                        templateUrl: 'app/entities/workspace/workspace-team.html',
                        controller: 'WorkspaceTeamController',
                        controllerAs: 'vm',
                        clickOutsideToClose: true,
                        targetEvent: ev,
                        resolve: {
                            entity: ['$stateParams', 'ApplicationsListDetails', function ($stateParams, ApplicationsListDetails) {
                                return ApplicationsListDetails.query({id: $stateParams.applicationId}).$promise;
                            }],
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader){
                                $translatePartialLoader.addPart('global');
                                $translatePartialLoader.addPart('applicationslist');
                                // $translatePartialLoader.addPart('workspace');
                                return $translate.refresh();
                            }]
                        }
                    }).then(function () {
                        $state.go($state.current.parent);
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
