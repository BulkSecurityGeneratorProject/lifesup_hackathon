(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceController', WorkspaceController);

    WorkspaceController.$inject = ['$scope', '$state', '$http', '$stateParams', 'entity', 'application'];

    function WorkspaceController($scope, $state, $http, $stateParams, entity, application) {
        var vm = this;
        if ($state.current.parent != "workspace"){
            $state.go("workspace-home", {reload: true});
        }
        vm.applicationId = application.applicationId;
        vm.challenge = entity.challenge;

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
        },
            function (response) { // optional
                // failed
            });
    }
})();
