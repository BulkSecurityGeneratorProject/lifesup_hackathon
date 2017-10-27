(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerController', ChallengeManagerController);

    ChallengeManagerController.$inject = ['$scope', '$state', 'ChallengeManager', 'ChallengeInfo', 'ChallengeByAuthority', 'Challenge', 'ChallengeByUser'];

    function ChallengeManagerController($scope, $state, ChallengeManager, ChallengeInfo, ChallengeByAuthority, Challenge, ChallengeByUser) {
        var vm = this;

        vm.challenges = [];
        vm.displayChallenges = [];
        vm.hasNoChallenge = false;
        vm.filter = filter;

        loadAll();

        function loadAll() {
            ChallengeByUser.query(function (result) {
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
                    if (element.info.status == 'ACTIVE') {
                        // Auto update challenge status on loading
                        var now = new Date().getTime();
                        var date = new Date(element.info.applicationCloseDate);
                        var end = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1).getTime();
                        var diff = end - now;
                        var time = diff / (1000 * 60 * 60 * 24);
                        if (diff < 0) {
                            element.info.status = 'CLOSED';
                            ChallengeInfo.update(element.info);
                            vm.closedChallenges.push(element);
                        } else {
                            vm.activeChallenges.push(element);
                        }

                        if (time <= 1) {
                            element.timeLeft = 'Apply in less than 1 day';
                        } else {
                            element.timeLeft = 'Apply in ' + parseInt(Math.ceil(time)) + ' day(s)';
                        }

                    } else {
                        if (element.info.status == 'CLOSED') {
                            vm.closedChallenges.push(element);
                        }
                        else {
                            vm.draftChallenges.push(element);
                        }
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
