(function () {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('InviteMember', InviteMember)
        .factory('AcceptInvitation', AcceptInvitation)
        .factory('DeclineInvitation', DeclineInvitation)
        .factory('ApplicationMembers', ApplicationMembers)

    InviteMember.$inject = ['$resource'];
    AcceptInvitation.$inject = ['$resource'];
    DeclineInvitation.$inject = ['$resource'];
    ApplicationMembers.$inject = ['$resource'];   

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
        var resourceUrl = '/api/challenge-user-applications/accept-invitation';

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
    // function DeclineInvitation($resource) {
    //     var resourceUrl = '/api/challenge-user-applications/decline-invitation';

    //     return $resource(resourceUrl, {}, {
    //         'query': { method: 'GET', isArray: true },
    //         'get': {
    //             method: 'GET',
    //             transformResponse: function (data) {
    //                 if (data) {
    //                     data = angular.fromJson(data);
    //                 }
    //                 return data;
    //             }
    //         },
    //         'update': { method: 'PUT' },
    //         'save': { method: 'POST' }
    //     });
    // }

    function DeclineInvitation ($resource) {
        var service = $resource('/api/challenge-user-applications/decline-invitation', {}, {
            'get': { method: 'GET', params: {}, isArray: false}
        });

        return service;
    }

    function ApplicationMembers($resource) {
      var resourceUrl =  '/api/challenge-user-applications/member-status/:applicationId';
    
      return $resource(resourceUrl, {applicationId: "@applicationId"}, {
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
