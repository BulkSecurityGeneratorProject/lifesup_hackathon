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
            });
        }

        function approve(id) {
            vm.app = {
                id: id,
                status: "APPROVED"
            };
            ApplicationsList.update(vm.app);
        }

        function reject(id) {
            vm.app = {
                id: id,
                status: "REJECTED"
            };
            ApplicationsList.update(vm.app);
        }
    }
})();
