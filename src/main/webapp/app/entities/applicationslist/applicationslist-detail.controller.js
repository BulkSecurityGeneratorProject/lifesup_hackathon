(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListDetailController', ApplicationsListDetailController);

    ApplicationsListDetailController.$inject = ['$stateParams', 'ApplicationsList', 'entity', 'Principal', 'Challenge', 'UserDetails'];

    function ApplicationsListDetailController($stateParams, ApplicationsList, entity, Principal, Challenge, UserDetails) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.application = entity;
        vm.users = [];
        vm.getUserDetails =getUserDetails;
        vm.determinateValue = 0;
        vm.progressCount = progressCount;

        getUserDetails();

        function getUserDetails() {
            UserDetails.query(function(data) {
                vm.users = data;
                console.log(vm.users);
            })
        }

        function progressCount() {
           return vm.determinateValue += 10;
        }
    }
})();
