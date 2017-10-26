(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengesListController', ChallengesListController);

    ChallengesListController.$inject = ['$log', '$timeout', '$q', 'Principal', 'Challenge', 'ApplicationsByUser', 'ParseLinks', 'paginationConstants'];

    function ChallengesListController($log, $timeout, $q, Principal, Challenge, ApplicationsByUser, ParseLinks, paginationConstants) {
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

        //Tuanpm: Open Search
        vm.openSearch = false;
        vm.toggleOpenSearch = toggleOpenSearch;
        function toggleOpenSearch() {
            vm.openSearch = !vm.openSearch;
            if (!vm.openSearch) {
                reset();
                vm.challengeSearch = null;
                loadAll();
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

        function searchAll() {
            Challenge.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                name: vm.challengeSearch.name ? vm.challengeSearch.name : null,
                eventStartTime: vm.challengeSearch.eventStartTime ? vm.challengeSearch.eventEndTime : null,
                eventEndTime: vm.challengeSearch.eventEndTime ? vm.challengeSearch.eventEndTime : null,
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
                vm.challenges = [];
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.challenges.push(data[i]);
                }
                vm.challenges.map(function (challenge) {
                    var today = (new Date()).getTime();
                    var endDate = new Date(challenge.info.applicationCloseDate).getTime();
                    var diff = endDate - today;
                    challenge.timeLeft = parseInt(Math.ceil(diff / (1000 * 60 * 60 * 24)));
                })
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll() {
            Challenge.query({
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
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.challenges.push(data[i]);
                }
                vm.challenges.map(function (challenge) {
                    var today = (new Date()).getTime();
                    var endDate = new Date(challenge.info.applicationCloseDate).getTime();
                    var diff = endDate - today;
                    challenge.timeLeft = parseInt(Math.ceil(diff / (1000 * 60 * 60 * 24)));
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
            if (!vm.challengeSearch.name) {
                loadAll();
            } else {
                querySearchName(vm.challengeSearch.name);
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

            return Challenge.query({
                page: 0,
                size: vm.itemsPerPage,
                name: vm.challengeSearch.name ? vm.challengeSearch.name : null,
                eventStartTime: vm.challengeSearch.eventStartTime ? vm.challengeSearch.eventStartTime : null,
                eventEndTime: vm.challengeSearch.eventEndTime ? vm.challengeSearch.eventEndTime : null,
                applicationCloseDate: vm.challengeSearch.applicationCloseDate ? vm.challengeSearch.applicationCloseDate : null,
                status: vm.challengeSearch.status ? vm.challengeSearch.status : null
            }).$promise.then(function (result) {
                vm.challenges = result;
                vm.challenges.map(function (challenge) {
                    var today = (new Date()).getTime();
                    var endDate = new Date(challenge.info.applicationCloseDate).getTime();
                    var diff = endDate - today;
                    challenge.timeLeft = parseInt(Math.ceil(diff / (1000 * 60 * 60 * 24)));
                })
            });

        }


    }

})();
