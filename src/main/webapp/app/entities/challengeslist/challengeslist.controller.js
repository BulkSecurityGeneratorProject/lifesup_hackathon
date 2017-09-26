(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengesListController', ChallengesListController);

    ChallengesListController.$inject = ['$log', '$timeout', '$q','Principal', 'Challenge'];

    function ChallengesListController($log, $timeout, $q, Principal, Challenge) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;

        vm.Math = Math;
        vm.challenges = [];
        vm.loadAll = loadAll;
        
        vm.filterByStatus = filterByStatus;
        vm.getStatus = getStatus;
        vm.filter = {};
        vm.startDate = null;
        vm.endDate = null;
        vm.filterByStartDate = filterByStartDate;
        vm.filterByEndDate = filterByEndDate;
        vm.timeLeft;

        // getData();

        // // Functions - Definitions
        // function getData() {
        //     return dataservice.getData()
        //         .then(function(data) {
        //             vm.challenges = data.challengeslist;
        //         });
        // }

        loadAll();
        function loadAll() {
            Challenge.query(function (result) {
                vm.challenges = result;
                console.log(result);
                vm.challenges.map(function(challenge){
                    var today = (new Date()).getTime();
                    var endDate = new Date(challenge.info.applicationCloseDate).getTime();
                    var diff = endDate - today;
                    vm.timeLeft = challenge.timeLeft = parseInt(Math.ceil(diff / (1000 * 60 * 60 * 24)));
                })
            });
        }

        function filterByStatus(challenge) {
            return vm.filter[challenge.info.status] || noFilter(vm.filter);
        }
            
        function getStatus() {
            return (vm.challenges || []).
              map(function (challenge) { return challenge.info.status; }).
              filter(function (cat, idx, arr) { return arr.indexOf(cat) === idx; });
        }

        function noFilter(filterObj) {
            return Object.
              keys(filterObj).
              every(function (key) { return !filterObj[key]; });
        }

        function filterByStartDate(challenge) {
            return vm.startDate ? vm.startDate > challenge.deadline : vm.challenges;
        }   

        function filterByEndDate(challenge) {
            return vm.endDate ? vm.endDate < challenge.deadline : vm.challenges;
        }

    }
   
})();
