(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerController', ChallengeManagerController);

    ChallengeManagerController.$inject = ['$scope', '$state', 'ChallengeManager', 'ChallengeInfo', 'ChallengeByAuthority'];

    function ChallengeManagerController($scope, $state, ChallengeManager, ChallengeInfo, ChallengeByAuthority) {
        var vm = this;

        vm.challenges = [];
        vm.displayChallenges = [];
        vm.hasNoChallenge = false;
        vm.filter = filter;

        loadAll();

        function loadAll() {
            ChallengeByAuthority.query(function (result) {
                vm.draftChallenges = [];
                vm.activeChallenges = [];
                vm.closedChallenges = [];
                vm.challenges = result;
                vm.displayChallenges = result;
                if (!result.length) {
                    vm.hasNoChallenge = true;
                    return;
                }
                vm.challenges.forEach(function (element) {
                    if (element.info.status == 'CLOSED') {
                        vm.closedChallenges.push(element);
                    }
                    if (element.info.status == 'DRAFT') {
                        vm.draftChallenges.push(element);
                    }
                    if (element.info.status == 'ACTIVE') {
                        vm.activeChallenges.push(element);
                        var now = new Date().getTime();
                        var end = new Date(element.info.applicationCloseDate).getTime();
                        var time = end - now;
                        if (time < 0) {
                            element.info.status = 'CLOSED';
                            ChallengeInfo.update(element.info);
                        }
                        element.timeLeft = parseInt(Math.ceil(time / (1000 * 60 * 60 * 24)));
                    }
                }, this);
            });
        }

        function filter(status) {
            if (status == 'all') {
                vm.displayChallenges = vm.challenges;
                return;
            }
            if (status == 'active') {
                vm.displayChallenges = vm.activeChallenges;
                return;
            }
            if (status == 'closed') {
                vm.displayChallenges = vm.closedChallenges;
                return;
            }
            if (status == 'draft') {
                vm.displayChallenges = vm.draftChallenges;
                return;
            }
        }
    }
})();
