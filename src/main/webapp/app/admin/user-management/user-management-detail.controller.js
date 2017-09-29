(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('UserManagementDetailController', UserManagementDetailController);

    UserManagementDetailController.$inject = ['$scope', '$stateParams', 'User', 'Skill', 'Experience', 'UserDetail', 'Principal'];

    function UserManagementDetailController($scope, $stateParams, User, Skill, Experience, UserDetail, Principal) {
        var vm = this;

        vm.user = {};
        vm.account = null;
        vm.rotateCard = rotateCard;
        vm.skills = [];
        vm.workAreas = [];
        vm.userInfo = {
            logoUrl: "content/images/avatar.jpg"
        }

        User.get({ login: $stateParams.login }, function (result) {
            vm.user = result;
            console.log(result);
            if (result.userInfo) {
                vm.userInfo = result.userInfo;
                vm.skills = result.userInfo.skills.split(",");
                vm.workAreas = result.userInfo.workArea.split(",");
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
