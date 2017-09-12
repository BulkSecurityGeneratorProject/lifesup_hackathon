(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('UserListDetailController', UserListDetailController);

    UserListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserList', 'UserInfo', 'Company', 'Application'];

    function UserListDetailController($scope, $rootScope, $stateParams, previousState, entity, UserList, UserInfo, Company, Application) {
        var vm = this;

        vm.userList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackathonApp:userListUpdate', function(event, result) {
            vm.userList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
