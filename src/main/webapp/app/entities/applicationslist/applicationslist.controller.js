(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListController', ApplicationsListController);

    ApplicationsListController.$inject = ['$scope', '$state', 'ApplicationsList', 'Challenge', 'entity' ];

    function ApplicationsListController ($scope, $state, ApplicationsList, Challenge, entity) {
        var vm = this;

        vm.applications = entity;
        vm.approve = approve;
        vm.reject = reject;

        function approve(application) {
            application.status = 'APPROVED';
            ApplicationsList.update(application);
        }

        function reject(application) {
            application.status = 'REJECTED';
            ApplicationsList.update(application);
        }
    }
})();
