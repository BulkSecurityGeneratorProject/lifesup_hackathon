(function () {
  'use strict';

  angular
    .module('hackathonApp')
    .controller('WorkspaceTeamController', WorkspaceTeamController);

  WorkspaceTeamController.$inject = ['entity', 'Principal', 'ApplicationsListDetails', 'Challenge', 'UserDetail', 'ApplicationStatus', 'ApplicationByChallengeId', 'ApplicationValidation', '$http'];

  function WorkspaceTeamController(entity, Principal, ApplicationsListDetails, Challenge, UserDetail, ApplicationStatus, ApplicationByChallengeId, ApplicationValidation, $http) {
    var vm = this;
    vm.isAuthenticated = Principal.isAuthenticated;
    vm.application = entity;
    vm.members = vm.application.members;

    vm.account = null;
    Principal.identity().then(function (account) {
      vm.account = account;
    });

    getSkills();

    function getSkills() {
      if (vm.members) {
        vm.members.map(function (member) {
          if (member.skills) {
            return member.skills = member.skills.split(',');
          }
        });
      }
    }
    console.log(vm.members);

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
})();
