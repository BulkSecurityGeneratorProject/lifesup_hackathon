(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengesListDetailController', ChallengesListDetailController);

    ChallengesListDetailController.$inject = ['$stateParams', 'Challenge', 'ChallengeInfo', 'entity', 'Principal', 'ApplicationsByUser', '$http', 'TimeServer'];

    function ChallengesListDetailController($stateParams, Challenge, ChallengeInfo, entity, Principal, ApplicationsByUser, $http, TimeServer) {
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

        $http({
            url: '/api/challenges/get-banner-base64',
            method: "POST",
            headers: {
                'Content-Type': 'text/plain'
            },
            data: vm.challenge.bannerUrl,
            transformResponse: [function (data) {
                return data;
            }]
        })
            .then(function (response) {
                // success
                if (response.data.length > 1) {
                    vm.challenge.bannerUrl64 = "data:image/jpeg;base64," + response.data;

                } else {
                    vm.challenge.bannerUrl64 = null;
                }
            },
            function (response) { // optional
                // failed
            });


        function parseChallengeStatus() {
            if (vm.challenge.info.status != 'INACTIVE') {
                TimeServer.get(function(result){
                    var today = new Date(result.timeServer);
                    var appClose = new Date(vm.challenge.info.applicationCloseDate);
                    var endApp = new Date(appClose.getFullYear(), appClose.getMonth(), appClose.getDate() + 1).getTime();
                    vm.timeLeft = endApp - today;
                    if (vm.timeLeft < 0) {
                        vm.challenge.info.status = 'INACTIVE';
                    } else {
                        vm.challenge.timeLeft = endApp;
                        console.log(vm.challenge.timeLeft);
                    }
                });
                
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
            } else if (vm.timeLeft < 0 || vm.challenge.info.status === 'INACTIVE') {
                return "INACTIVE";
            } else {
                return "ACTIVE";
            }
        }
    }
})();
