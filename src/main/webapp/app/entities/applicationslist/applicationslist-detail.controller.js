(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListDetailController', ApplicationsListDetailController);

    ApplicationsListDetailController.$inject = ['$stateParams', 'ApplicationsList', 'entity', 'Principal', 'Challenge', 'UserAccount'];

    function ApplicationsListDetailController($stateParams, ApplicationsList, entity, Principal, Challenge, UserAccount) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.application = entity;
        vm.user = [];
        vm.getAccount =getAccount;
        vm.determinateValue = 0;
        vm.progressCount = progressCount;
        vm.skills = [];
        vm.challenge = entity;

        getAccount();

        function getAccount() {
            UserAccount.query(function(data) {
                vm.user = data;
                console.log(vm.user);
                if (vm.user.userInfo) {
                    vm.skills = vm.user.userInfo.skills.split(',');
                }
            })
        }

        function progressCount() {
           return vm.determinateValue += 10;
        }

     
        

    }
})();
