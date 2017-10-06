(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListDetailController', ApplicationsListDetailController);

    ApplicationsListDetailController.$inject = ['entity', 'Principal', 'ApplicationsListDetails', 'Challenge', 'UserDetail', 'ApplicationStatus', 'ApplicationByChallenge'];

    function ApplicationsListDetailController(entity, Principal, ApplicationsListDetails, Challenge, UserDetail, ApplicationStatus, ApplicationByChallenge) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.application = entity;
        vm.getSkills = getSkills;
        vm.members = vm.application.members;
        vm.determinateValue = 0;
        vm.progressCount = progressCount;
        vm.approve = approve;
        vm.reject = reject;
        vm.getChallengeInfo = getChallengeInfo;
        vm.getUserInfo = getUserInfo;
        vm.userInfo = {};
        vm.getApplicationByChallenge = getApplicationByChallenge;

        getSkills();
        getChallengeInfo();
        getUserInfo();
        getApplicationByChallenge();

        function getSkills() {
            vm.members.map(function(member) {
                return vm.skills = member.skills.split(',');
            });
        }

        function getChallengeInfo() {
            Challenge.query(function(data) {
                data.map(function(challenge) {
                    if(challenge.id == vm.application.challengeId) {
                        vm.challenge = challenge;
                    }
                })
            })
        }

        function getUserInfo() {
          UserDetail.query(function(data) {
            return vm.userInfo = data;
          });
        }

        function progressCount() {
           return vm.determinateValue += 10;
        }

        function approve(application) {
            application.status = 'APPROVED';
            ApplicationStatus.update(application);
        }

        function reject(application) {
            application.status = 'REJECTED';
            ApplicationStatus.update(application);
        }

        function getApplicationByChallenge() {
          ApplicationByChallenge.query({challengeId: vm.application.challengeId}, function(data) {
            console.log(data);
          })
        }
    }
})();
