(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('CompanyController', CompanyController);

    CompanyController.$inject = ['$scope', '$state', 'Company'];

    function CompanyController ($scope, $state, Company) {
        var vm = this;
        
        vm.companies = [];

        loadAll();

        function loadAll() {
            Company.query(function(result) {
                vm.companies = result;
            });
        }
    }
})();
