(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .controller('WorkspaceManagerFeedbackController', WorkspaceManagerFeedbackController);

    WorkspaceManagerFeedbackController.$inject = ['$scope', 'ApplicationBasicInfo', 'entity', '$state', '$stateParams', 'FeedbackByChallenge', '$mdDialog'];

    function WorkspaceManagerFeedbackController($scope, ApplicationBasicInfo, entity, $state, $stateParams, FeedbackByChallenge, $mdDialog) {
        var vm = this;
        vm.feedbacks = [];
        vm.selected = [];
        FeedbackByChallenge.query({challengeId: $stateParams.challengeId}, function(res){
            vm.feedbacks = res;
            vm.feedbacks.forEach(function(element) {
                ApplicationBasicInfo.get({applicationId: element.applicationId}, function(re){
                    element.teamName = re.teamName;
                })
                
            });
        }, function(err){
            console.log(err);
        });

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

       
    }
})();
