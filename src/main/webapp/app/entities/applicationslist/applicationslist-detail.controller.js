(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListDetailController', ApplicationsListDetailController);

    ApplicationsListDetailController.$inject = ['$stateParams', 'ApplicationsList', 'entity', 'Principal', 'Challenge'];

    function ApplicationsListDetailController($stateParams, ApplicationsList, entity, Principal, Challenge) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.application = entity;

    }
})();
