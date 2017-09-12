(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('UserList', UserList);

    UserList.$inject = ['$resource', 'DateUtils'];

    function UserList ($resource, DateUtils) {
        var resourceUrl =  'api/user-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birthday = DateUtils.convertDateTimeFromServer(data.birthday);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
