(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('LoginSignupController', LoginSignupController);


    LoginSignupController.$inject = ['$translate', 'Auth', 'LoginService', '$state', '$rootScope', 'Principal', 'UserDetail'];

    function LoginSignupController($translate, Auth, LoginService, $state, $rootScope, Principal, UserDetail) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.email = null;
        vm.password = null;
        vm.errorUserExists = null;
        vm.login = login;
        vm.authenticationError = false;
        vm.register = register;
        vm.requestResetPassword = requestResetPassword;
        vm.registerAccount = {};
        vm.regSuccess = null;
        vm.rememberMe = true;

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
                    vm.regSuccess = 'OK';
                }).catch(function (response) {
                    vm.regSuccess = null;
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
            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }).then(function () {
                vm.authenticationError = false;

                // $rootScope broadcast to all child $scope ($on) on all controller that user has logged in successfully
                $rootScope.$broadcast('authenticationSuccess');
                UserDetail.get(function (rs) {
                    if (rs.status !== "PROFILE_COMPLETE") {
                        $state.go("settings");
                    } else {
                        // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                        // since login is succesful, go to stored previousState and clear previousState
                        if (Auth.getPreviousState()) {
                            var previousState = Auth.getPreviousState();
                            Auth.resetPreviousState();
                            if (previousState.name === "home") {

                                Principal.identity().then(function (account) {
                                    vm.account = account;
                                });

                                previousState.name = "challengeslist";

                            }
                            $state.go(previousState.name, previousState.params);
                        }
                        else {
                            Principal.identity().then(function (account) {
                                vm.account = account;
                                if (vm.account.authorities.indexOf('ROLE_ADMIN') != -1 || vm.account.authorities.indexOf('ROLE_HOST') != -1)
                                    $state.go("challenge-manager");
                                else {
                                    $state.go("challengeslist");
                                }
                            });

                        }
                    }
                })

            }).catch(function () {
                vm.authenticationError = true;
            });
        }
    }
})();
