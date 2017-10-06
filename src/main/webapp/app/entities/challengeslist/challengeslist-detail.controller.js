(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengesListDetailController', ChallengesListDetailController);

    ChallengesListDetailController.$inject = ['$stateParams', 'Challenge', 'entity', 'Principal', 'ApplicationsByUser'];

    function ChallengesListDetailController($stateParams, Challenge, entity, Principal, ApplicationsByUser) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.challenge = entity;
        vm.getChallengeId = getChallengeId;
        vm.getApplicationId = getApplicationId;
        vm.getApplicationStatus = getApplicationStatus;
        vm.getApplicationByUser = getApplicationByUser;
        vm.challengeId = [];
        vm.applicationId = '';

        var today = (new Date()).getTime();
        var endDate = new Date(vm.challenge.info.applicationCloseDate).getTime();
        var diff = endDate - today;
        vm.timeLeft = parseInt(Math.ceil(diff / (1000 * 60 * 60 * 24)));

        getApplicationByUser();

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
            });
        }

        function getApplicationStatus() {
            if (vm.challengeId.indexOf(vm.challenge.id) > -1) {
              return "APPLIED";
            } else if (vm.timeLeft < 0) {
              return "CLOSED";
            } else {
              return "ACTIVE";
            }
        }
    }
})();
