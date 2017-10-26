(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('LoginSignupController', LoginSignupController);


    LoginSignupController.$inject = ['$translate', '$timeout', 'Auth', 'LoginService', '$state', '$rootScope', 'Principal'];

    function LoginSignupController($translate, $timeout, Auth, LoginService, $state, $rootScope, Principal) {
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
        vm.credentials = {};
        vm.registerAccount = {};
        vm.success = null;
        vm.loginAccount = loginAccount;
        vm.rememberMe = true;

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
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
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

                        Principal.identity().then(function (account) {
                            vm.account = account;
                            console.log(vm.account);
                        });

                        previousState.name = "challengeslist";

                    }
                    $state.go(previousState.name, previousState.params);
                }
                else {
                    Principal.identity().then(function (account) {
                        vm.account = account;
                        console.log(vm.account);
                        if (vm.account.authorities.indexOf('ROLE_ADMIN') !== -1 || vm.account.authorities.indexOf('ROLE_HOST') !== -1)
                            $state.go("challenge-manager");
                        else {
                            $state.go("challengeslist");
                        }
                    });

                }
            }).catch(function () {
                vm.authenticationError = true;
            });
        }
    }
})();
