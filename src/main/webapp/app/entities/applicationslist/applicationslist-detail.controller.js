(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListDetailController', ApplicationsListDetailController);

    ApplicationsListDetailController.$inject = ['entity', 'Principal', 'ApplicationsListDetails', 'Challenge', 'UserDetail', 'ApplicationStatus', 'ApplicationByChallengeId', 'MemberStatusByApplication'];

    function ApplicationsListDetailController(entity, Principal, ApplicationsListDetails, Challenge, UserDetail, ApplicationStatus, ApplicationByChallengeId, MemberStatusByApplication) {
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
        vm.challengeUserApplication = {};
        vm.isInvited = isInvited;
        vm.isAccepted = isAccepted;
        vm.getMemberStatus = getMemberStatus;
        vm.memberStatus = [];
        vm.isProfileComplete = isProfileComplete;

        getSkills();
        getChallengeInfo();
        getUserInfo();
        getApplicationByChallenge();
        getMemberStatus();

        function getSkills() {
          if(vm.members) {
            vm.members.map(function(member) {
                return vm.skills = member.skills.split(',');
            });
          }
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

        function getApplicationByChallenge(challengeId) {
          ApplicationByChallengeId.query({challengeId: vm.application.challengeId}, function(data) {
            vm.challengeUserApplication = data;
          })
        }

        //team invitation validation
        function getMemberStatus() {
          MemberStatusByApplication.query({applicationId: vm.application.id}, function(data) {
            vm.memberStatus = data;
            console.log(vm.memberStatus);
          })
        }

        function isInvited() {
          if (vm.memberStatus.invitedMail !== null) {
            return true;
          } else return false;
        }

        function isAccepted() {
          if (vm.memberStatus.memberStatus === "ACCEPT") {
            return true;
          } else {
            return false;
          }
        }

        function isProfileComplete() {
          if (vm.memberStatus.userStatus === "PROFILE_COMPLETE") {
            return true;
          } else {
            return false;
          }
        }
    }
})();
