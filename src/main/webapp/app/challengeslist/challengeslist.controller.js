(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengesListController', ChallengesListController);

    ChallengesListController.$inject = ['$scope','dataservice','$log', '$timeout', '$q'];

    function ChallengesListController($scope, dataservice, $log, $timeout, $q) {
        var vm = this;
        $scope.Math = Math;
        vm.challenges = [];
        vm.deadline = [];
        vm.getData = getData;
        vm.today = (new Date()).getTime();

        getData();

        function getData() {
            return dataservice.getData()
                .then(function(data) {
                    vm.challenges = data.challengeslist;
                });
        }
    }

})();
