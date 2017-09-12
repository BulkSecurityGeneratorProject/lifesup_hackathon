(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationController', ApplicationController);

    ApplicationController.$inject = ['$scope', '$state', 'Application'];

    function ApplicationController ($scope, $state, Application) {
        var vm = this;
        
        vm.applications = [];

        loadAll();

        function loadAll() {
            Application.query(function(result) {
                vm.applications = result;
            });
        }
    }
})();
