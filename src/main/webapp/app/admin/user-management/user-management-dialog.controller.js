(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('UserManagementDialogController',UserManagementDialogController);

    UserManagementDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'User', 'JhiLanguageService', 'CompanyName'];

    function UserManagementDialogController ($stateParams, $uibModalInstance, entity, User, JhiLanguageService, CompanyName) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_HOST'];
        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.user = entity;
        vm.companies = [];
        vm.complete = complete;
        vm.fillTextbox = fillTextbox;
        vm.company = "";

        getCompanyName();

        function getCompanyName() {
            CompanyName.query(function(data) {
                return vm.companies = data;
            })
        }

        function complete(string){
            vm.hidethis = false;
            if (vm.company.length === 0) {
                vm.hidethis = true;
            }
            var output = [];
            angular.forEach(vm.companies, function(company){
                if(company.toLowerCase().indexOf(string.toLowerCase()) >= 0) {
                   output.push(company);
                }
            });
            vm.filterCompany = output;
        }

        function fillTextbox(string){
             vm.company = string;
             vm.hidethis = true;
        }

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save () {
            vm.isSaving = true;
            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }
    }
})();
