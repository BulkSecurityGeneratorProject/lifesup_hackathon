(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('Challenge', Challenge);

    Challenge.$inject = ['$resource'];

    function Challenge ($resource) {
        var resourceUrl =  'api/challenges/:id';

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
})();
