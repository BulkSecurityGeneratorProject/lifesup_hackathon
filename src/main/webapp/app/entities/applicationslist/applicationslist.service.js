(function() {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('ApplicationsList', ApplicationsList)
        .factory('ApplicationsListDetails', ApplicationsListDetails)
        .factory('UserDetail', UserDetail)
        .factory('ApplicationStatus', ApplicationStatus)
        .factory('ApplicationByChallengeId', ApplicationByChallengeId)
        // .factory('MemberStatusByApplication', MemberStatusByApplication)
        .factory('ApplicationValidation', ApplicationValidation)
        .factory('ChallengeUserApplication', ChallengeUserApplication)
        .factory('UserApplicationByChallengeID', UserApplicationByChallengeID)
        .factory('Application', Application)
        .factory('ApplicationByChallenge', ApplicationByChallenge)
        .factory('ApplicationByCurrentUser', ApplicationByCurrentUser)
        .factory('ApplicationByAcceptKey', ApplicationByAcceptKey)
        .factory('ApplicationBasicInfo', ApplicationBasicInfo);

    ApplicationsList.$inject = ['$resource'];
    ApplicationsListDetails.$inject = ['$resource'];
    UserDetail.$inject = ['$resource'];
    ApplicationStatus.$inject = ['$resource'];
    ApplicationByChallengeId.$inject = ['$resource'];
    // MemberStatusByApplication.$inject = ['$resource'];
    ApplicationValidation.$inject = ['$resource'];
    ChallengeUserApplication.$inject = ['$resource'];
    UserApplicationByChallengeID.$inject = ['$resource'];
    Application.$inject = ['$resource'];
    ApplicationByChallenge.$inject = ['$resource'];
    ApplicationByCurrentUser.$inject = ['$resource'];
    ApplicationByAcceptKey.$inject = ['$resource'];
    ApplicationBasicInfo.$inject = ['$resource'];

    function ApplicationsList ($resource) {
        var resourceUrl =  'api/applications/challenges/:id';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                isArray: true
                /*transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }*/
            },
            'update': { method:'PUT' }
        });
    }

    function ApplicationsListDetails ($resource) {
        var resourceUrl =  '/api/applications/details/:id';

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
            'update': { method:'PUT' }
        });
    }

    function UserDetail ($resource) {
        var resourceUrl =  '/api/user-detail';

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
            'update': { method:'PUT' }
        });
    }

    function ApplicationStatus ($resource) {
        var resourceUrl =  '/api/applications/status';

        return $resource(resourceUrl, {}, {
            'update': { method:'PUT' }
        });
    }

    function ApplicationByChallengeId($resource) {
      var resourceUrl =  '/api/challenge-user-applications/challenge/:challengeId';

      return $resource(resourceUrl, {challengeId: "@challengeId"}, {
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
          'update': { method:'PUT' }
      });
    }

    // function MemberStatusByApplication($resource) {
    //   var resourceUrl =  '/api/challenge-user-applications/member-status/:applicationId';
    //
    //   return $resource(resourceUrl, {applicationId: "@applicationId"}, {
    //       'query': { method: 'GET', isArray: true},
    //       'get': {
    //           method: 'GET',
    //           transformResponse: function (data) {
    //               if (data) {
    //                   data = angular.fromJson(data);
    //               }
    //               return data;
    //           }
    //       },
    //       'update': { method:'PUT' }
    //   });
    // }

    function ApplicationValidation($resource) {
      var resourceUrl =  '/api/applications/check/:applicationId';

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

    function ChallengeUserApplication ($resource) {
        var resourceUrl =  'api/challenge-user-applications/:id';

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
    function UserApplicationByChallengeID ($resource) {
        var resourceUrl =  'api/challenge-user-applications/challenge/:challengeId';

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
