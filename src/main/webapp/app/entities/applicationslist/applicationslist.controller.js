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

        function reject(id) {
            vm.application = ApplicationsList.get({id: id}, function(result){
                vm.application.status = 'REJECTED';
                vm.application.challengeId = result.challenge.id;
                ApplicationsList.update(vm.application);
            })
        }
    }
})();
