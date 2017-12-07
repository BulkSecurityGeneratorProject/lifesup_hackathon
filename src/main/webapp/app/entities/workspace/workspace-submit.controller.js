(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceSubmitController', WorkspaceSubmitController);

    WorkspaceSubmitController.$inject = ['$scope', 'entity', '$state', '$stateParams', 'ApplicationByChallengeId', 'WorkspaceOfChallenge', 'Upload', 'TimeServer', 'GetSubmissionByApplicationId'];

    function WorkspaceSubmitController($scope, entity, $state, $stateParams, ApplicationByChallengeId, WorkspaceOfChallenge, Upload, TimeServer, GetSubmissionByApplicationId) {
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
            GetSubmissionByApplicationId.query({applicationId: vm.applicationId}, function(res){
                console.log(res);
            })
        });

        vm.downloadSubmission = downloadSubmission;
        function downloadSubmission(attachId) {
            AttachDownload.get({ id: attachId }, function (result) {
                var base64Content = result.base64;
                var byteCharacters = atob(base64Content);
                var byteNumbers = new Array(byteCharacters.length);
                for (var i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                var byteArray = new Uint8Array(byteNumbers);
                var file = new Blob([byteArray], { type: result.filetype });
                if (window.navigator.msSaveOrOpenBlob) // IE10+
                    window.navigator.msSaveOrOpenBlob(file, result.filename);
                else { // Others
                    // tao the link a tu blob vua tao
                    var a = document.createElement("a"),
                        url = URL.createObjectURL(file);
                    // set href = url tao duoc tu blob
                    a.href = url;
                    a.download = result.filename;
                    // append the a vao body
                    document.body.appendChild(a);
                    // click the a ==> click href url download
                    a.click();
                    setTimeout(function () {
                        // xoa the a khi dowload xong
                        document.body.removeChild(a);

                        // goi lai trang
                        window.URL.revokeObjectURL(url);
                    }, 0);
                }
            })
        }

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
