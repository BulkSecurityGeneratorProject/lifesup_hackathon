(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerDetailController', ChallengeManagerDetailController);

    ChallengeManagerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChallengeManager', 'ChallengeInfo', 'Application', 'Company'];

    function ChallengeManagerDetailController($scope, $rootScope, $stateParams, previousState, entity, ChallengeManager, ChallengeInfo, Application, Company) {
        var vm = this;

        vm.challenge = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackathonApp:challengeUpdate', function(event, result) {
            vm.challenge = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
