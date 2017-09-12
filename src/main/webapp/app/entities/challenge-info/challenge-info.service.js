(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('ChallengeInfo', ChallengeInfo);

    ChallengeInfo.$inject = ['$resource', 'DateUtils'];

    function ChallengeInfo ($resource, DateUtils) {
        var resourceUrl =  'api/challenge-infos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.eventStartTime = DateUtils.convertDateTimeFromServer(data.eventStartTime);
                        data.eventEndTime = DateUtils.convertDateTimeFromServer(data.eventEndTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
