(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListController', ApplicationsListController);

    ApplicationsListController.$inject = ['$scope', '$state', 'ApplicationsList'];

    function ApplicationsListController ($scope, $state, ApplicationsList) {
        var vm = this;
        
        vm.applications = [];
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ApplicationsList.query(function(data) {
                vm.applications = data;
            });
        }
    }
})();
