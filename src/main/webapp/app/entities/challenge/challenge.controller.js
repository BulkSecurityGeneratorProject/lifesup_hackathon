(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeController', ChallengeController);

    ChallengeController.$inject = ['$scope', '$state', 'Challenge'];

    function ChallengeController ($scope, $state, Challenge) {
        var vm = this;
        
        vm.challenges = [];

        loadAll();

        function loadAll() {
            Challenge.query(function(result) {
                vm.challenges = result;
            });
        }
    }
})();
