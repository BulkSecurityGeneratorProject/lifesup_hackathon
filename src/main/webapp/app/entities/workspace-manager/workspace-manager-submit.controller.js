(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceManagerSubmitController', WorkspaceManagerSubmitController);

    WorkspaceManagerSubmitController.$inject = ['$scope', 'entity', '$state', '$stateParams', 'ApplicationByChallengeId', 'WorkspaceOfChallenge', 'Upload', 'DownloadSubmission', 'GetSubmissionByApplicationId', 'WorkspaceDetail', 'ApplicationBasicInfo'];

    function WorkspaceManagerSubmitController($scope, entity, $state, $stateParams, ApplicationByChallengeId, WorkspaceOfChallenge, Upload, DownloadSubmission, GetSubmissionByApplicationId, WorkspaceDetail, ApplicationBasicInfo) {
        var vm = this;
        vm.allSubmit = [];


        WorkspaceDetail.get({ challengeId: $stateParams.challengeId }, function (res) {
            res.challengeSubmissionDTOs.forEach(function (element) {
                var submit = {};
                submit.filePath = element.filePath;
                submit.additionalNote = element.additionalNote;
                ApplicationBasicInfo.get({ applicationId: element.applicationId }, function (res) {
                    submit.teamName = res.teamName;
                })
                vm.allSubmit.push(submit);
            });
        })


        vm.downloadSubmission = downloadSubmission;
        function downloadSubmission(filePath) {
            DownloadSubmission.save(filePath, function (result) {
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

        $scope.limitOptions = [10, 20];
        $scope.query = {
            order: 'login',
            limit: 20,
            page: 1
        };
        $scope.toggleLimitOptions = function () {
            $scope.limitOptions = $scope.limitOptions ? undefined : [5, 10, 15];
        };
        $scope.options = {
            rowSelection: true,
            multiSelect: true,
            autoSelect: true,
            decapitate: false,
            largeEditDialog: false,
            boundaryLinks: false,
            limitSelect: true,
            pageSelect: true
        };

    }
})();
