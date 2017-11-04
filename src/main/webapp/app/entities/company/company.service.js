(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('Company', Company)
        .factory('CompanyName', CompanyName);

    Company.$inject = ['$resource'];
    CompanyName.$inject = ['$resource'];

    function Company ($resource) {
        var resourceUrl =  'api/companies/:id';

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

    function CompanyName ($resource) {
        var resourceUrl =  'api/companies/name';

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
            }
        });
    }

})();
