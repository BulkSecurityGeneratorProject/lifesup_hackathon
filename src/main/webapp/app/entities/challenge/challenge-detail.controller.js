(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeDetailController', ChallengeDetailController);

    ChallengeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Challenge', 'ChallengeInfo', 'Application', 'Company'];

    function ChallengeDetailController($scope, $rootScope, $stateParams, previousState, entity, Challenge, ChallengeInfo, Application, Company) {
        var vm = this;

        vm.challenge = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackathonApp:challengeUpdate', function(event, result) {
            vm.challenge = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
