(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeUserApplicationController', ChallengeUserApplicationController);

    ChallengeUserApplicationController.$inject = ['$scope', '$state', 'ChallengeUserApplication'];

    function ChallengeUserApplicationController ($scope, $state, ChallengeUserApplication) {
        var vm = this;
        
        vm.challengeUserApplications = [];

        loadAll();

        function loadAll() {
            ChallengeUserApplication.query(function(result) {
                vm.challengeUserApplications = result;
            });
        }
    }
})();
