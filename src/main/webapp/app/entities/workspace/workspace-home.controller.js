(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceHomeController', WorkspaceHomeController);

    WorkspaceHomeController.$inject = ['$scope', 'entity', '$state', '$stateParams', 'ChallengeWorkspaceFeedback', 'ApplicationByChallengeId', '$mdDialog', 'ChallengeWorkspaceQuestion', 'GetWorkspaceQuestion', 'WorkspaceDetail', 'ChallengeWorkspaceAnswer', 'Upload', '$timeout', 'Principal' , 'application'];

    function WorkspaceHomeController($scope, entity, $state, $stateParams, ChallengeWorkspaceFeedback, ApplicationByChallengeId, $mdDialog, ChallengeWorkspaceQuestion, GetWorkspaceQuestion, WorkspaceDetail, ChallengeWorkspaceAnswer, Upload, $timeout, Principal, application) {
        var vm = this;
        vm.workspace = entity;
        vm.challengeId = $stateParams.challengeId;
        vm.applicationId = application.applicationId;
        vm.questions = [];

        Principal.identity().then(function (account) {
            vm.account = account;
        });

        load();
        function loadCount(){
            vm.questions.forEach(function (element) {
                element.totalcmt = element.answers.length;
            });
        }
        function load() {
            WorkspaceDetail.get({ challengeId: $stateParams.challengeId }, function (res) {
                vm.workspaceDetail = res;
                vm.questions = res.workspaceQuestions;
                vm.news = res.workspaceNews;
                loadCount();
            })
        }
        
        
        // Post Question
        vm.showAskForm = showAskForm;
        vm.askFormTriggered = false;
        vm.submitQuestion = submitQuestion;
        vm.question = {};

        function showAskForm() {
            reset();
            vm.askFormTriggered = true;
        }

        function submitQuestion() {
            vm.question.applicationId = application.applicationId;
            vm.question.workspaceId = entity.challenge.workspaceId;
            if (vm.question.id){
                ChallengeWorkspaceQuestion.update(vm.question, questionUpdated, onSaveError);
            } else {
                ChallengeWorkspaceQuestion.save(vm.question, questionSuccess, onSaveError);
            }
            
        }

        function questionUpdated() {
            reset();
            showMessage('Your question have been updated!');
        }

        function questionSuccess() {
            reset();
            showMessage('Your question have been sent!');
        }

        // My Question
        vm.showAnswerForm = showAnswerForm;
        vm.answerFormTriggered = false;
        vm.postAnswer = postAnswer;
        vm.answer = {};

        function showAnswerForm() {
            reset();
            vm.answerFormTriggered = true;
        }

        function postAnswer(id) {
            vm.answer.questionId = id;
            ChallengeWorkspaceAnswer.save(vm.answer, function (res) {
                vm.questions.forEach(function (element) {
                    element.totalcmt = element.answers.length;
                    if (element.id == id) {
                        element.answers.push(res);
                    }
                });
                vm.answer = {};
            }, function () {
                console.log("Error");
            });
        }

        vm.editComment = editComment;
        function editComment(ans, idx) {
            var confirm = $mdDialog.prompt()
                .title('Edit your answer!')
                .placeholder('Your answer')
                .ariaLabel('answer')
                .initialValue(ans.content)
                .required(true)
                .ok('Save')
                .cancel('Cancel');

            $mdDialog.show(confirm).then(function (result) {
                ans.content = result;
                ChallengeWorkspaceAnswer.update(ans, function(){
                    vm.questions.forEach(function (element) {
                        if (element.id == ans.questionId) {
                            element.answers[idx].content = ans.content;
                        }
                    });
                });
            }, function () {

            });
        };

        vm.deleteComment = deleteComment;
        function deleteComment(ans, idx) {
            console.log(ans);
            console.log(idx);
            var confirm = $mdDialog.confirm()
                .title('Are you sure delete this answer?')
                .ariaLabel('Delete answer')
                .ok('Yes')
                .cancel('No')

            $mdDialog.show(confirm).then(function () {
                ChallengeWorkspaceAnswer.delete({ id: ans.id }, function () {
                    vm.questions.forEach(function (element) {
                        element.totalcmt = element.answers.length;
                        console.log(element.answers);
                        if (element.id == ans.questionId) {
                            
                            element.answers.splice(idx,1);
                        }
                    });
                })
            }, function () {
                // $state.go("docs");
            });
        };

        vm.deleteQuestion = deleteQuestion;
        function deleteQuestion(ans) {
            var confirm = $mdDialog.confirm()
                .title('Are you sure delete this question?')
                .ariaLabel('Delete question')
                .ok('Yes')
                .cancel('No')

            $mdDialog.show(confirm).then(function () {
                ChallengeWorkspaceQuestion.delete({ id: ans.id }, function () {
                    load();
                })
            }, function () {
                // $state.go("docs");
            });
        };

        vm.editQuestion = editQuestion;
        function editQuestion(ans){
            console.log(ans);
            showAskForm();
            vm.question.id = ans.id;
            vm.question.subject = ans.subject;
            vm.question.content = ans.content;
        }


        function reset(){
            vm.question = {};
            vm.answer = {};
            vm.answerFormTriggered = false;
            vm.askFormTriggered = false;
        }
       

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
                $state.reload($state.current);
            }, function () {
            });
        };

       

    }
})();
