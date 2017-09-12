(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeUserApplicationDetailController', ChallengeUserApplicationDetailController);

    ChallengeUserApplicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChallengeUserApplication'];

    function ChallengeUserApplicationDetailController($scope, $rootScope, $stateParams, previousState, entity, ChallengeUserApplication) {
        var vm = this;

        vm.challengeUserApplication = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackathonApp:challengeUserApplicationUpdate', function(event, result) {
            vm.challengeUserApplication = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
