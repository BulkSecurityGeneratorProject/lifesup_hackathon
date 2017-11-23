(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerDialogController', ChallengeManagerDialogController);

    ChallengeManagerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$mdDialog', '$q', 'entity', 'ChallengeManager', 'ChallengeInfo', 'Application', 'Company', 'ChallengeBanner', 'validation', 'TimeServer'];

    function ChallengeManagerDialogController($timeout, $scope, $stateParams, $mdDialog, $q, entity, ChallengeManager, ChallengeInfo, Application, Company, ChallengeBanner, validation, TimeServer) {
        var vm = this;

        vm.challenge = entity;
        vm.challengeInfo = {};
        vm.clear = clear;
        vm.save = save;
        vm.isMinMaxInvalid = validation.isMinMaxInvalid;
        vm.isLowerThan100 = validation.isLowerThan100;
        vm.today = {};
        TimeServer.get(function(result){
            vm.today = new Date(result.timeServer);
            vm.minDate = new Date(result.timeServer);
        });
        

        getChallengesInfo();

        function getChallengesInfo() {
            if (vm.challenge.id == null) {
                vm.challengeInfo = {
                    status: "DRAFT",
                };
            }
            else {
                vm.challengeInfo = vm.challenge.info;
                console.log(vm.challenge);
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
            var today = vm.today.getTime();
            var appClose = new Date(vm.challengeInfo.applicationCloseDate);
            var endApp = new Date(appClose.getFullYear(), appClose.getMonth(), appClose.getDate() + 1).getTime();

            var evClose = new Date(vm.challengeInfo.eventEndTime);
            var endEv = new Date(evClose.getFullYear(), evClose.getMonth(), evClose.getDate() + 1).getTime();

            if (endEv - today < 0) {
                vm.challengeInfo.status = 'CLOSED';
            } else{
                if (endApp - today < 0){
                    vm.challengeInfo.status = 'INACTIVE';
                }
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
