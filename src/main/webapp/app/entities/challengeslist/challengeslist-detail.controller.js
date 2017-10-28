(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengesListDetailController', ChallengesListDetailController);

    ChallengesListDetailController.$inject = ['$stateParams', 'Challenge', 'ChallengeInfo', 'entity', 'Principal', 'ApplicationsByUser'];

    function ChallengesListDetailController($stateParams, Challenge, ChallengeInfo, entity, Principal, ApplicationsByUser) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.challenge = entity;
        vm.getChallengeId = getChallengeId;
        vm.getApplicationId = getApplicationId;
        vm.getApplicationStatus = getApplicationStatus;
        vm.getApplicationByUser = getApplicationByUser;
        vm.challengeId = [];
        vm.applicationId = '';
        vm.timeLeft = null;


        getApplicationByUser();
        parseChallengeStatus();


        function parseChallengeStatus() {
            if (vm.challenge.info.status != 'INACTIVE') {
                var today = new Date().getTime();
                var appClose = new Date(vm.challenge.info.applicationCloseDate);
                var endApp = new Date(appClose.getFullYear(), appClose.getMonth(), appClose.getDate() + 1).getTime();
                vm.timeLeft = endApp - today;
                if (vm.timeLeft < 0) {
                    vm.challenge.info.status = 'INACTIVE';
                    ChallengeInfo.update(vm.challenge.info);
                } else {
                    var time = vm.timeLeft / (1000 * 60 * 60 * 24);
                    if (time <= 1) {
                        vm.challenge.timeLeft = 'Apply in less than 1 day';
                    } else {
                        vm.challenge.timeLeft = 'Apply in ' + parseInt(Math.ceil(time)) + ' day(s)';
                    }
                }
            } else {
                vm.challenge.timeLeft = "Time's Up";
            }
        }

        function getApplicationByUser() {
            ApplicationsByUser.query(function (data) {
                vm.applicationsByUser = data;
                getChallengeId();
            });
        }

        function getChallengeId() {
            vm.challengeId = vm.applicationsByUser.map(function (item) {
                return item.challengeId;
            });
        }

        function getApplicationId(id) {
            vm.applicationsByUser.map(function (item) {
                if (item.challengeId === id) {
                    return vm.applicationId = item.applicationId;
                }
            });
        }

        function getApplicationStatus() {
            if (vm.challengeId.indexOf(vm.challenge.id) > -1) {
                return "APPLIED";
            } else if (vm.timeLeft < 0) {
                return "INACTIVE";
            } else {
                return "ACTIVE";
            }
        }
    }
})();
