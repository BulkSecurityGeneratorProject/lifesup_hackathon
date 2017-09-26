(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengesListDetailController', ChallengesListDetailController);

    ChallengesListDetailController.$inject = ['$stateParams', 'Challenge', 'entity'];

    function ChallengesListDetailController($stateParams, Challenge, entity) {
        var vm = this;

        vm.challenge = entity;

        var today = (new Date()).getTime();
        var endDate = new Date(vm.challenge.info.applicationCloseDate).getTime();
        var diff = endDate - today;
        vm.timeLeft = vm.challenge.timeLeft = parseInt(Math.ceil(diff / (1000 * 60 * 60 * 24)));

    }
})();
