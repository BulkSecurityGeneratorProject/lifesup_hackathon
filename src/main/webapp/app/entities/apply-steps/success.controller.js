(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('SuccessController', SuccessController);

    SuccessController.$inject = ['$scope', '$state', '$stateParams', 'Application'];

    function SuccessController($scope, $state, $stateParams, Application) {
        var vm = this;
        vm.challengeId = $stateParams.id;
    }
})();
