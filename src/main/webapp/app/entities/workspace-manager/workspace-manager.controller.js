(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceManagerController', WorkspaceManagerController);

    WorkspaceManagerController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$stateParams', 'CreateWorkspaceNews', 'ChallengeWorkspace', 'WorkspaceDetail', 'GetQuestionAnswers', 'ChallengeWorkspaceAnswer'];

    function WorkspaceManagerController($scope, Principal, LoginService, $state, $stateParams, CreateWorkspaceNews, ChallengeWorkspace, WorkspaceDetail, GetQuestionAnswers, ChallengeWorkspaceAnswer) {
        var vm = this;
        vm.workspaceDetail = {};

        // Home
        vm.newsEntity = {};
        vm.news = {};
        vm.postNews = postNews;
        vm.updateNews = updateNews;

        function postNews() {
            if (vm.newsEntity.id) {
                CreateWorkspaceNews.update(vm.newsEntity, function (res) {
                    $state.go($state.current, {challengeId: $stateParams.challengeId}, {reload: true});
                }, function (err) {
                    console.log(err);
                });
            } else {
                CreateWorkspaceNews.save(vm.newsEntity, function (res) {
                    console.log(res);
                    $state.reload();
                }, function (err) {
                    console.log(err);
                });
            }

        }

        function updateNews(news) {
            console.log(news);
            vm.newsEntity = news;
        }
        
        // Question
        vm.showAnswerForm = showAnswerForm;
        vm.answerFormTrigged = false;
        vm.questions = [];
        vm.answer = {};
        vm.postAnswer = postAnswer;
        reloadAnswer();

        function reloadAnswer() {
            WorkspaceDetail.get({ challengeId: $stateParams.challengeId }, function (res) {
                vm.workspaceDetail = res;
                vm.questions = res.workspaceQuestions;
                vm.questions.forEach(element => {
                    element.isAnswered = false;
                    if (element.answers.length > 0) {
                        element.answers.forEach(function (ans) {
                            if (ans.answerByType == 'BY_HOST')
                                element.isAnswered = true;
                        })
                    }
                });
                vm.newsEntity.workspaceId = res.id;
                vm.news = res.workspaceNews;
            })
        }

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

        // Feedback
        // Terms



















    }
})();
