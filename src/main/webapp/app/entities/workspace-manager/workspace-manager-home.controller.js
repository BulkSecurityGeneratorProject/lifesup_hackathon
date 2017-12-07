(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceManagerHomeController', WorkspaceManagerHomeController);

    WorkspaceManagerHomeController.$inject = ['$scope', 'WorkspaceOfChallenge', '$state', '$stateParams', 'ChallengeWorkspaceFeedback', 'ApplicationByChallengeId', '$mdDialog', 'DeleteWorkspaceNews', 'CreateWorkspaceNews', 'WorkspaceDetail', 'ChallengeWorkspaceAnswer', 'Upload', '$timeout', 'Principal' , 'TimeServer'];

    function WorkspaceManagerHomeController($scope, WorkspaceOfChallenge, $state, $stateParams, ChallengeWorkspaceFeedback, ApplicationByChallengeId, $mdDialog, DeleteWorkspaceNews, CreateWorkspaceNews, WorkspaceDetail, ChallengeWorkspaceAnswer, Upload, $timeout, Principal, TimeServer) {
        var vm = this;
        
        vm.challengeId = $stateParams.challengeId;
        vm.applicationId = null;
        vm.questions = [];
        vm.workspace = null;
        vm.isEventEnded = false;

        

        // News
        vm.newsEntity = {};
        vm.showNewsForm = showNewsForm
        vm.newsFormTriggered = false;
        vm.postNews = postNews;
        vm.updateNews = updateNews;
        vm.deleteNews = deleteNews;

        function showNewsForm() {
            vm.newsFormTriggered = !vm.newsFormTriggered;
            vm.answerFormTriggered = false;
        }

        function postNews() {
            console.log(vm.newsEntity);
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
            vm.newsFormTriggered = true;
            vm.answerFormTriggered = false;
            vm.newsEntity = news;
        }

        function deleteNews(news) {
            var confirm = $mdDialog.confirm()
                .title('Are you sure delete this news?')
                .ariaLabel('Delete news')
                .ok('Yes')
                .cancel('No')

            $mdDialog.show(confirm).then(function () {
                DeleteWorkspaceNews.delete({id: news.id}, function (res) {
                    $state.go($state.current, {challengeId: $stateParams.challengeId}, {reload: true});
                }, function (err) {
                    console.log(err);
                });
            }, function () {
                // $state.go("docs");
            });
        };


        load();
        function load() {
            WorkspaceDetail.get({ challengeId: $stateParams.challengeId }, function (res) {
                vm.workspaceDetail = res;
                vm.questions = res.workspaceQuestions;
                vm.news = res.workspaceNews;
                vm.newsEntity.workspaceId = res.id;
                vm.questions.forEach(function (element) {
                    element.totalcmt = element.answers.length;
                });
               
            });

            WorkspaceOfChallenge.get({challengeId: $stateParams.challengeId}, function(result){
                vm.workspace = result;
                TimeServer.get({}, function(res){
                    var t1 = new Date(result.challenge.info.eventEndTime).getTime();
                    var t2 = new Date(res.timeServer).getTime();
                    if (t1-t2 < 0) vm.isEventEnded = true;
                })
            });
    
            ApplicationByChallengeId.get({challengeId: $stateParams.challengeId}, function(res){
                vm.applicationId = res.applicationId;
            });
    
            Principal.identity().then(function (account) {
                vm.account = account;
            });
        }

        // My Question
        vm.showAnswerForm = showAnswerForm;
        vm.answerFormTriggered = false;
        vm.postAnswer = postAnswer;
        vm.answer = {};

        function showAnswerForm() {
            reset();
            vm.answerFormTriggered = !vm.answerFormTriggered;
            vm.newsFormTriggered = false;
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


        function reset(){
            vm.newsEntity = {};
            vm.answer = {};
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
