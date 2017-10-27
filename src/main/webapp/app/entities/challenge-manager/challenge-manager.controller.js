(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerController', ChallengeManagerController);

    ChallengeManagerController.$inject = ['$scope', '$timeout', '$state', 'ChallengeManager', 'ChallengeInfo', 'ChallengeByAuthority', 'Challenge', 'ChallengeByUser', 'ParseLinks', 'AlertService', '$anchorScroll'];

    function ChallengeManagerController($scope, $timeout, $state, ChallengeManager, ChallengeInfo, ChallengeByAuthority, Challenge, ChallengeByUser, ParseLinks, AlertService, $anchorScroll) {
        var vm = this;

        vm.challenges = [];
        vm.displayChallenges = [];
        vm.hasNoChallenge = false;
        vm.querySearchName = querySearchName;

        // Tuanpm : Object Params to Search Challenge
        vm.challengeSearch = {
            name: null,
            eventStartTime: null,
            eventEndTime: null,
            applicationCloseDate: null,
            status: null
        };

        // Tuanpm : Pageable api + Infinite Scroll
        vm.loadPage = loadPage;
        vm.itemsPerPage = 3;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        function loadAll() {
            ChallengeByUser.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                if (!data.length) {
                    vm.hasNoChallenge = true;
                    return;
                }
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.challenges.push(data[i]);
                }
                vm.challenges.map(function (challenge) {
                    if (challenge.info.status == 'ACTIVE') {
                        // Auto update challenge status on loading
                        var now = new Date().getTime();
                        var date = new Date(challenge.info.applicationCloseDate);
                        var end = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1).getTime();
                        var diff = end - now;
                        var time = diff / (1000 * 60 * 60 * 24);
                        if (diff < 0) {
                            challenge.info.status = 'CLOSED';
                            ChallengeInfo.update(challenge.info);
                        }

                        if (time <= 1) {
                            challenge.timeLeft = 'Apply in less than 1 day';
                        } else {
                            challenge.timeLeft = 'Apply in ' + parseInt(Math.ceil(time)) + ' day(s)';
                        }
                    }
                })
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset() {
            vm.page = 0;
            vm.challenges = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            if (!vm.challengeSearch.name && !vm.challengeSearch.eventStartTime && !vm.challengeSearch.eventEndTime && !vm.challengeSearch.status) {
                loadAll();
            } else {
                querySearchNameContinue();
            }
        }
        loadAll();

        vm.status = null;
        vm.allStatus = [
            { id: 1, value: 'ACTIVE' },
            { id: 2, value: 'DRAFT' },
            { id: 3, value: 'CLOSED' }
        ];

        function querySearchName() {
            vm.page = 0;
            vm.challenges = [];
            ChallengeByUser.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                name: vm.challengeSearch.name ? vm.challengeSearch.name : null,
                eventStartTime: vm.challengeSearch.eventStartTime ? moment(vm.challengeSearch.eventStartTime).format("YYYY-MM-DD HH:mm:ss") : null,
                eventEndTime: vm.challengeSearch.eventEndTime ? moment(vm.challengeSearch.eventEndTime).format("YYYY-MM-DD HH:mm:ss") : null,
                applicationCloseDate: vm.challengeSearch.applicationCloseDate ? vm.challengeSearch.applicationCloseDate : null,
                status: vm.challengeSearch.status ? vm.challengeSearch.status : null
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.challenges.push(data[i]);
                }
                vm.challenges.map(function (challenge) {
                    if (challenge.info.status == 'ACTIVE') {
                        // Auto update challenge status on loading
                        var now = new Date().getTime();
                        var date = new Date(challenge.info.applicationCloseDate);
                        var end = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1).getTime();
                        var diff = end - now;
                        var time = diff / (1000 * 60 * 60 * 24);
                        if (diff < 0) {
                            challenge.info.status = 'CLOSED';
                            ChallengeInfo.update(challenge.info);
                        }

                        if (time <= 1) {
                            challenge.timeLeft = 'Apply in less than 1 day';
                        } else {
                            challenge.timeLeft = 'Apply in ' + parseInt(Math.ceil(time)) + ' day(s)';
                        }
                    }
                })
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }

        }

        function querySearchNameContinue() {
            ChallengeByUser.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                name: vm.challengeSearch.name ? vm.challengeSearch.name : null,
                eventStartTime: vm.challengeSearch.eventStartTime ? moment(vm.challengeSearch.eventStartTime).format("YYYY-MM-DD HH:mm:ss") : null,
                eventEndTime: vm.challengeSearch.eventEndTime ? moment(vm.challengeSearch.eventEndTime).format("YYYY-MM-DD HH:mm:ss") : null,
                applicationCloseDate: vm.challengeSearch.applicationCloseDate ? vm.challengeSearch.applicationCloseDate : null,
                status: vm.challengeSearch.status ? vm.challengeSearch.status : null
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.challenges.push(data[i]);
                }
                vm.challenges.map(function (challenge) {
                    if (challenge.info.status == 'ACTIVE') {
                        // Auto update challenge status on loading
                        var now = new Date().getTime();
                        var date = new Date(challenge.info.applicationCloseDate);
                        var end = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1).getTime();
                        var diff = end - now;
                        var time = diff / (1000 * 60 * 60 * 24);
                        if (diff < 0) {
                            challenge.info.status = 'CLOSED';
                            ChallengeInfo.update(challenge.info);
                        }

                        if (time <= 1) {
                            challenge.timeLeft = 'Apply in less than 1 day';
                        } else {
                            challenge.timeLeft = 'Apply in ' + parseInt(Math.ceil(time)) + ' day(s)';
                        }
                    }
                })
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }

        }
    }
})();
