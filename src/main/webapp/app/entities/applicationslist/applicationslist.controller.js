(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListController', ApplicationsListController);

    ApplicationsListController.$inject = ['$scope', '$state', 'ApplicationsList', 'Challenge', 'entity', 'ApplicationStatus'];

    function ApplicationsListController ($scope, $state, ApplicationsList, Challenge, entity, ApplicationStatus) {
        var vm = this;

        vm.applications = entity;
        vm.approve = approve;
        vm.reject = reject;

        function approve(application) {
            application.status = 'APPROVED';
            ApplicationStatus.update(application);
        }

        function reject(application) {
            application.status = 'REJECTED';
            ApplicationStatus.update(application);
        }
    }
})();
