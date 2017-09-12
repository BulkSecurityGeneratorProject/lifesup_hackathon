(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('ChallengeUserApplication', ChallengeUserApplication);

    ChallengeUserApplication.$inject = ['$resource'];

    function ChallengeUserApplication ($resource) {
        var resourceUrl =  'api/challenge-user-applications/:id';

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
