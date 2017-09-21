(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengesListController', ChallengesListController);

    ChallengesListController.$inject = ['$scope','dataservice','$log', '$timeout', '$q'];

    function ChallengesListController($scope, dataservice, $log, $timeout, $q) {
        var vm = this;
        vm.Math = Math;
        vm.challenges = [];
        vm.deadline = [];
        vm.getData = getData;
        vm.today = (new Date()).getTime();
        vm.filterByStatus = filterByStatus;
        vm.getStatus = getStatus;
        vm.filter = {};

        getData();

        // Functions - Definitions
        function getData() {
            return dataservice.getData()
                .then(function(data) {
                    vm.challenges = data.challengeslist;
                });
        }

        function filterByStatus(challenge) {
            return vm.filter[challenge.status] || noFilter(vm.filter);
        }
            
        function getStatus() {
            return (vm.challenges || []).
              map(function (challenge) { return challenge.status; }).
              filter(function (cat, idx, arr) { return arr.indexOf(cat) === idx; });
        }

        function noFilter(filterObj) {
            return Object.
              keys(filterObj).
              every(function (key) { return !filterObj[key]; });
        }   
    }
   
})();
