(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeDeleteController',ChallengeDeleteController);

    ChallengeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Challenge'];

    function ChallengeDeleteController($uibModalInstance, entity, Challenge) {
        var vm = this;

        vm.challenge = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Challenge.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
