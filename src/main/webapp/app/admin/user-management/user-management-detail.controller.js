(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('UserManagementDetailController', UserManagementDetailController);

    UserManagementDetailController.$inject = ['$scope', '$stateParams', 'User', 'Skill', 'Experience', 'UserDetail', 'Principal','$http'];

    function UserManagementDetailController($scope, $stateParams, User, Skill, Experience, UserDetail, Principal, $http) {
        var vm = this;

        vm.user = {};
        vm.account = null;
        vm.rotateCard = rotateCard;
        vm.skills = [];
        vm.workAreas = [];
        vm.userInfo = {
            logoUrl: "content/images/default/avatar.jpg"
        }

        User.get({ login: $stateParams.login }, function (result) {
            vm.user = result;
            if (result.userInfo) {
                vm.userInfo = result.userInfo;
                vm.skills = result.userInfo.skills.split(",");
                vm.workAreas = result.userInfo.workArea.split(",");
                $http({
                    url: '/api/challenges/get-banner-base64',
                    method: "POST",
                    headers: {
                      'Content-Type': 'text/plain'
                    },
                    data: vm.userInfo.logoUrl,
                    transformResponse: [function (data) {
                      return data;
                    }]
                  })
                    .then(function (response) {
                      // success
                      if (response.data.length > 1) {
                        vm.userInfo.logoUrl64 = "data:image/jpeg;base64," + response.data;
          
                      } else {
                        vm.userInfo.logoUrl64 = null;
                      }
                    },
                    function (response) { // optional
                      // failed
                    });
            }
        });

        function rotateCard(btn) {
            var card = angular.element(document.querySelector('.card-container'));
            if (card.hasClass('hover')) {
                card.removeClass('hover');
            } else {
                card.addClass('hover');
            }
        }



    }
})();
