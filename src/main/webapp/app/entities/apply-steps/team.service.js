(function () {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('InviteMember', InviteMember)
        .factory('AcceptInvitation', AcceptInvitation)
        .factory('DeclineInvitation', DeclineInvitation)



    InviteMember.$inject = ['$resource'];
    AcceptInvitation.$inject = ['$resource'];
    DeclineInvitation.$inject = ['$resource'];

    function InviteMember($resource) {
        var resourceUrl = 'api/challenge-user-applications/members';
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
            'save': { method: 'POST' }
        });
    }
    function AcceptInvitation($resource) {
        var resourceUrl = 'api/challenge-user-applications/:key/invitation';

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
            'save': { method: 'POST' }
        });
    }

    function DeclineInvitation($resource) {
        var resourceUrl = 'api/challenge-user-applications/:key/invitation';

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
            'save': { method: 'POST' }
        });
    }


})();
