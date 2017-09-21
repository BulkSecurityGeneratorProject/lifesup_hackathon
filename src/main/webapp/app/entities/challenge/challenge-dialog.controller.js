(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeDialogController', ChallengeDialogController);

    ChallengeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$mdDialog', '$q', 'entity', 'Challenge', 'ChallengeInfo', 'Application', 'Company'];

    function ChallengeDialogController($timeout, $scope, $stateParams, $mdDialog, $q, entity, Challenge, ChallengeInfo, Application, Company) {
        var vm = this;

        vm.challenge = entity;
        vm.challengeInfo = {};

        vm.clear = clear;
        vm.save = save;
        vm.applications = Application.query();
        vm.companies = Company.query();

        getChallengeInfo();

        function getChallengeInfo() {
            if (vm.challenge.id == null) {
                vm.challengeInfo = {
                    status: "DRAFT",
                };
            }
            else {
                vm.challengeInfo = vm.challenge.info;
                vm.challengeInfo.eventStartTime = new Date(vm.challenge.info.eventStartTime);
                vm.challengeInfo.eventEndTime = new Date(vm.challenge.info.eventEndTime);
                vm.challengeInfo.pilotPhaseStartDate = new Date(vm.challenge.info.pilotPhaseStartDate);
                vm.challengeInfo.pilotPhaseEndDate = new Date(vm.challenge.info.pilotPhaseEndDate);
                vm.challengeInfo.kickoffWebinarDate = new Date(vm.challenge.info.kickoffWebinarDate);
                vm.challengeInfo.selectionInformDate = new Date(vm.challenge.info.selectionInformDate);
                vm.challengeInfo.applicationCloseDate = new Date(vm.challenge.info.applicationCloseDate);
                vm.challengeInfo.pilotSubmissionCloseDate = new Date(vm.challenge.info.pilotSubmissionCloseDate);
            }
        }

        function clear() {
            $mdDialog.cancel('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.challengeInfo.id !== null) {
                console.log("Hello");
                ChallengeInfo.update(vm.challengeInfo, onSaveInfoSuccess, onSaveInfoError);
            } else {
                ChallengeInfo.save(vm.challengeInfo, onSaveInfoSuccess, onSaveInfoError);
            }
        }

        function onSaveInfoSuccess(result) {
            $scope.$emit('hackathonApp:challengeUpdate', result);
            if (vm.challenge.id !== null) {
                Challenge.update(vm.challenge, onSaveSuccess, onSaveError);
            }
            else {
                vm.challenge.info = result;
                Challenge.save(vm.challenge, onSaveSuccess, onSaveError);
            }
        }
        function onSaveSuccess(result) {
            $scope.$emit('hackathonApp:challengeUpdate', result);
            $mdDialog.hide(result);
            vm.isSaving = false;
        }

        function onSaveInfoError() {

        }

        function onSaveError() {
            ChallengeInfo.delete({id: vm.challenge.info.id});
        }


    }
})();
