(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('ChallengeManagerDetailController', ChallengeManagerDetailController);

    ChallengeManagerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChallengeManager', 'ChallengeInfo', 'Application', 'Company', '$http'];

    function ChallengeManagerDetailController($scope, $rootScope, $stateParams, previousState, entity, ChallengeManager, ChallengeInfo, Application, Company, $http) {
        var vm = this;

        vm.challenge = entity;

        $http({
            url: '/api/challenges/get-banner-base64',
            method: "POST",
            headers: {
                'Content-Type': 'text/plain'
            },
            data: vm.challenge.bannerUrl,
            transformResponse: [function (data) {
                return data;
            }]
        })
            .then(function (response) {
                // success
                if (response.data.length > 1) {
                    vm.challenge.bannerUrl64 = "data:image/jpeg;base64," + response.data;

                } else {
                    vm.challenge.bannerUrl64 = null;
                }
            },
            function (response) { // optional
                // failed
            });
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hackathonApp:challengeUpdate', function (event, result) {
            vm.challenge = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
