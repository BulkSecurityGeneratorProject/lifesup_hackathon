(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .factory('User', User)
        .factory('DeleteUserByID', DeleteUserByID)
        .factory('GetVisibleUsers', GetVisibleUsers)
        .factory('UserInfo', UserInfo)
        .factory('UserDetail', UserDetail)
        .factory('UserLogo', UserLogo);

    User.$inject = ['$resource'];
    DeleteUserByID.$inject = ['$resource'];
    GetVisibleUsers.$inject = ['$resource'];
    UserInfo.$inject = ['$resource'];
    UserDetail.$inject = ['$resource', 'DateUtils'];
    UserLogo.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });
        return service;
    }

    function DeleteUserByID ($resource) {
        var service = $resource('api/delete-users/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });
        return service;
    }

    function GetVisibleUsers ($resource) {
        var service = $resource('api/list-user', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });
        return service;
    }

    function UserInfo($resource) {
        var resourceUrl = 'api/user-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method: 'PUT' }
        });
    }
    
    function UserDetail($resource, DateUtils) {
        var service = $resource('api/user-detail', {}, {
            'query': { method: 'GET', isArray: true },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method: 'PUT' }
        });

        return service;
    }
    function UserLogo($resource) {
        var service = $resource('api/user-info/banner', {}, {
            'query': { method: 'GET', isArray: true },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method: 'PUT' }
        });

        return service;
    }
})();
