(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('UserListDeleteController',UserListDeleteController);

    UserListDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserList'];

    function UserListDeleteController($uibModalInstance, entity, UserList) {
        var vm = this;

        vm.userList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
