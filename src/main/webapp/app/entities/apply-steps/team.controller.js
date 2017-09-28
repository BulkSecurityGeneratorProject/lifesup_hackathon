(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$state', 'Application', 'entity'];

    function TeamController ($scope, $state, Application, entity) {
        var vm = this;
        vm.save = save;
        
        vm.team = entity;

        function save(){
            console.log(vm.team);
            Application.save(vm.team, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            $state.go('success');
        }

        function onSaveError () {

        }
    }
})();
