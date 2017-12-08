(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceManagerSubmitController', WorkspaceManagerSubmitController);

    WorkspaceManagerSubmitController.$inject = ['$scope', 'entity', '$state', '$stateParams', 'ApplicationByChallengeId', 'WorkspaceOfChallenge', 'Upload', 'TimeServer', 'GetSubmissionByApplicationId', 'WorkspaceDetail', 'ApplicationBasicInfo'];

    function WorkspaceManagerSubmitController($scope, entity, $state, $stateParams, ApplicationByChallengeId, WorkspaceOfChallenge, Upload, TimeServer, GetSubmissionByApplicationId, WorkspaceDetail, ApplicationBasicInfo) {
        var vm = this;
        vm.allSubmit = [];


        WorkspaceDetail.get({ challengeId: $stateParams.challengeId }, function (res) {
            var submit = {};
            res.challengeSubmissionDTOs.forEach(function (element) {
                submit.filePath = element.filePath;
                ApplicationBasicInfo.get({ applicationId: element.applicationId }, function (res) {
                    submit.teamName = res.teamName;
                })
            });
        })

        WorkspaceOfChallenge.get({ challengeId: $stateParams.challengeId }, function (result) {
            TimeServer.get({}, function (res) {
                var t1 = new Date(result.challenge.info.eventEndTime).getTime();
                var t2 = new Date(res.timeServer).getTime();
                if (t1 - t2 < 0) vm.isEventEnded = true;
            })
        });

        vm.downloadSubmission = downloadSubmission;
        function downloadSubmission(filePath) {
            DownloadSubmission.save(filePath, function (result) {
                console.log(result);
                var base64Content = result.base64;
                var byteCharacters = atob(base64Content);
                var byteNumbers = new Array(byteCharacters.length);
                for (var i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                var byteArray = new Uint8Array(byteNumbers);
                var file = new Blob([byteArray], { type: result.filetype });
                if (window.navigator.msSaveOrOpenBlob) // IE10+
                    window.navigator.msSaveOrOpenBlob(file, result.fileName);
                else { // Others
                    // tao the link a tu blob vua tao
                    var a = document.createElement("a"),
                        url = URL.createObjectURL(file);
                    // set href = url tao duoc tu blob
                    a.href = url;
                    a.download = result.fileName;
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

    }
})();
