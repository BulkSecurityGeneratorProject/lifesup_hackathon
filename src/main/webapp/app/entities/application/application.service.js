(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('Application', Application)
        .factory('ApplicationByChallenge', ApplicationByChallenge)
        .factory('ApplicationByCurrentUser', ApplicationByCurrentUser)
        .factory('ApplicationByAcceptKey', ApplicationByAcceptKey)
        .factory('ApplicationBasicInfo', ApplicationBasicInfo);

    Application.$inject = ['$resource'];
    ApplicationByChallenge.$inject = ['$resource'];
    ApplicationByCurrentUser.$inject = ['$resource'];
    ApplicationByAcceptKey.$inject = ['$resource'];
    ApplicationBasicInfo.$inject = ['$resource'];

    function Application ($resource) {
        var resourceUrl =  'api/applications/:id';

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
    function ApplicationByChallenge ($resource) {
        var resourceUrl =  'api/applications/challenges/:challengeId';

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
    function ApplicationByCurrentUser ($resource) {
        var resourceUrl =  'api/challenge-user-applications/current-user';

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

    function ApplicationByAcceptKey ($resource) {
        var resourceUrl =  'api/application-invite-emails/acceptkey/:acceptkey';

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

    function ApplicationBasicInfo ($resource) {
        var resourceUrl =  '/api/applications/basics/:applicationId';
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
