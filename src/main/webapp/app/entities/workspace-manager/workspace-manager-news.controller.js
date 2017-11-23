(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceNewsController', WorkspaceNewsController);

    WorkspaceNewsController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function WorkspaceNewsController ($scope, Principal, LoginService, $state) {
        
    }
})();