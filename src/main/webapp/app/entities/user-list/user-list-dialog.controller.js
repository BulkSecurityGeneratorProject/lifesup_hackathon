(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('UserListDialogController', UserListDialogController);

    UserListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UserList', 'UserInfo', 'Company', 'Application'];

    function UserListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, UserList, UserInfo, Company, Application) {
        var vm = this;

        vm.userList = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.userinfos = UserInfo.query({filter: 'userlist-is-null'});
        $q.all([vm.userList.$promise, vm.userinfos.$promise]).then(function() {
            if (!vm.userList.userInfo || !vm.userList.userInfo.id) {
                return $q.reject();
            }
            return UserInfo.get({id : vm.userList.userInfo.id}).$promise;
        }).then(function(userInfo) {
            vm.userinfos.push(userInfo);
        });
        vm.companies = Company.query();
        vm.applications = Application.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userList.id !== null) {
                UserList.update(vm.userList, onSaveSuccess, onSaveError);
            } else {
                UserList.save(vm.userList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hackathonApp:userListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.birthday = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
