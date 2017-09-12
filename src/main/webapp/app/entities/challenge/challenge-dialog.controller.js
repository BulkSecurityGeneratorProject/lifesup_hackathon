(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeDialogController', ChallengeDialogController);

    ChallengeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Challenge', 'ChallengeInfo', 'Application', 'Company'];

    function ChallengeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Challenge, ChallengeInfo, Application, Company) {
        var vm = this;

        vm.challenge = entity;
        vm.clear = clear;
        vm.save = save;
        vm.infos = ChallengeInfo.query({filter: 'challenge-is-null'});
        $q.all([vm.challenge.$promise, vm.infos.$promise]).then(function() {
            if (!vm.challenge.info || !vm.challenge.info.id) {
                return $q.reject();
            }
            return ChallengeInfo.get({id : vm.challenge.info.id}).$promise;
        }).then(function(info) {
            vm.infos.push(info);
        });
        vm.applications = Application.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.challenge.id !== null) {
                Challenge.update(vm.challenge, onSaveSuccess, onSaveError);
            } else {
                Challenge.save(vm.challenge, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackathonApp:challengeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
