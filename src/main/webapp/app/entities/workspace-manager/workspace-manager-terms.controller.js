(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceManagerTermsController', WorkspaceManagerTermsController);

    WorkspaceManagerTermsController.$inject = ['$scope', 'entity', '$state', '$stateParams'];

    function WorkspaceManagerTermsController($scope, entity, $state, $stateParams) {
        var vm = this;
        vm.workspace = entity;
    }
})();
