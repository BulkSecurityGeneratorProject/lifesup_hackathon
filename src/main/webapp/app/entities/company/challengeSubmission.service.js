(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('ChallengeSubmissionCreated', ChallengeSubmissionCreated);

    ChallengeSubmissionCreated.$inject = ['$resource'];

    function ChallengeSubmissionCreated ($resource) {
        var resourceUrl =  'api/challenge-submissions-created';

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
            }
        });
    }

})();
