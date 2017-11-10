(function () {
  'use strict';

  angular
    .module('hackathonApp')
    .controller('ApplicationsListDetailController', ApplicationsListDetailController);

  ApplicationsListDetailController.$inject = ['entity', 'Principal', 'ApplicationsListDetails', 'Challenge', 'UserDetail', 'ApplicationStatus', 'ApplicationByChallengeId', 'ApplicationValidation', '$http'];

  function ApplicationsListDetailController(entity, Principal, ApplicationsListDetails, Challenge, UserDetail, ApplicationStatus, ApplicationByChallengeId, ApplicationValidation,$http) {
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
    vm.numOfFields = null;

    vm.account = null;
    Principal.identity().then(function (account) {
      vm.account = account;
    });

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

      angular.forEach(vm.members, function (challenge) {
        //Convert Base64 img 
        $http({
          url: '/api/challenges/get-banner-base64',
          method: "POST",
          headers: {
            'Content-Type': 'text/plain'
          },
          data: challenge.logoUrl,
          transformResponse: [function (data) {
            return data;
          }]
        })
          .then(function (response) {
            // success
            if (response.data.length > 1) {
              challenge.logoUrl64 = "data:image/jpeg;base64," + response.data;

            } else {
              challenge.logoUrl64 = null;
            }
          },
          function (response) { // optional
            // failed
          });
      });
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
      return vm.determinateValue += 100 / vm.numOfFields;
    }

    function approve(application) {
      application.status = 'APPROVED';
      application.members = null;
      ApplicationStatus.update(application);
    }

    function reject(application) {
      application.status = 'REJECTED';
      application.members = null;
      ApplicationStatus.update(application);
    }

    function getApplicationByChallenge(challengeId) {
      ApplicationByChallengeId.query({ challengeId: vm.application.challengeId }, function (data) {
        vm.challengeUserApplication = data;
      })
    }

    //team invitation validation
    function validateApplication() {
      ApplicationValidation.query({ applicationId: vm.application.id }, function (data) {
        vm.validations = data;
        vm.numOfFields = data.length;
        vm.validations = vm.validations.map(function (item) {
          return item.split(',');
        })
      })
    }

  }
})();
