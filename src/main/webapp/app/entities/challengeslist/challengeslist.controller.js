(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengesListController', ChallengesListController);

    ChallengesListController.$inject = ['$log', '$timeout', '$q','Principal', 'Challenge', 'ApplicationsByUser'];

    function ChallengesListController($log, $timeout, $q, Principal, Challenge, ApplicationsByUser) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.challenges = [];
        vm.loadAll = loadAll;
        vm.filterByStatus = filterByStatus;
        vm.getStatus = getStatus;
        vm.filter = {};
        vm.startDate = null;
        vm.endDate = null;
        vm.filterByStartDate = filterByStartDate;
        vm.filterByEndDate = filterByEndDate;
        vm.getChallengeId = getChallengeId;
        vm.getApplicationId = getApplicationId;
        vm.getApplicationStatus = getApplicationStatus;
        vm.getApplicationByUser = getApplicationByUser;
        vm.challengeId = [];
        vm.applicationId = '';

        loadAll();
        getApplicationByUser();

        function loadAll() {
            Challenge.query(function (result) {
                vm.challenges = result;
                vm.challenges.map(function(challenge){
                    var today = (new Date()).getTime();
                    var endDate = new Date(challenge.info.applicationCloseDate).getTime();
                    var diff = endDate - today;
                    challenge.timeLeft = parseInt(Math.ceil(diff / (1000 * 60 * 60 * 24)));
                })
            });
        }

        function getApplicationByUser() {
            ApplicationsByUser.query(function(data) {
                vm.applicationsByUser = data;
                getChallengeId();
            });
        }

        function getChallengeId() {
            vm.challengeId = vm.applicationsByUser.map(function(item){
              return item.challengeId;
            });
        }

        function getApplicationId(id) {
            vm.applicationsByUser.map(function(item){
              if (item.challengeId === id) {
                return vm.applicationId = item.applicationId;
              }
              return;
            });
        }

        function getApplicationStatus(challenge) {
            if (vm.challengeId.indexOf(challenge.id) > -1) {
              return "APPLIED";
            } else if (challenge.timeLeft < 0) {
              return "CLOSED";
            } else {
              return "ACTIVE";
            }
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
            if (vm.startDate) {
              return (new Date(vm.startDate).getTime()) < (new Date(challenge.info.eventStartTime).getTime());
            } else return vm.challenges;
        }

        function filterByEndDate(challenge) {
            if (vm.endDate) {
              return (new Date(vm.endDate).getTime()) > (new Date(challenge.info.eventEndTime).getTime());
            } else return vm.challenges;
        }

    }

})();
