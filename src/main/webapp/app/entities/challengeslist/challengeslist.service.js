// angular
//     .module('hackathonApp')
//     .factory('dataservice', dataservice);

// dataservice.$inject = ['$http', '$log'];

// function dataservice($http, $log) {
//     return {
//         getData: getData
//     };

//     function getData() {
//         return $http.get('i18n/en/challengeslist.json')
//             .then(getDataSuccess)
//             .catch(getDataFail);

//         function getDataSuccess(response) {
//             return response.data;
//         }

//         function getDataFail(error) {
//             $log.error('ERROR' + error.data);
//         }
//     }
// }

(function () {
    'use strict';
    angular
        .module('hackathonApp')
        .factory('Challenge', Challenge)
        .factory('ChallangeDetails', ChallangeDetails);

    ChallangeDetails.$inject = ['$resource', 'DateUtils'];

    Challenge.$inject = ['$resource'];

    function Challenge($resource) {
        var resourceUrl = 'i18n/en/challengeslist.json';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: false },
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

    function ChallangeDetails($resource, DateUtils) {
        var resourceUrl = 'i18n/en/challengeslist.json';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: false },
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
})();
