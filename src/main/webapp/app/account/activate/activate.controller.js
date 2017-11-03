(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ActivationController', ActivationController);

    ActivationController.$inject = ['$stateParams', 'Auth', 'LoginService', '$timeout', '$state'];

    function ActivationController($stateParams, Auth, LoginService, $timeout, $state) {
        var vm = this;

        Auth.activateAccount({ key: $stateParams.key }).then(function () {
            vm.error = null;
            vm.success = 'OK';
            redirect();
        }).catch(function () {
            vm.success = null;
            vm.error = 'ERROR';
        });

        function redirect() {
            $timeout(function () {
                $state.go('login-signup');
            }, 3500);
        }

        vm.login = LoginService.open;
    }
})();
