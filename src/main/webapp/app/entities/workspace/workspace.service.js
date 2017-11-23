(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('ChallengeWorkspace', ChallengeWorkspace)
        .factory('ChallengeWorkspaceNews', ChallengeWorkspaceNews)
        .factory('ChallengeWorkspaceQuestion', ChallengeWorkspaceQuestion)
        .factory('ChallengeWorkspaceAnswer', ChallengeWorkspaceAnswer)

    ChallengeWorkspace.$inject = ['$resource'];
    ChallengeWorkspaceNews.$inject = ['$resource'];
    ChallengeWorkspaceQuestion.$inject = ['$resource'];
    ChallengeWorkspaceAnswer.$inject = ['$resource'];

    function ChallengeWorkspace ($resource) {
        var resourceUrl =  'api/challenge-workspaces';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: false},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' }
        });
    }
    
    function ChallengeWorkspaceNews ($resource) {
        var resourceUrl =  'api/challenge-workspace-news';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: false},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' }
        });
    }
    function ChallengeWorkspaceQuestion ($resource) {
        var resourceUrl =  'api/challenge-workspace-questions';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: false},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' }
        });
    }
    function ChallengeWorkspaceAnswer ($resource) {
        var resourceUrl =  'api/challenge-workspace-answers';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: false},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' }
        });
    }

})();
