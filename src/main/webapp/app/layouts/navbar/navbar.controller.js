(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', 'UserDetail'];

    function NavbarController($state, Auth, Principal, ProfileService, LoginService, UserDetail) {
        var vm = this;
        vm.account = null;
        vm.visibleHost = true;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        ProfileService.getProfileInfo().then(function (response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        load();

        function load() {
            Principal.identity().then(function (account) {
                vm.account = account;
                if (account) {
                    if (account.authorities.indexOf('ROLE_HOST') != -1 || account.authorities.indexOf('ROLE_ADMIN') != -1) {
                        vm.visibleHost = false;
                    } else {
                        vm.visibleHost = true;
                    }
                }
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('challengeslist', {}, { reload: true });
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
