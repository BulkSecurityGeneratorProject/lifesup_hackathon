(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamInviteController', TeamInviteController);

    TeamInviteController.$inject = ['$rootScope', '$stateParams', 'Auth', 'Principal', 'ApplicationsListDetails', 'Challenge', 'LoginService', '$state', '$translate', '$timeout', 'AcceptInvitation', 'ApplicationByAcceptKey', 'UserDetail', 'ApplicationBasicInfo', 'ApplicationValidation'];

    function TeamInviteController($rootScope, $stateParams, Auth, Principal, ApplicationsListDetails, Challenge, LoginService, $state, $translate, $timeout, AcceptInvitation, ApplicationByAcceptKey, UserDetail, ApplicationBasicInfo, ApplicationValidation) {
        var vm = this;
        vm.login = login;
        vm.isAuthenticated = Principal.isAuthenticated;

        vm.determinateValue = 0;
        vm.progressCount = progressCount;

        // Reg acc notif
        vm.doNotMatch = null;
        vm.error = null;
        vm.email = null;
        vm.password = null;
        vm.errorUserExists = null;
        vm.authenticationError = false;
        vm.success = null;

        vm.register = register;
        vm.requestResetPassword = requestResetPassword;
        vm.registerAccount = {};
        // vm.loginAccount = loginAccount;

        vm.acceptInvite = acceptInvite;
        vm.declineInvite = declineInvite;


        ApplicationByAcceptKey.get({ acceptkey: $stateParams.id }, function (result) {
            vm.challenge = Challenge.get({ id: result.application.challenge.id }, function (result) {
            });
            vm.application = ApplicationsListDetails.get({ id: result.application.id }, function (result) {
                vm.members = result.members;
                vm.members.forEach(function(element) {
                    if (element.skills)
                    element.skills = element.skills.split(',');
                }, this);
            });

            ApplicationValidation.query({ applicationId: result.application.id }, function (data) {
                vm.validations = data;
                vm.validations = vm.validations.map(function (item) {
                    return item.split(',');
                })
            })
        });



        UserDetail.query(function (data) {
            return vm.userInfo = data;
        });

        function progressCount() {
            return vm.determinateValue += 10;
        }

        $timeout(function () { angular.element('#username').focus(); });

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
                    vm.success = 'OK';
                }).catch(function (response) {
                    vm.success = null;
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

        function login(event) {
            event.preventDefault();
            Auth.login({
                username: vm.username,
                password: vm.password,
            }).then(function () {
                vm.authenticationError = false;
                if ($state.current.name === 'register' || $state.current.name === 'activate' ||
                    $state.current.name === 'finishReset' || $state.current.name === 'requestReset') {
                    $state.go('home');
                }
                $rootScope.$broadcast('authenticationSuccess');
                $state.reload();
            }).catch(function () {
                vm.authenticationError = true;
            });
        }

        function requestResetPassword() {
            $state.go('requestReset');
        }

        function acceptInvite() {
            var temp = $stateParams.id + ",true";
            AcceptInvitation.update(temp, onSuccess, onError);
        }

        function declineInvite() {
            var temp = $stateParams.id + ",false";
            console.log(temp);
            AcceptInvitation.update({acceptKey: temp}, onSuccess, onError);
        }

        function onSuccess() {
            console.log("Accept or Decline Successful");
        }
        function onError() {
            console.log("Accept or Decline Failed");
        }

    }
})();
