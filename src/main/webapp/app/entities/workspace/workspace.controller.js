(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceController', WorkspaceController);

    WorkspaceController.$inject = ['$scope', 'GetQuestionAnswers', 'entity', '$state', '$stateParams', 'ChallengeWorkspaceFeedback', 'ApplicationByChallengeId', '$mdDialog', 'ChallengeWorkspaceQuestion', 'GetWorkspaceQuestion', 'WorkspaceDetail', 'ChallengeWorkspaceAnswer', 'Upload', '$timeout'];

    function WorkspaceController($scope, GetQuestionAnswers, entity, $state, $stateParams, ChallengeWorkspaceFeedback, ApplicationByChallengeId, $mdDialog, ChallengeWorkspaceQuestion, GetWorkspaceQuestion, WorkspaceDetail, ChallengeWorkspaceAnswer, Upload, $timeout) {
        var vm = this;
        vm.workspace = entity;
        console.log(entity.id);
        vm.applicationId = null;
        // Home
        vm.showAskForm = showAskForm;
        vm.askFormTrigged = false;
        vm.submitQuestion = submitQuestion;
        vm.question = {};

        function showAskForm() {
            vm.askFormTrigged = !vm.askFormTrigged;
        }

        function submitQuestion() {
            vm.question.workspaceId = entity.challenge.workspaceId;
            console.log(vm.question);
            ChallengeWorkspaceQuestion.save(vm.question, questionSuccess, onSaveError);
        }

        function questionSuccess() {
            showMessage('Your question have been sent.')
        }

        // My Team
        // My Question
        vm.showAnswerForm = showAnswerForm;
        vm.answerFormTrigged = false;
        vm.questions = [];
        vm.answer = {};
        vm.postAnswer = postAnswer;
        loadAnswer();

        function showAnswerForm() {
            vm.answerFormTrigged = !vm.answerFormTrigged;
        }

        function postAnswer(id) {
            vm.answer.questionId = id;
            ChallengeWorkspaceAnswer.save(vm.answer, function (res) {
                vm.questions.forEach(element => {
                    if (element.id == id) {
                        element.answers.push(res);
                    }
                });
                vm.answer = {};
            }, function () {
                console.log("Error");
            });
        }

        function loadAnswer() {
            WorkspaceDetail.get({ challengeId: $stateParams.challengeId }, function (res) {
                vm.workspaceDetail = res;
                vm.questions = res.workspaceQuestions;
                vm.news = res.workspaceNews;
                vm.questions.forEach(element => {
                    element.totalcmt = element.answers.length;
                });

            })
        }

        // Feedback
        vm.feedback = {};
        vm.rating = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'];
        vm.submitFeedback = submitFeedback;
        function submitFeedback() {
            ChallengeWorkspaceFeedback.save(vm.feedback, feedbackSuccess, onSaveError);
        }

        function feedbackSuccess() {
            showMessage('Your feedbacks have been sent.')
        }
        // Common
        ApplicationByChallengeId.get({ challengeId: $stateParams.challengeId }, function (res) {
            console.log(res);
            vm.feedback.applicationId = res.applicationId;
            vm.question.applicationId = res.applicationId;
            vm.applicationId = res.applicationId;
        });

        function onSaveError() {
            console.log("Error");
        }

        function showMessage(mess) {
            var confirm = $mdDialog.alert()
                .title('Thank you!')
                .textContent(mess)
                .ariaLabel('Send feedback')
                .ok('Got it')

            $mdDialog.show(confirm).then(function () {
                $state.go($state.current, { challengeId: $stateParams.challengeId }, { reload: true });
            }, function () {
                // $state.go("docs");
            });
        };

        $scope.uploadFiles = function (file, errFiles) {
            $scope.f = file;
            console.log(file);
            $scope.errFile = errFiles && errFiles[0];
            if (file) {
                file.upload = Upload.upload({
                    url: '/api/challenge-submissions-created',
                    data: { 
                        multipartFile: file,
                        additionalNote: "test",
                        applicationId: vm.applicationId,
                        workspace: entity.id
                        }
                });

                file.upload.then(function (response) {
                    $timeout(function () {
                        file.result = response.data;
                    });
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
