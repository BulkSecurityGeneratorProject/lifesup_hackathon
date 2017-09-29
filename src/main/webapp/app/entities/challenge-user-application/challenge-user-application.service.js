(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('ChallengeUserApplication', ChallengeUserApplication)
        .factory('UserApplicationByChallengeID', UserApplicationByChallengeID);

    ChallengeUserApplication.$inject = ['$resource'];
    UserApplicationByChallengeID.$inject = ['$resource'];

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
    function UserApplicationByChallengeID ($resource) {
        var resourceUrl =  'api/challenge-user-applications/challenge/:challengeId';

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
