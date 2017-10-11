(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('company-info', {
            parent: 'entity',
            url: '/company-info',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hackathonApp.company.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/company-info/company-info.html',
                    controller: 'CompanyInfoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('company');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }

})();
