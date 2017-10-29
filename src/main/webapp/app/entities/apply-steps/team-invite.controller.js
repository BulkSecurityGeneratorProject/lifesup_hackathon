(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamInviteController', TeamInviteController);

    TeamInviteController.$inject = ['$rootScope', '$stateParams', 'Auth', 'Principal', 'ApplicationsListDetails', 'Challenge', 'LoginService', '$state', '$translate', '$timeout', 'AcceptInvitation', 'ApplicationByAcceptKey', 'UserDetail', 'ApplicationBasicInfo', 'ApplicationValidation', 'DeclineInvitation'];

    function TeamInviteController($rootScope, $stateParams, Auth, Principal, ApplicationsListDetails, Challenge, LoginService, $state, $translate, $timeout, AcceptInvitation, ApplicationByAcceptKey, UserDetail, ApplicationBasicInfo, ApplicationValidation, DeclineInvitation) {
        var vm = this;
        vm.login = login;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.rememberMe = true;

        // Reg acc
        vm.register = register;
        vm.requestResetPassword = requestResetPassword;
        vm.registerAccount = {};

        // Reg acc notif
        vm.doNotMatch = null;
        vm.error = null;
        vm.email = null;
        vm.password = null;
        vm.errorUserExists = null;
        vm.authenticationError = false;
        vm.regSuccess = null;

        // Validation
        vm.determinateValue = 0;
        vm.progressCount = progressCount;
        vm.numOfFields = null;

        // Actions for invitation
        vm.acceptInvite = acceptInvite;
        vm.declineInvite = declineInvite;
        vm.accepted = false;
        vm.declined = false;

        vm.reload = reload;
        vm.applicationId = null;
        vm.isInvitedMail = false;
        vm.invitedMail = null;

        vm.account = null;
        Principal.identity().then(function (account) {
            vm.account = account;
        });

        ApplicationByAcceptKey.get({ acceptkey: $stateParams.id }, function (result) {
            vm.invitedMail = result.email;
            console.log(result);
            vm.registerAccount.email = result.email;

            vm.applicationId = result.application.id;
            if (vm.account) {
                if (result.email === vm.account.email) vm.isInvitedMail = true;
            }
            vm.challenge = Challenge.get({ id: result.application.challenge.id });
            console.log(vm.challenge);
            vm.application = ApplicationsListDetails.get({ id: result.application.id }, function (result) {
                vm.members = result.members;
                vm.members.forEach(function (element) {
                    if (element.skills)
                        element.skills = element.skills.split(',');
                }, this);
            });

            ApplicationValidation.query({ applicationId: result.application.id }, function (data) {
                vm.validations = data;
                vm.numOfFields = data.length;
                vm.validations = vm.validations.map(function (item) {
                    return item.split(',');
                })
            })
        });

        function progressCount() {
            return vm.determinateValue += 100 / vm.numOfFields;
        }

        function acceptInvite() {
            console.log("We go here");
            AcceptInvitation.update($stateParams.id, onAcceptSuccess, onError);
        }

        function declineInvite() {
            DeclineInvitation.update($stateParams.id, onDeclineSuccess, onError);
        }
        console.log(vm.isAuthenticated);

        function onAcceptSuccess() {
            vm.accepted = true;
            console.log("Accept Successful");
        }

        function onDeclineSuccess() {
            vm.declined = true;
            $timeout(function () {
                $state.go('challengeslist');
            }, 2000);
            console.log("Decline Successful");
        }

        function onError() {
            alert("Accept or Decline Failed");
        }

        function reload() {
            $state.reload();
        }

        function register() {
            if (vm.registerAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.registerAccount.langKey = $translate.use();
                vm.doNotMatch = null;
                vm.error = null;
                vm.errorUserExists = null;
                vm.errorEmailExists = null;

                Auth.createAccount(vm.registerAccount).then(function () {
                    acceptInvite();
                    vm.regSuccess = 'OK';
                }).catch(function (response) {
                    vm.regSuccess = false;
                    if (response.status === 400 && response.data === 'login already in use') {
                        vm.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        vm.errorEmailExists = 'ERROR';
                    } else {
                        vm.error = 'ERROR';
                    }
                });
            }
        }

        function requestResetPassword() {
            $state.go('requestReset');
        }

        function login(event) {
            event.preventDefault();
            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }).then(function () {
                vm.authenticationError = false;
                $rootScope.$broadcast('authenticationSuccess');
                $state.reload();
            }).catch(function () {
                vm.authenticationError = true;
            });
        }
    }
})();
