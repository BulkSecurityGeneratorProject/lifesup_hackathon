(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerDialogController', ChallengeManagerDialogController);

    ChallengeManagerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$mdDialog', '$q', 'entity', 'ChallengeManager', 'ChallengeInfo', 'Application', 'Company', 'ChallengeBanner', 'validation'];

    function ChallengeManagerDialogController($timeout, $scope, $stateParams, $mdDialog, $q, entity, ChallengeManager, ChallengeInfo, Application, Company, ChallengeBanner, validation) {
        var vm = this;

        vm.challenge = entity;
        vm.challengeInfo = {};
        vm.clear = clear;
        vm.save = save;
        vm.isMinMaxInvalid = validation.isMinMaxInvalid;
        vm.isLowerThan100 = validation.isLowerThan100;
        vm.minDate = new Date();

        getChallengesInfo();

        function getChallengesInfo() {
            if (vm.challenge.id == null) {
                vm.challengeInfo = {
                    status: "DRAFT",
                };
            }
            else {
                vm.challengeInfo = vm.challenge.info;
                vm.minDate = new Date(vm.challenge.info.applicationCloseDate);
                vm.challengeInfo.applicationCloseDate = new Date(vm.challenge.info.applicationCloseDate);
                vm.challengeInfo.selectionInformDate = new Date(vm.challenge.info.selectionInformDate);
                vm.challengeInfo.kickoffWebinarDate = new Date(vm.challenge.info.kickoffWebinarDate);
                vm.challengeInfo.eventStartTime = new Date(vm.challenge.info.eventStartTime);
                vm.challengeInfo.eventEndTime = new Date(vm.challenge.info.eventEndTime);
                vm.challengeInfo.pilotSubmissionCloseDate = new Date(vm.challenge.info.pilotSubmissionCloseDate);
                vm.challengeInfo.pilotPhaseStartDate = new Date(vm.challenge.info.pilotPhaseStartDate);
                vm.challengeInfo.pilotPhaseEndDate = new Date(vm.challenge.info.pilotPhaseEndDate);
            }
        }
        
        function save() {
            vm.isSaving = true;
            var now = new Date().getTime();
            var date = new Date(vm.challengeInfo.applicationCloseDate);
            var end = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1).getTime();
            var time = end - now;
            if (time < 0) {
                vm.challengeInfo.status = 'CLOSED';
            }
            if (vm.challengeInfo.id !== null) {
                ChallengeInfo.update(vm.challengeInfo, onSaveInfoSuccess, onSaveInfoError);
            } else {
                ChallengeInfo.save(vm.challengeInfo, onSaveInfoSuccess, onSaveInfoError);
            }
        }

        function onSaveInfoSuccess(result) {
            if (vm.challenge.id !== null) {
                console.log("Update Challenge");
                ChallengeManager.update(vm.challenge, onSaveSuccess, onSaveError);
            }
            else {
                console.log("Save Challenge");
                vm.challenge.info = result;
                vm.challenge.bannerUrl = "content/images/default/challenge-cover.jpg";
                ChallengeManager.save(vm.challenge, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            if (vm.banner != null) {
                vm.attach = vm.banner;
                vm.attach.challengeId = result.id;
                ChallengeBanner.update(vm.attach, onUploadSuccess, onUploadError);
            }
            else {
                $mdDialog.hide(result);
            }
        }

        function onUploadSuccess(result) {
            $mdDialog.hide(result);
            vm.isSaving = false;
        }


        function onSaveInfoError() {
            alert('Save ChallengeInfo Error');
            vm.isSaving = false;
        }

        function onSaveError() {
            ChallengeInfo.delete({ id: vm.challenge.info.id });
            alert('Save Challenge Error');
            vm.isSaving = false;
        }

        function onUploadError() {
            alert('Upload Banner Error');
            vm.isSaving = false;
        }

        function clear() {
            $mdDialog.cancel('cancel');
        }


    }
})();
