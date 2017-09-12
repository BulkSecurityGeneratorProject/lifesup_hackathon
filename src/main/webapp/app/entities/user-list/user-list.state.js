(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-list', {
            parent: 'entity',
            url: '/user-list',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackathonApp.userList.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-list/user-lists.html',
                    controller: 'UserListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userList');
                    $translatePartialLoader.addPart('userSex');
                    $translatePartialLoader.addPart('userStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-list-detail', {
            parent: 'entity',
            url: '/user-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackathonApp.userList.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-list/user-list-detail.html',
                    controller: 'UserListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userList');
                    $translatePartialLoader.addPart('userSex');
                    $translatePartialLoader.addPart('userStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserList', function($stateParams, UserList) {
                    return UserList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-list-detail.edit', {
            parent: 'user-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-list/user-list-dialog.html',
                    controller: 'UserListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserList', function(UserList) {
                            return UserList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-list.new', {
            parent: 'user-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-list/user-list-dialog.html',
                    controller: 'UserListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                email: null,
                                password: null,
                                fullName: null,
                                phone: null,
                                sex: null,
                                companyName: null,
                                jobTitle: null,
                                logoUrl: null,
                                country: null,
                                city: null,
                                nationality: null,
                                birthday: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-list', null, { reload: 'user-list' });
                }, function() {
                    $state.go('user-list');
                });
            }]
        })
        .state('user-list.edit', {
            parent: 'user-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-list/user-list-dialog.html',
                    controller: 'UserListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserList', function(UserList) {
                            return UserList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-list', null, { reload: 'user-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-list.delete', {
            parent: 'user-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-list/user-list-delete-dialog.html',
                    controller: 'UserListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserList', function(UserList) {
                            return UserList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-list', null, { reload: 'user-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
