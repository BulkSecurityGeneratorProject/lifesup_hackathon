(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceManagerController', WorkspaceManagerController);

    WorkspaceManagerController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$stateParams'];

    function WorkspaceManagerController ($scope, Principal, LoginService, $state, $stateParams) {
        console.log($stateParams.challengeId);
    }
})();
