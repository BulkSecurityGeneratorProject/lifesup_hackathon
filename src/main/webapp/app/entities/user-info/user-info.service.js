(function () {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('UserInfo', UserInfo)
        .factory('UserDetail', UserDetail)
        .factory('UserLogo', UserLogo)

    UserInfo.$inject = ['$resource'];
    UserDetail.$inject = ['$resource', 'DateUtils'];
    UserLogo.$inject = ['$resource'];


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
