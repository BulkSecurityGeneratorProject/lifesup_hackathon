(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('ApplicationsList', ApplicationsList)
        .factory('ApplicationsListDetails', ApplicationsListDetails)

    ApplicationsList.$inject = ['$resource'];
    ApplicationsListDetails.$inject = ['$resource'];

    function ApplicationsList ($resource) {
        var resourceUrl =  'api/applications/challenges/:id';

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
        var resourceUrl =  '/api/applications/details/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
