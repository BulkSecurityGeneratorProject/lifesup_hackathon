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

        function approve() {
            ApplicationsList.update(function(item) {
                item.status = "APPROVED";
                console.log("something");
            })
        }

        function reject() {
            ApplicationsList.update(function(item) {
                item.status = "REJECTED";
                console.log("something");
            })
        }

    }
})();
