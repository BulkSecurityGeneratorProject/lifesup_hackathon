angular
    .module('hackathonApp')
    .factory('dataservice', dataservice);

dataservice.$inject = ['$http', '$log'];

function dataservice($http, $log) {
    return {
        getData: getData
    };

    function getData() {
        return $http.get('app/challengeslist/challengeslist.json')
            .then(getDataSuccess)
            .catch(getDataFail);

        function getDataSuccess(response) {
            return response.data;
        }

        function getDataFail(error) {
            $log.error('ERROR' + error.data);
        }
    }
}