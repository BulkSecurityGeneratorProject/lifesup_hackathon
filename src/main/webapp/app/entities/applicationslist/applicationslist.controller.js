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

        loadAll();

        function loadAll() {
            ApplicationsList.query(function(data) {
                vm.applications = data;
                console.log(data);
            });
        }
    }
})();
