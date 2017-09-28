(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerDeleteController', ChallengeManagerDeleteController)

    ChallengeManagerDeleteController.$inject = ['$scope', '$state', 'entity', 'ChallengeManager', 'ChallengeInfo', '$mdDialog'];

    function ChallengeManagerDeleteController($scope, $state, entity, ChallengeManager, ChallengeInfo, $mdDialog) {
        var vm = this;

        vm.challenge = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        function clear() {
            $mdDialog.cancel();
        }

        function confirmDelete(id) {
            ChallengeManager.delete({ id: id });
            $mdDialog.hide(id);
        }
    }
})();
