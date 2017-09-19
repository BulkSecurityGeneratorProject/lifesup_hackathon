(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeController', ChallengeController);

    ChallengeController.$inject = ['$scope', '$state', 'Challenge'];

    function ChallengeController($scope, $state, Challenge) {
        var vm = this;

        vm.challenges = [];
        vm.displayChallenges = [];
        vm.hasNoChallenge = false;
        vm.filter = filter;

        loadAll();

        function loadAll() {
            Challenge.query(function (result) {
                vm.draftChallenges = [];
                vm.activeChallenges = [];
                vm.inactiveChallenges = [];
                vm.challenges = result;
                vm.displayChallenges = result;
                vm.challenges.forEach(function (element) {
                    if (element.info.status == 'DRAFT'){
                        vm.draftChallenges.push(element);
                    }
                    if (element.info.status == 'ACTIVE'){
                        vm.activeChallenges.push(element);
                    }
                    if (element.info.status == 'INACTIVE'){
                        vm.inactiveChallenges.push(element);
                    }
                    var now = new Date().getTime();
                    var applyEnd = new Date(element.info.applyEndTime).getTime();
                    var time = applyEnd - now;
                    if (time <= 0) {
                        element.isClosed = true;
                    }
                    else{
                        element.isClosed = false;
                        element.timeLeft =  parseInt(Math.ceil(time/(1000*60*60*24)));
                    }
                }, this);
                if (!result.length) {
                    vm.hasNoChallenge = true;
                }
            });
        }

        function filter(status) {
            if (status == 'all') {
                vm.displayChallenges = vm.challenges;
                console.log(vm.displayChallenges);
                return;
            }
            if (status == 'active') {
                vm.displayChallenges = vm.activeChallenges;
                console.log(vm.displayChallenges);
                return;
            }
            if (status == 'inactive') {
                vm.displayChallenges = vm.inactiveChallenges;
                console.log(vm.displayChallenges);
                return;
            }
            if (status == 'draft') {
                vm.displayChallenges = vm.draftChallenges;
                console.log(vm.displayChallenges);
                return;
            }
        }
    }
})();
