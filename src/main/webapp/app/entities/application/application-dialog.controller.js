(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationDialogController', ApplicationDialogController);

    ApplicationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Application', 'Challenge', 'UserList'];

    function ApplicationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Application, Challenge, UserList) {
        var vm = this;

        vm.application = entity;
        vm.clear = clear;
        vm.save = save;
        vm.challenges = Challenge.query();
        vm.userlists = UserList.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.application.id !== null) {
                Application.update(vm.application, onSaveSuccess, onSaveError);
            } else {
                Application.save(vm.application, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackathonApp:applicationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
