(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceManagerController', WorkspaceManagerController);

    WorkspaceManagerController.$inject = ['$scope', '$state', '$http', '$stateParams', 'entity', 'application', 'WorkspaceOfChallenge', 'ApplicationByChallengeId'];

    function WorkspaceManagerController($scope, $state, $http, $stateParams, entity, application, WorkspaceOfChallenge, ApplicationByChallengeId) {
        var vm = this;
        
        vm.applicationId = application.applicationId;
        
        ApplicationByChallengeId.get({challengeId: $stateParams.challengeId}, function(res){
            vm.applicationId = res.applicationId;
        });

        vm.challenge = entity.challenge;
        WorkspaceOfChallenge.get({challengeId: $stateParams.challengeId}, function(res){
            vm.challenge = res.challenge;

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
            }).then(function (response) {
                // success
                if (response.data.length > 1) {
                    vm.challenge.bannerUrl64 = "data:image/jpeg;base64," + response.data;
    
                } else {
                    vm.challenge.bannerUrl64 = null;
                }
                if ($state.current.parent != "workspace-manager"){
                    $state.go("workspace-manager-home", {reload: true});
                }
            },
                function (response) { // optional
                    // failed
                });
        });

        
    }
})();
