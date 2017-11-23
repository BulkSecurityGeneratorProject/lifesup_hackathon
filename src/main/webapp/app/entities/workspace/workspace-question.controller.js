(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceQuestionController', WorkspaceQuestionController);

    WorkspaceQuestionController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function WorkspaceQuestionController ($scope, Principal, LoginService, $state) {
        
    }
})();