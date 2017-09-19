(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeDeleteController', ChallengeDeleteController)

    ChallengeDeleteController.$inject = ['$scope', '$state', 'entity', 'Challenge', 'ChallengeInfo', '$mdDialog'];

    function ChallengeDeleteController($scope, $state, entity, Challenge, ChallengeInfo, $mdDialog) {
        var vm = this;

        vm.challenge = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        function clear() {
            $mdDialog.cancel();
        }

        function confirmDelete(id) {
            Challenge.delete({ id: id },
                function () {
                    ChallengeInfo.delete({ id: vm.challenge.info.id }, function () {
                        $mdDialog.hide(id);
                    });
                });
        }
    }
})();
