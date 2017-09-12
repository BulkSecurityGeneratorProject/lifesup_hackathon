(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeInfoController', ChallengeInfoController);

    ChallengeInfoController.$inject = ['$scope', '$state', 'ChallengeInfo'];

    function ChallengeInfoController ($scope, $state, ChallengeInfo) {
        var vm = this;
        
        vm.challengeInfos = [];

        loadAll();

        function loadAll() {
            ChallengeInfo.query(function(result) {
                vm.challengeInfos = result;
            });
        }
    }
})();
