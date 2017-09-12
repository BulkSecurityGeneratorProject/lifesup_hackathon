(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeInfoDeleteController',ChallengeInfoDeleteController);

    ChallengeInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChallengeInfo'];

    function ChallengeInfoDeleteController($uibModalInstance, entity, ChallengeInfo) {
        var vm = this;

        vm.challengeInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ChallengeInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
