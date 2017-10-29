(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerDetailController', ChallengeManagerDetailController);

    ChallengeManagerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChallengeManager', 'ChallengeInfo', 'Application', 'Company'];

    function ChallengeManagerDetailController($scope, $rootScope, $stateParams, previousState, entity, ChallengeManager, ChallengeInfo, Application, Company) {
        var vm = this;

        vm.challenge = entity;
        // var now = new Date().getTime();
        // var end = new Date(vm.challenge.info.applicationCloseDate).getTime();
        // var time = end - now;
        // vm.challenge.timeLeft = parseInt(Math.ceil(time / (1000 * 60 * 60 * 24)));
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackathonApp:challengeUpdate', function(event, result) {
            vm.challenge = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
