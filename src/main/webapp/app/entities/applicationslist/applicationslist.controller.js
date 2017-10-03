(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListController', ApplicationsListController);

    ApplicationsListController.$inject = ['$scope', '$state', 'ApplicationsList', 'Challenge', 'entity' ];

    function ApplicationsListController ($scope, $state, ApplicationsList, Challenge, entity) {
        var vm = this;
        
        vm.applications = entity;
        vm.loadAll = loadAll;
        vm.approve = approve;
        vm.reject = reject;

        function approve(id) {
            vm.application = ApplicationsList.get({id: id}, function(result){
                vm.application.status = 'APPROVED';
                vm.application.challengeId = result.challenge.id;
                ApplicationsList.update(vm.application);
            })
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
