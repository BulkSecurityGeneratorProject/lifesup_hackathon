(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceController', WorkspaceController);

    WorkspaceController.$inject = ['$scope', 'Principal', 'entity', '$state', '$stateParams', 'ChallengeWorkspaceFeedback', 'ApplicationByChallengeId', '$mdDialog', 'ChallengeWorkspaceQuestion', 'GetWorkspaceQuestion'];

    function WorkspaceController ($scope, Principal, entity, $state, $stateParams, ChallengeWorkspaceFeedback, ApplicationByChallengeId, $mdDialog, ChallengeWorkspaceQuestion, GetWorkspaceQuestion) {
        var vm = this;
        vm.workspace = entity;
        console.log(entity);
        vm.ask = ask;
        vm.askClicked = false;
        vm.rating = ['1','2','3','4','5','6','7','8','9','10'];
        vm.submitFeedback = submitFeedback;
        vm.submitQuestion = submitQuestion;
        vm.allQuestions = {};

        vm.feedback = {};
        vm.question = {};

        ApplicationByChallengeId.get({challengeId: $stateParams.challengeId}, function(res){
            vm.feedback.applicationId = res.applicationId;
            vm.question.applicationId = res.applicationId;
        });

        GetWorkspaceQuestion.query({workspaceId: entity.challenge.workspaceId}, function(res){
            vm.allQuestions = res;
        })

        function ask(){
            vm.askClicked = !vm.askClicked;
        }

        function submitFeedback(){
            ChallengeWorkspaceFeedback.save(vm.feedback, feedbackSuccess , onSaveError);
        }

        function submitQuestion(){
            vm.question.workspaceId = entity.challenge.workspaceId;
            ChallengeWorkspaceQuestion.save(vm.question, questionSuccess, onSaveError);
        }

        function feedbackSuccess(){
            showMessage('Your feedbacks have been sent.')
        }

        function questionSuccess(){
            showMessage('Your question have been sent.')
        }

        function onSaveError(){
            console.log("Error");
        }

        function showMessage(mess) {
            // Appending dialog to document.body to cover sidenav in docs app
            var confirm = $mdDialog.alert()
                .title('Thank you!')
                .textContent(mess)
                .ariaLabel('Send feedback')
                .ok('Got it')

            $mdDialog.show(confirm).then(function () {
                $state.reload();
            }, function () {
                // $state.go("docs");
            });
        };
    }
})();
