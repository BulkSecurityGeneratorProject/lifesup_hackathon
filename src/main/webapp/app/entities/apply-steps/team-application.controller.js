(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$timeout', '$stateParams', '$state', 'Application', 'UserApplicationByChallengeID', 'InviteMember', 'ApplicationBasicInfo', 'ApplicationsListDetails', 'Principal', 'UserDetail', 'DeleteInvitedMail', 'ChallengeManager', '$mdDialog'];

    function TeamController($scope, $timeout, $stateParams, $state, Application, UserApplicationByChallengeID, InviteMember, ApplicationBasicInfo, ApplicationsListDetails, Principal, UserDetail, DeleteInvitedMail, ChallengeManager, $mdDialog) {
        var vm = this;
        vm.save = save;
        vm.challengeId = $stateParams.id;
        vm.applicationId = null;
        vm.team = {};
        vm.account = null;
        vm.appDetail = [];
        vm.inviteMails = [];
        vm.members = [];
        vm.deleteMail = [];
        vm.removeMail = removeMail;
        vm.separator = [186, 13, 9]; // semicolon, enter, tab
        vm.removable = false;
        vm.emptySlot = 0;
        vm.maxMember = null;
        vm.showConfirm = showConfirm;
        vm.entity = null;

        


        function removeMail(mail) {
            vm.deleteMail.push(mail);
            vm.members.splice(vm.members.indexOf(mail));
        }

        load();

        function load() {
            ChallengeManager.get({ id: $stateParams.id }, function (result) {
                vm.maxMember = result.maxTeamNumber;
                vm.emptySlot = vm.maxMember;

                vm.entity = UserApplicationByChallengeID.get({ challengeId: $stateParams.id }, function (result) {
                    if (result.applicationId) {
                        vm.removable = true;
                        vm.applicationId = result.applicationId;
                        Application.get({ id: result.applicationId }, function (result) {
                            vm.team = result;
                        });
                        ApplicationBasicInfo.get({ applicationId: result.applicationId }, function (data) {
                            vm.emptySlot = vm.maxMember - data.members.length + 1;
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
            })
        }

        Principal.identity().then(function (account) {
            vm.account = account;
        });

        function showConfirm(ev) {
            // Appending dialog to document.body to cover sidenav in docs app
            var confirm = $mdDialog.confirm()
                .title('Leave team?')
                .textContent('Please confirm that you wish to leave this team and application.')
                .ariaLabel('Leave application')
                .targetEvent(ev)
                .ok('Leave')
                .cancel('Close');

            $mdDialog.show(confirm).then(function () {
                DeleteInvitedMail.delete({ email: vm.account.email, applicationId: vm.applicationId }, onLeaveSuccess, onDeleteError);
            }, function () {
                // $state.go("docs");
            });
        };

        function save() {
            vm.team.challengeId = $stateParams.id;
            vm.team.members = [];
            if (vm.deleteMail) {
                vm.deleteMail.forEach(function (element) {
                    DeleteInvitedMail.delete({ email: element, applicationId: vm.applicationId }, onDeleteSuccess, onDeleteError);
                })
            }

            if (vm.inviteMails) {
                vm.inviteMails.forEach(function (element) {
                    if (element != vm.account.email) {
                        vm.team.members.push(element);
                    }
                })
            }

            if (vm.entity.applicationId) {
                Application.update(vm.team, onSaveSuccess, onSaveError);
            } else {
                Application.save(vm.team, onSaveSuccess, onSaveError);
            }
        }

        function onLeaveSuccess(){
            $state.go("home");
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
