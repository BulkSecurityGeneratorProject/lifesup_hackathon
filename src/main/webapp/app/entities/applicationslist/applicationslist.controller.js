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
            ApplicationsList.query(function(data) {
                data.forEach(function(item) {
                    if (item.id === id) {
                        ApplicationsList.update(vm.app);
                    } else {console.log("not there yet");}                    
                }, this);
            });
        }

        function reject(id) {
            vm.app = {
                id: id,
                status: "REJECTED"
            };
            ApplicationsList.query(function(data) {
                data.forEach(function(item) {
                    if (item.id === id) {
                        ApplicationsList.update(vm.app);
                    } else {console.log("not there yet");}                    
                }, this);
            });
        }
    }
})();
