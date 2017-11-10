(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('HostApplicationController', HostApplicationController);


    HostApplicationController.$inject = ['$translate', '$timeout', 'Auth', 'LoginService', '$state', '$rootScope', 'HostRequest'];

    function HostApplicationController ($translate, $timeout, Auth, LoginService, $state, $rootScope, HostRequest) {
        var vm = this;
        vm.host = null;
        vm.save = save;
        vm.success = false;
        vm.failed = false;

        function save(){
            HostRequest.save(vm.host, onSuccess, onError);
        }

        function onSuccess(result){
            vm.success = true;
            console.log(result);
        }
        function onError(result){
            vm.failed = true;
            console.log(result);
        }

    }
})();
