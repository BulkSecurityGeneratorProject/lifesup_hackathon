(function () {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('Challenge', Challenge)
        .factory('ChallengeDetails', ChallengeDetails)
        .factory('ApplicationsByUser', ApplicationsByUser)
        .factory('ChallengeByUser', ChallengeByUser);
        

    ChallengeDetails.$inject = ['$resource', 'DateUtils'];
    Challenge.$inject = ['$resource'];
    ApplicationsByUser.$inject = ['$resource'];
    ChallengeByUser.$inject = ['$resource'];

    function Challenge($resource) {
        var resourceUrl = '/api/challenges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method: 'PUT' },
        });
    }

    function ChallengeDetails($resource, DateUtils) {
        var resourceUrl = '/api/challenges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true },
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

    function ApplicationsByUser($resource) {
        var resourceUrl = '/api/challenge-user-applications/current-user';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true },
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

    function ChallengeByUser($resource) {
        var resourceUrl = '/api/challenges-by-user';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true },
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
