(function () {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('UserInfo', UserInfo)
        .factory('UserDetail', UserDetail)

    UserInfo.$inject = ['$resource'];
    UserDetail.$inject = ['$resource', 'DateUtils'];


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
                    data.userInfo.birthday = DateUtils.convertDateTimeFromServer(data.userInfo.birthday);
                    return data;
                }
            },
            'update': { method: 'PUT' }
        });

        return service;
    }
})();
