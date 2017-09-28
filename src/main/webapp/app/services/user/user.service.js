(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .factory('User', User)
        .factory('DeleteUserByID', DeleteUserByID)
        .factory('GetVisibleUsers', GetVisibleUsers);

    User.$inject = ['$resource'];
    DeleteUserByID.$inject = ['$resource'];
    GetVisibleUsers.$inject = ['$resource'];

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
})();
