(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceManagerController', WorkspaceManagerController);

    WorkspaceManagerController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$stateParams', 'CreateWorkspaceNews', 'ChallengeWorkspace', 'WorkspaceDetail', 'GetQuestionAnswers', 'ChallengeWorkspaceAnswer'];

    function WorkspaceManagerController ($scope, Principal, LoginService, $state, $stateParams, CreateWorkspaceNews, ChallengeWorkspace, WorkspaceDetail, GetQuestionAnswers, ChallengeWorkspaceAnswer) {
        var vm = this;
        vm.showNewsForm = showNewsForm;
        vm.newsFormTrigged = false;
        vm.showAnswerForm = showAnswerForm;
        vm.answerFormTrigged = false;

        vm.workspaceDetail = {};
        vm.newsEntity = {};
        
        vm.news = {};
        vm.questions = [];
        vm.answer = {};
        
        vm.save = save;
        vm.postAnswer = postAnswer;
        
        reloadAnswer();

        function reloadAnswer(){
            WorkspaceDetail.get({challengeId: $stateParams.challengeId}, function(res){
                vm.workspaceDetail = res;
                vm.questions = res.workspaceQuestions;
                vm.questions.forEach(element => {
                    GetQuestionAnswers.get({id: element.id}, function(ans){
                        console.log(ans.answers);
                        element.answers = ans.answers;
                    })
                });
                vm.newsEntity.workspaceId = res.id;
                vm.news = res.workspaceNews;
            })
        }

        function showNewsForm(){
            vm.newsFormTrigged = !vm.newsFormTrigged;
        }

        function save(){
            if (vm.newsEntity.id){
                CreateWorkspaceNews.update(vm.newsEntity, function(res){
                    console.log(res);
                    $state.reload();
                }, function(err){
                    console.log(err);
                });
            } else {
                CreateWorkspaceNews.save(vm.newsEntity, function(res){
                    console.log(res);
                    $state.reload();
                }, function(err){
                    console.log(err);
                });
            }
            
        }

        function showAnswerForm(){
            vm.answerFormTrigged = !vm.answerFormTrigged;
        }

        function postAnswer(id){
            vm.answer.questionId = id;
            ChallengeWorkspaceAnswer.save(vm.answer, function(res){
                vm.questions.forEach(element => {
                    if (element.id == id){
                        element.answers.push(res);
                    }
                });
                vm.answer = {};
            }, function(){
                console.log("Error");
            });
        }

        vm.editNews = editNews;
        function editNews(news){
            console.log(news);
            vm.newsEntity = news;
        }


    }
})();
