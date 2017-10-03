(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListDetailController', ApplicationsListDetailController);

    ApplicationsListDetailController.$inject = ['$stateParams', 'ApplicationsList', 'entity', 'Principal', 'ApplicationsListDetails', 'Challenge'];

    function ApplicationsListDetailController($stateParams, ApplicationsList, entity, Principal, ApplicationsListDetails, Challenge) {
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

        getSkills();
        getChallengeInfo();

        function getSkills() {
            vm.members.map(function(member) {
                console.log(member.workArea);
                return vm.skills = member.workArea.split(',');  
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

        function progressCount() {
           return vm.determinateValue += 10;
        }

        function approve(id) {
            vm.application = ApplicationsList.get({id: id}, function(result){
                vm.application.status = 'APPROVED';
                vm.application.challengeId = result.challenge.id;
                ApplicationsList.update(vm.application);
            })
        }

        function reject(id) {
            vm.application = ApplicationsList.get({id: id}, function(result){
                vm.application.status = 'REJECTED';
                vm.application.challengeId = result.challenge.id;
                ApplicationsList.update(vm.application);
            })
        }
    }
})();
