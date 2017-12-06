(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('CompanyController', CompanyController);

    CompanyController.$inject = ['$scope', '$state', 'Company', 'Upload'];

    function CompanyController ($scope, $state, Company, Upload) {
        var vm = this;
        
        vm.companies = [];
        vm.selected = [];
        vm.showSearchBox=false;
        vm.toggleSearchBox = toggleSearchBox;
        $scope.limitOptions = [10, 20];
        $scope.query = {
            order: 'login',
            limit: 20,
            page: 1
        };
        $scope.toggleLimitOptions = function () {
            $scope.limitOptions = $scope.limitOptions ? undefined : [5, 10, 15];
        };
        $scope.options = {
            rowSelection: true,
            multiSelect: true,
            autoSelect: true,
            decapitate: false,
            largeEditDialog: false,
            boundaryLinks: false,
            limitSelect: true,
            pageSelect: true
        };

        function toggleSearchBox() {
            if (vm.showSearchBox === true) {
                vm.showSearchBox = false;
            } else {
                vm.showSearchBox = true;
            }
        }
        loadAll();

        function loadAll() {
            Company.query(function(result) {
                vm.companies = result;
            });
        }

    }
})();
