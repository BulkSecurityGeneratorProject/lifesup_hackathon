(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$stateParams', '$state', 'Application', 'UserApplicationByChallengeID', 'InviteMember', 'ApplicationMembers'];

    function TeamController($scope, $stateParams, $state, Application, UserApplicationByChallengeID, InviteMember, ApplicationMembers) {
        var vm = this;
        vm.save = save;
        vm.challengeId = $stateParams.id;
        vm.applicationId = null;
        vm.team = {};
        vm.inviteMails = null;

        load();

        function load() {
            vm.entity = UserApplicationByChallengeID.get({ challengeId: $stateParams.id }, function (result) {
                console.log(result);
                if (result.applicationId) {
                    Application.get({ id: result.applicationId }, function (result) {
                        vm.team = result;
                    });
                    ApplicationMembers.query({ applicationId: result.applicationId }, function (data) {
                        vm.members = data;
                        console.log(vm.members);
                    })
                }
            });
        }

        function save() {
            vm.team.challengeId = $stateParams.id;
            vm.team.members = vm.inviteMails.split(";");
            if (vm.entity.applicationId) {
                Application.update(vm.team, onSaveSuccess, onSaveError);
            }
            else {
                console.log(vm.team);
                Application.save(vm.team, onSaveSuccess, onSaveError);
            }

        }

        function onSaveSuccess(result) {
            $state.go('applicationslist-detail', { id: result.id });
        }


        function onSaveError() {

        }
    }
})();
