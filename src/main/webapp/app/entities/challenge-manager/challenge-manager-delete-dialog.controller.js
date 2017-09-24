(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerDeleteController', ChallengeManagerDeleteController)

    ChallengeManagerDeleteController.$inject = ['$scope', '$state', 'entity', 'Challenge', 'ChallengeInfo', '$mdDialog'];

    function ChallengeManagerDeleteController($scope, $state, entity, Challenge, ChallengeInfo, $mdDialog) {
        var vm = this;

        vm.challenge = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        function clear() {
            $mdDialog.cancel();
        }

        function confirmDelete(id) {
            Challenge.delete({ id: id });
            $mdDialog.hide(id);
        }
    }
})();
