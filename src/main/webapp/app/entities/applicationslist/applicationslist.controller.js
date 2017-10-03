(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListController', ApplicationsListController);

    ApplicationsListController.$inject = ['$scope', '$state', 'ApplicationsList', 'Challenge', 'entity' ];

    function ApplicationsListController ($scope, $state, ApplicationsList, Challenge, entity) {
        var vm = this;
        
        vm.applications = entity;
<<<<<<< HEAD
=======
        vm.loadAll = loadAll;
>>>>>>> 9c6eb186b51fd46cacd419193b376ae015530565
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
