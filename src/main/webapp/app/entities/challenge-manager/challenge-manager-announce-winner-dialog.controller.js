(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerAnnounceWinnerController', ChallengeManagerAnnounceWinnerController)

    ChallengeManagerAnnounceWinnerController.$inject = ['$scope', '$state', 'entity', 'ChallengeManager', 'ChallengeInfo', '$mdDialog', 'ApprovedApplication', '$stateParams', 'ChallengeResult', '$timeout'];

    function ChallengeManagerAnnounceWinnerController($scope, $state, entity, ChallengeManager, ChallengeInfo, $mdDialog, ApprovedApplication, $stateParams, ChallengeResult, $timeout) {
        var vm = this;
        vm.clear = clear;
        vm.save = save;
        vm.challengeResult = entity;
        vm.loadTeams = loadTeams;
        vm.isFormValid = isFormValid;
        function clear() {
            $mdDialog.cancel();
        }

        ApprovedApplication.query({ challengeId: $stateParams.challengeId }, function (res) {
            vm.teams = res;
        }, function () {
            console.log("XXX");
        });


        function save() {
            console.log(vm.challengeResult);
            ChallengeResult.save(vm.challengeResult, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess(){
            $mdDialog.hide();
        }
        function onSaveError(){

        }

        function loadTeams() {
            return $timeout(function () {
            }, 500);
        };

        function isFormValid() {
            var x = vm.challengeResult.firstTeam;
            var y = vm.challengeResult.secondTeam;
            var z = vm.challengeResult.thirdTeam;
            if (x && y && z){
                if (x == z || x == y || y == z) return false;
            }
            if (x && y && !z){
                if (x == y) return false;
            }
            if (x && !y && z){
                if (x == z) return false;
            }
            return true;
        }




    }
})();
