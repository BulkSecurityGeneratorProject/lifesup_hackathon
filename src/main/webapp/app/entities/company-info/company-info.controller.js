(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('CompanyInfoController', CompanyInfoController);

    CompanyInfoController.$inject = ['$scope', '$state', 'CompanyInfo'];

    function CompanyInfoController ($scope, $state, CompanyInfo) {
        var vm = this;
        vm.companyInfo = {};
        vm.getCompanyInfo = getCompanyInfo;

        getCompanyInfo();

        function getCompanyInfo() {
            CompanyInfo.query(function(result) {
                vm.companyInfo = result;
                console.log(vm.companyInfo);
            });
        }
    }
})();
