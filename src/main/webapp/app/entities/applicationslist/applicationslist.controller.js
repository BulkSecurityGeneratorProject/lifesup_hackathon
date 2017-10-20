(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationsListController', ApplicationsListController);

    ApplicationsListController.$inject = ['$scope', '$state', 'ApplicationsList', 'Challenge', 'entity', 'ApplicationStatus', 'ApplicationValidation'];

    function ApplicationsListController ($scope, $state, ApplicationsList, Challenge, entity, ApplicationStatus, ApplicationValidation) {
        var vm = this;

        vm.applications = entity;
        vm.approve = approve;
        vm.reject = reject;
        vm.getValidation = getValidation;
        vm.validations = [];
        vm.isReadyToApprove = isReadyToApprove;

        getValidation();

        function approve(application) {
            application.status = 'APPROVED';
            // ApplicationStatus.update(application, onSaveSuccess, onSaveError);
        }

        function reject(application) {
            application.status = 'REJECTED';
            // ApplicationStatus.update(application, onSaveSuccess, onSaveError);
        }

        function onSaveError(){
            console.log("Error");
        }

        function onSaveSuccess(){
            console.log("Success");
        }

        function getValidation() {
          vm.applications.map(function(application) {
            ApplicationValidation.query({applicationId: application.id}, function(validate) {
              vm.validations = validate;
              console.log(vm.validations);
            })
          })
        }

        function isReadyToApprove() {
          var count = 0;
          vm.validations.map(function(item) {
            if (item.slice(-4) === "true") {
              count++;
            }
          })
          if (count === vm.validations.length) {
            return true;
          } else return false;
        }
    }
})();
