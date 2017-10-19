(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('HostApplicationController', HostApplicationController);


    HostApplicationController.$inject = ['$translate', '$timeout', 'Auth', 'LoginService', '$state', '$rootScope'];

    function HostApplicationController ($translate, $timeout, Auth, LoginService, $state, $rootScope) {
        var vm = this;


    }
})();
