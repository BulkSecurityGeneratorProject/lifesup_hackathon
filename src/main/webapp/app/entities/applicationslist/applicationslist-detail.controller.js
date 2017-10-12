(function () {
  'use strict';

  angular
    .module('hackathonApp')
    .controller('ApplicationsListDetailController', ApplicationsListDetailController);

  ApplicationsListDetailController.$inject = ['entity', 'Principal', 'ApplicationsListDetails', 'Challenge', 'UserDetail', 'ApplicationStatus', 'ApplicationByChallengeId', 'ApplicationValidation'];

  function ApplicationsListDetailController(entity, Principal, ApplicationsListDetails, Challenge, UserDetail, ApplicationStatus, ApplicationByChallengeId, ApplicationValidation) {
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
    vm.validateApplication = validateApplication;
    vm.validations = [];

    getSkills();
    getChallengeInfo();
    getUserInfo();
    getApplicationByChallenge();
    validateApplication();


    function getSkills() {
      if (vm.members) {
        vm.members.map(function (member) {
          if (member.skills) {
            return member.skills = member.skills.split(',');
          }
        });
      }
    }

    function getChallengeInfo() {
      Challenge.query(function (data) {
        data.map(function (challenge) {
          if (challenge.id == vm.application.challengeId) {
            vm.challenge = challenge;
          }
        })
      })
    }

    function getUserInfo() {
      UserDetail.query(function (data) {
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
      ApplicationByChallengeId.query({ challengeId: vm.application.challengeId }, function (data) {
        vm.challengeUserApplication = data;
      })
    }

    //team invitation validation
    function validateApplication() {
      ApplicationValidation.query({applicationId: vm.application.id}, function(data) {
        vm.validations = data;
        vm.validations = vm.validations.map(function(item) {
          return item.split(',');
        })
        console.log(vm.validations);
      })
    }
  }
})();
