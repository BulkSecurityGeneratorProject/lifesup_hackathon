(function () {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('InviteMember', InviteMember)
        .factory('AcceptInvitation', AcceptInvitation)
        .factory('ApplicationMembers', ApplicationMembers)



    InviteMember.$inject = ['$resource'];
    AcceptInvitation.$inject = ['$resource'];
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
