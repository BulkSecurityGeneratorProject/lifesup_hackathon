(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$stateParams', '$state', 'Application', 'UserApplicationByChallengeID', 'InviteMember'];

    function TeamController($scope, $stateParams, $state, Application, UserApplicationByChallengeID, InviteMember) {
        var vm = this;
        vm.save = save;
        vm.challengeId = $stateParams.id;
        vm.applicationId = null;
        vm.team = {};
        vm.invite = {};
        vm.members = [
            {
                name: 'Duc Tran',
                email: 'ductranplay@gmail.com',
                profileComplete: true
            },
            {
                name: 'New Commer',
                email: 'conkienconcon@gmail.com',
                profileComplete: false
            }
        ]

        vm.emptySlot = emptySlot;

        function emptySlot(){
            return Array(1);
        }

        load()

        function load() {
            vm.entity = UserApplicationByChallengeID.get({ challengeId: $stateParams.id }, function (result) {
                if (result.applicationId) {
                    Application.get({ id: result.applicationId }, function (result) {
                        vm.team = result;
                    });
                }
            });
        }

        function save() {
            vm.team.challengeId = $stateParams.id;
            if (vm.entity.applicationId) {
                Application.update(vm.team, onSaveSuccess, onSaveError);
            }
            else {
                Application.save(vm.team, onSaveSuccess, onSaveError);
            }

        }

        function onSaveSuccess(result) {
            if (vm.invite.userName && vm.invite.userEmail){
                vm.invite.applicationId = result.id;
                vm.invite.challengeId = result.challenge.id;
                console.log(vm.invite);
                InviteMember.save(vm.invite, onInviteSuccess, onInviteError);
            }
            else{
                console.log("else");
                $state.go('applicationslist-detail', {id:result.id});
            }
            vm.applicationId = result.id;
        }

        function onInviteSuccess(){
            $state.go('applicationslist-detail', {id:vm.applicationId});
        }

        function onInviteError(){
            alert('Invite Member Failed');
        }

        function onSaveError() {

        }
    }
})();
