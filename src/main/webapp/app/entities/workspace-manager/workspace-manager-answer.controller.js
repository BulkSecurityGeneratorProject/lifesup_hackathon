(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceAnswerController', WorkspaceAnswerController);

    WorkspaceAnswerController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function WorkspaceAnswerController ($scope, Principal, LoginService, $state) {
        
    }
})();