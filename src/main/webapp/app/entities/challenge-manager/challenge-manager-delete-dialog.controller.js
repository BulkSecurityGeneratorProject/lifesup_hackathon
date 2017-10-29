(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerDeleteController', ChallengeManagerDeleteController)

    ChallengeManagerDeleteController.$inject = ['$scope', '$state', 'entity', 'ChallengeManager', 'ChallengeInfo', '$mdDialog'];

    function ChallengeManagerDeleteController($scope, $state, entity, ChallengeManager, ChallengeInfo, $mdDialog) {
        var vm = this;

        vm.challenge = entity;
        console.log(entity);
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        function clear() {
            $mdDialog.cancel();
        }

        function confirmDelete() {
            // ChallengeManager.delete({ id: id });
            vm.challenge.info.status = 'REMOVED';
            ChallengeInfo.update(vm.challenge.info);
            $mdDialog.hide(vm.challenge);
        }
    }
})();
