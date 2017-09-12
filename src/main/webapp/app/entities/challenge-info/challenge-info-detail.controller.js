(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeInfoDetailController', ChallengeInfoDetailController);

    ChallengeInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChallengeInfo'];

    function ChallengeInfoDetailController($scope, $rootScope, $stateParams, previousState, entity, ChallengeInfo) {
        var vm = this;

        vm.challengeInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackathonApp:challengeInfoUpdate', function(event, result) {
            vm.challengeInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
