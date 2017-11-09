(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('HostRequest', HostRequest);

    HostRequest.$inject = ['$resource'];

    function HostRequest ($resource) {
        var resourceUrl =  'api/companies/send-mail';

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
