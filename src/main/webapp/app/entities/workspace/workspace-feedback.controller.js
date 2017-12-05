(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceFeedbackController', WorkspaceFeedbackController);

    WorkspaceFeedbackController.$inject = ['$scope', 'GetQuestionAnswers', 'entity', '$state', '$stateParams', 'ChallengeWorkspaceFeedback', 'ApplicationByChallengeId', '$mdDialog', 'ChallengeWorkspaceQuestion', 'GetWorkspaceQuestion', 'WorkspaceDetail', 'ChallengeWorkspaceAnswer', 'Upload', '$timeout', 'Principal'];

    function WorkspaceFeedbackController($scope, GetQuestionAnswers, entity, $state, $stateParams, ChallengeWorkspaceFeedback, ApplicationByChallengeId, $mdDialog, ChallengeWorkspaceQuestion, GetWorkspaceQuestion, WorkspaceDetail, ChallengeWorkspaceAnswer, Upload, $timeout, Principal) {
        var vm = this;
        vm.workspace = entity;
        vm.applicationId = null;
        vm.challengeId = $stateParams.challengeId;
        vm.$state = $state;
        console.log(vm.challengeId);

        Principal.identity().then(function (account) {
            vm.account = account;
        });
        
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
            ChallengeWorkspaceQuestion.save(vm.question, questionSuccess, onSaveError);
        }

        function questionSuccess() {
            vm.question = {};
            vm.answerFormTrigged = false;
            showMessage('Your question have been sent.');
        }

        // My Team
        // My Question
        vm.showAnswerForm = showAnswerForm;
        vm.answerFormTrigged = false;
        vm.questions = [];
        vm.answer = {};
        vm.postAnswer = postAnswer;
        load();

        function showAnswerForm() {
            vm.answerFormTrigged = !vm.answerFormTrigged;
        }

        function postAnswer(id) {
            vm.answer.questionId = id;
            ChallengeWorkspaceAnswer.save(vm.answer, function (res) {
                vm.questions.forEach(function (element) {
                    if (element.id == id) {
                        element.answers.push(res);
                    }
                });
                vm.answer = {};
            }, function () {
                console.log("Error");
            });
        }

        function load() {
            WorkspaceDetail.get({ challengeId: $stateParams.challengeId }, function (res) {
                vm.workspaceDetail = res;
                vm.questions = res.workspaceQuestions;
                vm.news = res.workspaceNews;
                vm.questions.forEach(function (element) {
                    element.totalcmt = element.answers.length;
                });
            })
            
        }

        vm.showPrompt = showPrompt;
        function showPrompt(value) {
            console.log(value);
            var confirm = $mdDialog.prompt()
                .title('Edit your answer!')
                .placeholder('Your answer')
                .ariaLabel('answer')
                .initialValue(value.content)
                .required(true)
                .ok('Save')
                .cancel('Cancel');

            $mdDialog.show(confirm).then(function (result) {
                value.content = result;
                ChallengeWorkspaceAnswer.update(value, function(){
                    load();
                });
                $scope.status = 'You decided to name your dog ' + result + '.';
            }, function () {

            });
        };

        vm.showConfirm = showConfirm;
        function showConfirm(ans, idx) {
            var confirm = $mdDialog.confirm()
                .title('Are you sure delete this answer?')
                .ariaLabel('Delete answer')
                .ok('Yes')
                .cancel('No')

            $mdDialog.show(confirm).then(function () {
                ChallengeWorkspaceAnswer.delete({ id: ans.id }, function () {
                    vm.questions.forEach(function (element) {
                        if (element.id == ans.questionId) {
                            element.answers.splice(idx,1);
                        }
                    });
                })
            }, function () {
                // $state.go("docs");
            });
        };
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
                // $state.go($state.current, { challengeId: $stateParams.challengeId }, { reload: true });
                load();
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
