(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$stateParams', '$state', 'Application', 'UserApplicationByChallengeID', 'InviteMember', 'ApplicationBasicInfo', 'ApplicationsListDetails', 'Principal'];

    function TeamController($scope, $stateParams, $state, Application, UserApplicationByChallengeID, InviteMember, ApplicationBasicInfo, ApplicationsListDetails, Principal) {
        var vm = this;
        vm.save = save;
        vm.challengeId = $stateParams.id;
        vm.applicationId = null;
        vm.team = {};
        vm.appDetail = [];
        vm.inviteMails = null;
        vm.members = [];

        load();

        function load() {
            vm.entity = UserApplicationByChallengeID.get({ challengeId: $stateParams.id }, function (result) {
                console.log(result);
                if (result.applicationId) {
                    Application.get({ id: result.applicationId }, function (result) {
                        vm.team = result;
                        console.log(result);
                    });
                    ApplicationsListDetails.get({ id: result.applicationId }, function (result) {
                        vm.appDetail = result;
                        console.log(result);
                    });
                    ApplicationBasicInfo.get({ applicationId: result.applicationId }, function (data) {
                        data.members.forEach(function(element){
                            console.log(element);
                            var temp = element.split(',');
                            vm.members.push(temp);
                        })
                    })
                }
            });
        }

        vm.account = null;
        Principal.identity().then(function (account) {
            vm.account = account;
        });

        function save() {

            vm.team.challengeId = $stateParams.id;
            vm.team.members = [vm.account.email];
            vm.mails = [];
            vm.canSave = true;

            if (vm.entity.applicationId) {
                vm.appDetail.members.forEach(function (element) {
                    vm.mails.push(element.invitedMail);
                }, this);
                if (vm.inviteMails) {
                    vm.team.members = vm.inviteMails.split(";");
                    vm.team.members.forEach(function (element) {
                        if (vm.mails.indexOf(element) != -1) {
                            alert(element + " has been invited!")
                            vm.canSave = false;
                        }
                    })
                }
            }
            
            if (vm.canSave) {
                console.log("pass");
                if (vm.entity.applicationId) {
                    Application.update(vm.team, onSaveSuccess, onSaveError);
                }
                else {
                    Application.save(vm.team, onSaveSuccess, onSaveError);
                }
            }
        }

        function onSaveSuccess(result) {
            $state.go('applicationslist-detail', { id: result.id });
        }


        function onSaveError() {
            alert("Error");
        }
    }
})();
