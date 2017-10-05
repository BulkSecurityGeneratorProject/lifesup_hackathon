(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListDetailController', ApplicationsListDetailController);

    ApplicationsListDetailController.$inject = ['$stateParams', 'ApplicationsList', 'entity', 'Principal', 'ApplicationsListDetails', 'Challenge', 'UserDetail'];

    function ApplicationsListDetailController($stateParams, ApplicationsList, entity, Principal, ApplicationsListDetails, Challenge, UserDetail) {
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

        getSkills();
        getChallengeInfo();
        getUserInfo();

        function getSkills() {
            vm.members.map(function(member) {
                console.log(member.skills);
                return vm.skills = member.skills.split(',');
            });
        }

        function getChallengeInfo() {
            Challenge.query(function(data) {
                data.map(function(challenge) {
                    if(challenge.id == vm.application.challengeId) {
                        vm.challenge = challenge;
                        console.log(vm.challenge);
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
            ApplicationsListDetails.update(application);
        }

        function reject(application) {
            application.status = 'REJECTED';
            ApplicationsListDetails.update(application);
        }
    }
})();
