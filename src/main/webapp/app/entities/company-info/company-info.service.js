(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('CompanyInfo', CompanyInfo);

    CompanyInfo.$inject = ['$resource'];

    function CompanyInfo ($resource) {
        var resourceUrl =  'api/companies/user';

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
