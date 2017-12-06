(function () {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('ChallengeManager', ChallengeManager)
        .factory('ChallengeByAuthority', ChallengeByAuthority)
        .factory('ChallengeInfo', ChallengeInfo)
        .factory('ChallengeBanner', ChallengeBanner)
        .factory('TimeServer', TimeServer)
        .factory('ApprovedApplication', ApprovedApplication)
        .factory('ChallengeResult', ChallengeResult)
        .factory('ChallengeResultById', ChallengeResultById)


    ChallengeManager.$inject = ['$resource'];
    ChallengeByAuthority.$inject = ['$resource'];
    ChallengeInfo.$inject = ['$resource', 'DateUtils'];
    ChallengeBanner.$inject = ['$resource'];
    TimeServer.$inject = ['$resource', 'DateUtils'];
    ApprovedApplication.$inject = ['$resource', 'DateUtils'];
    ChallengeResult.$inject = ['$resource'];
    ChallengeResultById.$inject = ['$resource'];
    



    function ChallengeManager($resource) {
        var resourceUrl = 'api/challenges/:id';

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

    function ChallengeBanner($resource) {
        var resourceUrl = 'api/challenges/banner';

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
            'save': { method: 'POST' },
            'update': { method: 'PUT' },
        });
    }

    function ChallengeByAuthority($resource) {
        var resourceUrl = 'api/challenges-by-authories';

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
    
    function ChallengeInfo($resource, DateUtils) {
        var resourceUrl = 'api/challenge-infos/:id';
        
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.eventStartTime = DateUtils.convertDateTimeFromServer(data.eventStartTime);
                        data.eventEndTime = DateUtils.convertDateTimeFromServer(data.eventEndTime);
                        data.applyStartTime = DateUtils.convertDateTimeFromServer(data.applyStartTime);
                        data.applyEndTime = DateUtils.convertDateTimeFromServer(data.applyEndTime);
                        data.activeTime = DateUtils.convertDateTimeFromServer(data.activeTime);
                    }
                    return data;
                }
            },
            'update': { method: 'PUT' },
            'save': { method: 'POST' }
        });
    }
    
    function TimeServer($resource, DateUtils) {
        var resourceUrl = 'api/challenges/get-time';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.timeServer = DateUtils.convertDateTimeFromServer(data.timeServer);
                    }
                    return data;
                }
            }
        });
    }
    
    function ApprovedApplication($resource, DateUtils) {
        var resourceUrl = '/api/applications/approved/:challengeId';
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
            }
        });
    }
    
    function ChallengeResult($resource) {
        var resourceUrl = '/api/challenge-results/:id';

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

    function ChallengeResultById($resource) {
        var resourceUrl = '/api/challenge-results/challenge/:challengeId';
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
    
})();
