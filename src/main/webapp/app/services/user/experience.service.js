(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .factory('Experience', Experience);

    Experience.$inject = ['$resource'];

    function Experience ($resource) {
        var service = $resource('api/experiences', {}, {
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
