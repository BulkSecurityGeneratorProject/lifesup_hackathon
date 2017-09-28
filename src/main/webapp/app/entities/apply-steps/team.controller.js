(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$state', 'Application', 'entity'];

    function TeamController ($scope, $state, Application, entity) {
        var vm = this;
        vm.save = save;
        
        vm.entity = entity;
        vm.team = {};

        load()

        function load(){
            if (entity.applicationId){
                Application.get({id: entity.applicationId}, function(result){
                    vm.team = result;
                });
            }
        }

        function save(){
            if (entity.applicationId){
                console.log("Update");
                Application.update(vm.team, onSaveSuccess, onSaveError);
            }
            else{
                console.log("Save");
                Application.save(vm.team, onSaveSuccess, onSaveError);
            }
            
        }

        function onSaveSuccess (result) {
            $state.go('success');
        }

        function onSaveError () {

        }
    }
})();
