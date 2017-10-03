(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListController', ApplicationsListController);

    ApplicationsListController.$inject = ['$scope', '$state', 'ApplicationsList', 'Challenge'];

    function ApplicationsListController ($scope, $state, ApplicationsList, Challenge) {
        var vm = this;
        
        vm.applications = [];
        vm.loadAll = loadAll;
        vm.approve = approve;
        vm.reject = reject;
        

        loadAll();

        function loadAll() {
            ApplicationsList.query(function(data) {
                vm.applications = data;
                console.log(vm.applications);
            });
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
