(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListDetailController', ApplicationsListDetailController);

    ApplicationsListDetailController.$inject = ['$stateParams', 'ApplicationsList', 'entity', 'Principal', 'UserAccount', 'ApplicationsListDetails'];

    function ApplicationsListDetailController($stateParams, ApplicationsList, entity, Principal, UserAccount, ApplicationsListDetails) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.application = entity;
        vm.user = [];
        vm.getAccount = getAccount;
        vm.determinateValue = 0;
        vm.progressCount = progressCount;
        vm.skills = [];
        vm.approve = approve;
        vm.reject = reject;

        getAccount();

        function getAccount() {
            UserAccount.query(function(data) {
                vm.user = data;
                if (vm.user.userInfo) {
                    vm.skills = vm.user.userInfo.skills.split(',');
                }
            })
        }

        console.log(vm.application);

        function progressCount() {
           return vm.determinateValue += 10;
        }

        function approve(id) {
            vm.application = ApplicationsList.get({id: id}, function(result){
                vm.application.status = 'APPROVED';
                vm.application.challengeId = result.challenge.id;
                ApplicationsList.update(vm.application);
            })
        }

        function reject(id) {
            vm.application = ApplicationsList.get({id: id}, function(result){
                vm.application.status = 'REJECTED';
                vm.application.challengeId = result.challenge.id;
                ApplicationsList.update(vm.application);
            })
        }
    }
})();
