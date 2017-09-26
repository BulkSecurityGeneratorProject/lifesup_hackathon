(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengesListDetailController', ChallengesListDetailController);

    ChallengesListDetailController.$inject = ['$stateParams', 'Challenge', 'challenge'];

    function ChallengesListDetailController($stateParams, Challenge, challenge) {
        var vm = this;

        vm.challenge = challenge;
    }
})();
