(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceController', WorkspaceController);

    WorkspaceController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$stateParams'];

    function WorkspaceController ($scope, Principal, LoginService, $state, $stateParams) {
        console.log($stateParams.challengeId);
    }
})();
