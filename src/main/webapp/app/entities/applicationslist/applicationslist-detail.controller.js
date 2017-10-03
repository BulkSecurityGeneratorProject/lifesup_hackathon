(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListDetailController', ApplicationsListDetailController);

    ApplicationsListDetailController.$inject = ['$stateParams', 'ApplicationsList', 'entity', 'Principal', 'UsersInfo', 'ApplicationsListDetails'];

    function ApplicationsListDetailController($stateParams, ApplicationsList, entity, Principal, UsersInfo, ApplicationsListDetails) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.application = entity;
        vm.users = [];
        vm.getUsers = getUsers;
        vm.determinateValue = 0;
        vm.progressCount = progressCount;
        vm.skills = [];
        vm.approve = approve;
        vm.reject = reject;

        getUsers();

        function getUsers() {
            UsersInfo.query(function(data) {
                vm.users = data;
                console.log(vm.users);
                vm.users.map(function(user) {
                    if (user.userInfo) {
                    vm.skills = user.userInfo.skills.split(',');
                    }
                });
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
