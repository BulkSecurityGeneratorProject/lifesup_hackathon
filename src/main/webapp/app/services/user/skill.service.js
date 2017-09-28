(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .factory('Skill', Skill);

    Skill.$inject = ['$resource'];

    function Skill ($resource) {
        var service = $resource('api/skills', {}, {
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
