(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('UserManagementDetailController', UserManagementDetailController);

    UserManagementDetailController.$inject = ['$scope', '$stateParams', 'User', 'Skill', 'Experience', 'UserDetail'];

    function UserManagementDetailController($scope, $stateParams, User, Skill, Experience, UserDetail) {
        var vm = this;

        vm.user = {};
        vm.rotateCard = rotateCard;
        vm.skills = [];
        vm.workAreas = [];
        UserDetail.get(function (result) {
            vm.user = result;
            vm.userInfo = result.userInfo;
            vm.skills = result.userInfo.skills.split(",");
            vm.workAreas = result.userInfo.workArea.split(",");
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
