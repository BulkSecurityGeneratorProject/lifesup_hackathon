(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('CompanyController', CompanyController);

    CompanyController.$inject = ['$scope', '$state', 'Company'];

    function CompanyController ($scope, $state, Company) {
        var vm = this;
        
        vm.companies = [];
        vm.selected = [];
        vm.showSearchBox=false;
        vm.toggleSearchBox = toggleSearchBox;
        $scope.limitOptions = [10, 20];
        $scope.query = {
            order: 'login',
            limit: 20,
            page: 1
        };
        $scope.toggleLimitOptions = function () {
            $scope.limitOptions = $scope.limitOptions ? undefined : [5, 10, 15];
        };
        $scope.options = {
            rowSelection: true,
            multiSelect: true,
            autoSelect: true,
            decapitate: false,
            largeEditDialog: false,
            boundaryLinks: false,
            limitSelect: true,
            pageSelect: true
        };

        function toggleSearchBox() {
            if (vm.showSearchBox === true) {
                vm.showSearchBox = false;
            } else {
                vm.showSearchBox = true;
            }
        }
        loadAll();

        function loadAll() {
            Company.query(function(result) {
                vm.companies = result;
            });
        }

        $scope.uploadFiles = function (file, errFiles) {
            $scope.f = file;
            $scope.errFile = errFiles && errFiles[0];
            if (file) {
                file.upload = Upload.upload({
                    url: 'api/upload-test',
                    data: { file: file }
                });

                file.upload.then(function (response) {
                    $timeout(function () {
                        file.result = response.data;
                    });
                }, function (response) {
                    if (response.status > 0)
                        $scope.errorMsg = response.status + ': ' + response.data;
                }, function (evt) {
                    file.progress = Math.min(100, parseInt(100.0 *
                        evt.loaded / evt.total));
                });

            }
        }
    }
})();
