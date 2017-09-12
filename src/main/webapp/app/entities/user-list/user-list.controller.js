(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('UserListController', UserListController);

    UserListController.$inject = ['$scope', '$state', 'UserList'];

    function UserListController ($scope, $state, UserList) {
        var vm = this;
        
        vm.userLists = [];

        loadAll();

        function loadAll() {
            UserList.query(function(result) {
                vm.userLists = result;
            });
        }
    }
})();
