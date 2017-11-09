(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerController', ChallengeManagerController);

    ChallengeManagerController.$inject = ['$scope', '$timeout', '$state', 'ChallengeManager', 'ChallengeInfo', 'ChallengeByAuthority', 'Challenge', 'ChallengeByUser', 'ParseLinks', 'AlertService', '$anchorScroll', '$http', 'TimeServer'];

    function ChallengeManagerController($scope, $timeout, $state, ChallengeManager, ChallengeInfo, ChallengeByAuthority, Challenge, ChallengeByUser, ParseLinks, AlertService, $anchorScroll, $http, TimeServer) {
        var vm = this;

        vm.challenges = [];
        vm.allStatus = ['', 'DRAFT', 'ACTIVE', 'INACTIVE', 'CLOSED'];
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
        vm.reverse = true;
        vm.reset = reset;

        loadAll();

        function sort() {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }
        function parseChallengeStatus(challenges) {
            challenges.map(function (challenge) {
                if (challenge.info.status !== 'REMOVED') {

                    TimeServer.get(function(result){
                        var today = new Date(result.timeServer).getTime();

                        var appClose = new Date(challenge.info.applicationCloseDate);
                        var endApp = new Date(appClose.getFullYear(), appClose.getMonth(), 
                        appClose.getDate() + 1).getTime(); 
    
                        var evClose = new Date(challenge.info.eventEndTime);
                        var endEv = new Date(evClose.getFullYear(), evClose.getMonth(), evClose.getDate() + 1).getTime();
    
                        if (endEv - today < 0) {
                            challenge.info.status = 'CLOSED';
                        } else {
                            if (endApp - today < 0) {
                                challenge.info.status = 'INACTIVE';
                            } else {
                                challenge.timeLeft = endApp;
                            }
                        }
                    });
                    // End Time
                }
            })
        }

        function loadAll() {
            var startTime = new Date().getTime();
            ChallengeByUser.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function onSuccess(data, headers) {
                var endTime = new Date().getTime();
                console.log(endTime - startTime);
                if (!data.length) {
                    vm.hasNoChallenge = true;
                    return;
                }
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.challenges.push(data[i]);
                }
                angular.forEach(data, function (challenge) {
                    //Convert Base64 img 
                    $http({
                        url: '/api/challenges/get-banner-base64',
                        method: "POST",
                        headers: {
                            'Content-Type': 'text/plain'
                        },
                        data: challenge.bannerUrl,
                        transformResponse: [function (data) {
                            return data;
                        }]
                    })
                        .then(function (response) {
                            // success
                            if(response.data.length>1){
                                challenge.bannerUrl64 = "data:image/jpeg;base64," + response.data;

                            } else {
                                challenge.bannerUrl64 = null;
                            }
                        },
                        function (response) { // optional
                            // failed
                        });
                });
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
            if (!vm.challengeSearch.name && !vm.challengeSearch.eventStartTime && !vm.challengeSearch.eventEndTime && !vm.challengeSearch.status) {
                loadAll();
            } else {
                querySearchNameContinue();
            }
        }


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

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.challenges.push(data[i]);
                }
                angular.forEach(data, function (challenge) {
                    //Convert Base64 img 
                    $http({
                        url: '/api/challenges/get-banner-base64',
                        method: "POST",
                        headers: {
                            'Content-Type': 'text/plain'
                        },
                        data: challenge.bannerUrl,
                        transformResponse: [function (data) {
                            return data;
                        }]
                    })
                        .then(function (response) {
                            // success
                            if(response.data.length>1){
                                challenge.bannerUrl64 = "data:image/jpeg;base64," + response.data;

                            } else {
                                challenge.bannerUrl64 = null;
                            }
                        },
                        function (response) { // optional
                            // failed
                        });
                });
                parseChallengeStatus(vm.challenges);
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

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.challenges.push(data[i]);
                }
                angular.forEach(data, function (challenge) {
                    //Convert Base64 img 
                    $http({
                        url: '/api/challenges/get-banner-base64',
                        method: "POST",
                        headers: {
                            'Content-Type': 'text/plain'
                        },
                        data: challenge.bannerUrl,
                        transformResponse: [function (data) {
                            return data;
                        }]
                    })
                        .then(function (response) {
                            // success
                            if(response.data.length>1){
                                challenge.bannerUrl64 = "data:image/jpeg;base64," + response.data;

                            } else {
                                challenge.bannerUrl64 = null;
                            }
                        },
                        function (response) { // optional
                            // failed
                        });
                });
                parseChallengeStatus(vm.challenges);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
    }
})();
