(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationDetailController', ApplicationDetailController);

    ApplicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Application', 'Challenge', 'UserList'];

    function ApplicationDetailController($scope, $rootScope, $stateParams, previousState, entity, Application, Challenge, UserList) {
        var vm = this;

        vm.application = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackathonApp:applicationUpdate', function(event, result) {
            vm.application = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
