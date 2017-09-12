(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeInfoDialogController', ChallengeInfoDialogController);

    ChallengeInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChallengeInfo'];

    function ChallengeInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ChallengeInfo) {
        var vm = this;

        vm.challengeInfo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.challengeInfo.id !== null) {
                ChallengeInfo.update(vm.challengeInfo, onSaveSuccess, onSaveError);
            } else {
                ChallengeInfo.save(vm.challengeInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackathonApp:challengeInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.eventStartTime = false;
        vm.datePickerOpenStatus.eventEndTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
