(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-info', {
            parent: 'entity',
            url: '/user-info',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackathonApp.userInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-info/user-infos.html',
                    controller: 'UserInfoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-info-detail', {
            parent: 'entity',
            url: '/user-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackathonApp.userInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-info/user-info-detail.html',
                    controller: 'UserInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserInfo', function($stateParams, UserInfo) {
                    return UserInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-info-detail.edit', {
            parent: 'user-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-info/user-info-dialog.html',
                    controller: 'UserInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserInfo', function(UserInfo) {
                            return UserInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-info.new', {
            parent: 'user-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-info/user-info-dialog.html',
                    controller: 'UserInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                introduction: null,
                                twitterUrl: null,
                                linkedInUrl: null,
                                websiteUrl: null,
                                skills: null,
                                workArea: null,
                                feedbackFrom: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-info', null, { reload: 'user-info' });
                }, function() {
                    $state.go('user-info');
                });
            }]
        })
        .state('user-info.edit', {
            parent: 'user-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-info/user-info-dialog.html',
                    controller: 'UserInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserInfo', function(UserInfo) {
                            return UserInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-info', null, { reload: 'user-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-info.delete', {
            parent: 'user-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-info/user-info-delete-dialog.html',
                    controller: 'UserInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserInfo', function(UserInfo) {
                            return UserInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-info', null, { reload: 'user-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
