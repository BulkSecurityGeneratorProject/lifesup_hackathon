(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceSubmitController', WorkspaceSubmitController);

    WorkspaceSubmitController.$inject = ['$scope', 'entity', '$state', '$stateParams', 'ApplicationByChallengeId', 'WorkspaceOfChallenge', 'Upload'];

    function WorkspaceSubmitController($scope, entity, $state, $stateParams, ApplicationByChallengeId, WorkspaceOfChallenge, Upload) {
        var vm = this;
        vm.file = null;
        vm.note = null;
        vm.applicationId = null;
        vm.workspaceId = null;

        WorkspaceOfChallenge.get({challengeId: $stateParams.challengeId}, function(res){
            console.log(res);
            vm.workspaceId = res.id;
        });

        ApplicationByChallengeId.get({challengeId: $stateParams.challengeId}, function(res){
            vm.applicationId = res.applicationId;
        });

        $scope.uploadFiles = function (file) {
            $scope.f = file;
            console.log(file);
            if (file) {
              Upload.upload({
                    url: '/api/challenge-submissions-created',
                    data: { 
                        file: file,
                        additionalNote: vm.note,
                        workspaceId: vm.workspaceId,
                        applicationId: vm.applicationId
                    }
                }).then(function (response) {
                    console.log('OK!');
                }, function (response) {
                    if (response.status > 0)
                        $scope.errorMsg = response.status + ': ' + response.data;
                }, function (evt) {
                    file.progress = Math.min(100, parseInt(100.0 *
                        evt.loaded / evt.total));
                });

            }
        }
    }
})();
