(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeUserApplicationDialogController', ChallengeUserApplicationDialogController);

    ChallengeUserApplicationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChallengeUserApplication'];

    function ChallengeUserApplicationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ChallengeUserApplication) {
        var vm = this;

        vm.challengeUserApplication = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.challengeUserApplication.id !== null) {
                ChallengeUserApplication.update(vm.challengeUserApplication, onSaveSuccess, onSaveError);
            } else {
                ChallengeUserApplication.save(vm.challengeUserApplication, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackathonApp:challengeUserApplicationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
