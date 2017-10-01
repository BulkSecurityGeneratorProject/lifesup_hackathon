(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$stateParams', '$state', 'Application', 'entity'];

    function TeamController ($scope, $stateParams, $state, Application, entity) {
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
                vm.team.challengeId = $stateParams.id;
                console.log(vm.team);
                
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
