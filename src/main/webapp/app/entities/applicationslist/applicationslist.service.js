(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('ApplicationsList', ApplicationsList)
        .factory('ApplicationsListDetails', ApplicationsListDetails)
        .factory('UserAccount', UserAccount);

    ApplicationsList.$inject = ['$resource'];
    ApplicationsListDetails.$inject = ['$resource'];
    UserAccount.$inject = ['$resource'];

    function ApplicationsList ($resource) {
        var resourceUrl =  'api/applications/challenges/:challengeId';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }

    function ApplicationsListDetails ($resource) {
        var resourceUrl =  'api/applications/challenges/:challengeId';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT', params: {id: '@id'} }
        });
    }

    function UserAccount ($resource) {
        var resourceUrl = "api/account";

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: false},
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
})();
