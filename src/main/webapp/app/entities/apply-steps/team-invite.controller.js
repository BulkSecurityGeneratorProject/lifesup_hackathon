(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamInviteController', TeamInviteController);

    TeamInviteController.$inject = ['$stateParams', 'ApplicationsList', 'Auth', 'Principal', 'ApplicationsListDetails', 'Challenge', 'LoginService', '$state', '$translate', '$timeout', 'AcceptInvitation', 'ApplicationByAcceptKey'];

    function TeamInviteController($stateParams, ApplicationsList, Auth, Principal, ApplicationsListDetails, Challenge, LoginService, $state, $translate, $timeout, AcceptInvitation, ApplicationByAcceptKey) {
        var vm = this;

        vm.application = ApplicationByAcceptKey.get({ acceptKey: $stateParams.id }, function (result) {
            vm.members = result.members;
            vm.members.map(function (member) {
                return vm.skills = member.skills.split(',');
            });
            vm.challenge = Challenge.get({ id: result.challengeId });
        });
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.determinateValue = 0;
        vm.progressCount = progressCount;

        function progressCount() {
            return vm.determinateValue += 10;
        }

        function approve(application) {
            application.status = 'APPROVED';
            ApplicationsListDetails.update(application);
        }

        function reject(application) {
            application.status = 'REJECTED';
            ApplicationsListDetails.update(application);
        }

        vm.doNotMatch = null;
        vm.error = null;
        vm.email = null;
        vm.password = null;
        vm.errorUserExists = null;
        vm.login = login;
        vm.authenticationError = false;
        vm.register = register;
        vm.requestResetPassword = requestResetPassword;
        vm.credentials = {};
        vm.registerAccount = {};
        vm.success = null;
        vm.loginAccount = loginAccount;

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
        function requestResetPassword() {
            $state.go('requestReset');
        }
        function loginAccount() {
            $state.go('register');
        }
        function login(event) {
            event.preventDefault();
            Auth.login({
                email: vm.email,
                password: vm.password,
            }).then(function () {
                vm.authenticationError = false;

                if ($state.current.name === 'register' || $state.current.name === 'activate' ||
                    $state.current.name === 'finishReset' || $state.current.name === 'requestReset') {
                    $state.go('home');
                }

                $rootScope.$broadcast('authenticationSuccess');

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is succesful, go to stored previousState and clear previousState
                if (Auth.getPreviousState()) {
                    var previousState = Auth.getPreviousState();
                    Auth.resetPreviousState();
                    if (previousState.name === "home") {
                        previousState.name = "challengeslist";
                    }
                    $state.go(previousState.name, previousState.params);
                }
                else {
                    $state.go("challengeslist");
                }
            }).catch(function () {
                vm.authenticationError = true;
            });
        }
    }
})();
