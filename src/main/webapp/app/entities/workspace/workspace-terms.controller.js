(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceTermsController', WorkspaceTermsController);

    WorkspaceTermsController.$inject = ['$scope', 'entity', '$state', '$stateParams'];

    function WorkspaceTermsController($scope, entity, $state, $stateParams) {
        var vm = this;
        vm.workspace = entity;
    }
})();
