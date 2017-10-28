(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengesListController', ChallengesListController);

    ChallengesListController.$inject = ['$log', '$timeout', '$q', 'Principal', 'Challenge', 'ApplicationsByUser', 'ParseLinks', 'paginationConstants', 'AlertService', '$location', '$anchorScroll'];

    function ChallengesListController($log, $timeout, $q, Principal, Challenge, ApplicationsByUser, ParseLinks, paginationConstants, AlertService, $location, $anchorScroll) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.challenges = [];
        vm.loadAll = loadAll;
        vm.getStatus = getStatus;
        vm.filter = {};
        vm.startDate = null;
        vm.endDate = null;
        vm.getChallengeId = getChallengeId;
        vm.getApplicationId = getApplicationId;
        vm.getApplicationStatus = getApplicationStatus;
        vm.getApplicationByUser = getApplicationByUser;
        vm.challengeId = [];
        vm.applicationId = '';
        vm.querySearchName = querySearchName;
        vm.gotoBottom = gotoBottom;

        function gotoBottom() {
            // set the location.hash to the id of
            // the element you wish to scroll to.
            $location.hash('mainFooter');

            // call $anchorScroll()
            $anchorScroll();
        }

        //Tuanpm: Open Search
        vm.openSearch = false;
        vm.toggleOpenSearch = toggleOpenSearch;
        function toggleOpenSearch() {
            vm.openSearch = !vm.openSearch;
            if (!vm.openSearch) {
                reset();
                vm.challengeSearch = {
                    name: null,
                    eventStartTime: null,
                    eventEndTime: null,
                    applicationCloseDate: null,
                    status: null
                };
            }
        }

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

        function sort() {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }

        function parseChallengeStatus(challenges) {
            challenges.map(function (challenge) {
                console.log(challenge);
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

        function loadAll() {
            Challenge.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.challenges.push(data[i]);
                }
                parseChallengeStatus(vm.challenges);
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
            if (!vm.challengeSearch.name && !vm.challengeSearch.eventStartTime && !vm.challengeSearch.eventEndTime) {
                loadAll();
            } else {
                querySearchNameContinue();
            }
        }
        // ----------------------------
        loadAll();
        getApplicationByUser();



        function getApplicationByUser() {
            ApplicationsByUser.query(function (data) {
                vm.applicationsByUser = data;
                getChallengeId();
            });
        }

        function getChallengeId() {
            vm.challengeId = vm.applicationsByUser.map(function (item) {
                return item.challengeId;
            });
        }

        function getApplicationId(id) {
            vm.applicationsByUser.map(function (item) {
                if (item.challengeId === id) {
                    return vm.applicationId = item.applicationId;
                }
                return;
            });
        }

        function getApplicationStatus(challenge) {
            if (vm.challengeId.indexOf(challenge.id) > -1) {
                return "APPLIED";
            } else if (challenge.timeLeft < 0 || challenge.info.status === "CLOSED") {
                return "CLOSED";
            } else {
                return "ACTIVE";
            }
        }

        function getStatus() {
            return (vm.challenges || []).
                map(function (challenge) { return challenge.info.status; }).
                filter(function (cat, idx, arr) { return arr.indexOf(cat) === idx; });
        }

        function querySearchName() {
            vm.page = 0;
            vm.challenges = [];
            Challenge.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                name: vm.challengeSearch.name ? vm.challengeSearch.name : null,
                eventStartTime: vm.challengeSearch.eventStartTime ? moment(vm.challengeSearch.eventStartTime).format("YYYY-MM-DD HH:mm:ss") : null,
                eventEndTime: vm.challengeSearch.eventEndTime ? moment(vm.challengeSearch.eventEndTime).format("YYYY-MM-DD HH:mm:ss") : null,
                applicationCloseDate: vm.challengeSearch.applicationCloseDate ? vm.challengeSearch.applicationCloseDate : null,
                status: vm.challengeSearch.status ? vm.challengeSearch.status : null
            }, onSuccess, onError);


            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.challenges.push(data[i]);
                }
                parseChallengeStatus(vm.challenges);
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }

        }

        function querySearchNameContinue() {
            Challenge.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                name: vm.challengeSearch.name ? vm.challengeSearch.name : null,
                eventStartTime: vm.challengeSearch.eventStartTime ? moment(vm.challengeSearch.eventStartTime).format("YYYY-MM-DD HH:mm:ss") : null,
                eventEndTime: vm.challengeSearch.eventEndTime ? moment(vm.challengeSearch.eventEndTime).format("YYYY-MM-DD HH:mm:ss") : null,
                applicationCloseDate: vm.challengeSearch.applicationCloseDate ? vm.challengeSearch.applicationCloseDate : null,
                status: vm.challengeSearch.status ? vm.challengeSearch.status : null
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.challenges.push(data[i]);
                }
                parseChallengeStatus(vm.challenges);
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


    }

})();
