(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceController', WorkspaceController);

    WorkspaceController.$inject = ['$scope', 'Principal', 'entity', '$state', '$stateParams', 'ChallengeWorkspaceFeedback', 'ApplicationByChallengeId', '$mdDialog'];

    function WorkspaceController ($scope, Principal, entity, $state, $stateParams, ChallengeWorkspaceFeedback, ApplicationByChallengeId, $mdDialog) {
        var vm = this;
        vm.workspace = entity;
        console.log(entity)
        vm.rating = ['1','2','3','4','5','6','7','8','9','10'];
        vm.feedback = {};
        ApplicationByChallengeId.get({challengeId: $stateParams.challengeId}, function(res){
            vm.feedback.applicationId = res.applicationId;
        });

        vm.submit = submit;

        function submit(){
            ChallengeWorkspaceFeedback.save(vm.feedback, showMessage(), console.log("Error"));
            
        }

        function showMessage(ev) {
            // Appending dialog to document.body to cover sidenav in docs app
            var confirm = $mdDialog.alert()
                .title('Thank you!')
                .textContent('Your feedbacks have been sent.')
                .ariaLabel('Send feedback')
                .targetEvent(ev)
                .ok('Got it')

            $mdDialog.show(confirm).then(function () {
                $state.reload();
            }, function () {
                // $state.go("docs");
            });
        };
    }
})();
