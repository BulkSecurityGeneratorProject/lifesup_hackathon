(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('QuestionController', QuestionController);

    QuestionController.$inject = ['$scope', 'GetQuestionAnswers', 'entity', '$state', '$stateParams', 'ChallengeWorkspaceFeedback', 'ApplicationByChallengeId', '$mdDialog'];

    function QuestionController($scope, GetQuestionAnswers, entity, $state, $stateParams, ChallengeWorkspaceFeedback, ApplicationByChallengeId, $mdDialog) {
        var vm = this;
        
    }
})();
