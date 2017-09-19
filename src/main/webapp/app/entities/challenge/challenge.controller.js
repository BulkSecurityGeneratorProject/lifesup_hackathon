(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeController', ChallengeController);

    ChallengeController.$inject = ['$scope', '$state', 'Challenge'];

    function ChallengeController ($scope, $state, Challenge) {
        var vm = this;
        
        vm.challenges = [];
        vm.hasNoChallenge = false;

        loadAll();

        function loadAll() {
            Challenge.query(function(result) {
                vm.challenges = result;
                if (!result.length){
                    vm.hasNoChallenge = true;
                }
            });
        }
    }
})();
