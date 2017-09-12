(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeUserApplicationDeleteController',ChallengeUserApplicationDeleteController);

    ChallengeUserApplicationDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChallengeUserApplication'];

    function ChallengeUserApplicationDeleteController($uibModalInstance, entity, ChallengeUserApplication) {
        var vm = this;

        vm.challengeUserApplication = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ChallengeUserApplication.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
