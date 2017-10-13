(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$timeout', '$stateParams', '$state', 'Application', 'UserApplicationByChallengeID', 'InviteMember', 'ApplicationBasicInfo', 'ApplicationsListDetails', 'Principal', 'UserDetail', 'DeleteInvitedMail'];

    function TeamController($scope, $timeout, $stateParams, $state, Application, UserApplicationByChallengeID, InviteMember, ApplicationBasicInfo, ApplicationsListDetails, Principal, UserDetail, DeleteInvitedMail) {
        var vm = this;
        vm.save = save;
        vm.challengeId = $stateParams.id;
        vm.applicationId = null;
        vm.team = {};
        vm.account = null;
        vm.appDetail = [];
        vm.inviteMails = null;
        vm.members = [];
        vm.deleteMail = [];
        vm.removeMail = removeMail;

        function removeMail(mail) {
            vm.deleteMail.push(mail);
            vm.members.splice(vm.members.indexOf(mail));
        }

        load();

        function load() {
            vm.entity = UserApplicationByChallengeID.get({ challengeId: $stateParams.id }, function (result) {
                if (result.applicationId) {
                    vm.applicationId = result.applicationId;
                    Application.get({ id: result.applicationId }, function (result) {
                        vm.team = result;
                    });
                    ApplicationBasicInfo.get({ applicationId: result.applicationId }, function (data) {
                        data.members.forEach(function (element) {
                            var temp = element.split(',');
                            vm.members.push(temp);
                        })
                    })
                } else {
                    UserDetail.get(function (result) {
                        var temp = [result.email, result.status];
                        vm.members.push(temp);
                    });
                }
            });
        }

        Principal.identity().then(function (account) {
            vm.account = account;
        });

        function save() {
            vm.team.challengeId = $stateParams.id;
            vm.team.members = [];
            if (vm.deleteMail) {
                vm.deleteMail.forEach(function (element) {
                    DeleteInvitedMail.delete({ email: element, applicationId: vm.applicationId }, onDeleteSuccess, onDeleteError);
                })
            }
            
            if (vm.inviteMails) {
                vm.team.members = vm.inviteMails.split(";");
            }
            
            if (vm.entity.applicationId) {
                Application.update(vm.team, onSaveSuccess, onSaveError);
            } else {
                Application.save(vm.team, onSaveSuccess, onSaveError);
            }
        }

        function onDeleteSuccess() {
            console.log("delete successful");
        }

        function onDeleteError() {
            console.log("delete failed");
        }

        function onSaveSuccess(result) {
            $state.go('applicationslist-detail', { id: result.id });
        }


        function onSaveError() {
            alert("Error");
        }
    }
})();
