(function () {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('Challenge', Challenge)
        .factory('ChallengeInfo', ChallengeInfo);

    ChallengeInfo.$inject = ['$resource', 'DateUtils'];



    Challenge.$inject = ['$resource'];

    function Challenge($resource) {
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
            'update': { method: 'PUT' }
        });
    }
})();