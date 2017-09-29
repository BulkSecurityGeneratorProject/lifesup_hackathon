(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('UserManagementDeleteController', UserManagementDeleteController);

    UserManagementDeleteController.$inject = ['$uibModalInstance', 'entity', 'DeleteUserByID'];

    function UserManagementDeleteController ($uibModalInstance, entity, DeleteUserByID) {
        var vm = this;

        vm.user = entity;
        console.log(entity);
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DeleteUserByID.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
