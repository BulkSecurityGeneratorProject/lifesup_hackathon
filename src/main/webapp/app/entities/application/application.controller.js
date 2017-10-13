(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ApplicationController', ApplicationController);

    ApplicationController.$inject = ['$scope', '$state', 'ApplicationByCurrentUser', 'Application'];

    function ApplicationController ($scope, $state, ApplicationByCurrentUser, Application) {
        var vm = this;
        
        vm.applications = [];

        loadAll();

        function loadAll() {
            ApplicationByCurrentUser.query(function(result) {
                result.forEach(function(element) {
                    var temp = Application.get({id: element.applicationId});
                    vm.applications.push(temp);
                }, this);
                // vm.applications = result;
            });
        }
    }
})();
