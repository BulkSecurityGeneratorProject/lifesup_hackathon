(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceManagerSubmitController', WorkspaceManagerSubmitController);

    WorkspaceManagerSubmitController.$inject = ['$scope', 'entity', '$state', '$stateParams', 'ApplicationByChallengeId', 'WorkspaceOfChallenge', 'Upload', 'TimeServer'];

    function WorkspaceManagerSubmitController($scope, entity, $state, $stateParams, ApplicationByChallengeId, WorkspaceOfChallenge, Upload, TimeServer) {
        var vm = this;
        vm.file = null;
        vm.note = null;
        vm.applicationId = null;
        vm.workspaceId = null;
        vm.isEventEnded = false;

        WorkspaceOfChallenge.get({challengeId: $stateParams.challengeId}, function(result){
            vm.workspaceId = result.id;
            TimeServer.get({}, function(res){
                var t1 = new Date(result.challenge.info.eventEndTime).getTime();
                var t2 = new Date(res.timeServer).getTime();
                if (t1-t2 < 0) vm.isEventEnded = true;
            })
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
                        multipartFile: file,
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
