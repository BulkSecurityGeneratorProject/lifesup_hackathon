(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$stateParams', '$state', 'Application', 'UserApplicationByChallengeID'];

    function TeamController($scope, $stateParams, $state, Application, UserApplicationByChallengeID) {
        var vm = this;
        vm.save = save;
        vm.challengeId = $stateParams.id;
        vm.team = {};
        vm.member = [
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
            return Array(3);
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
            $state.go('applicationslist-detail', {id:result.id});
        }

        function onSaveError() {

        }
    }
})();
