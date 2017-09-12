'use strict';

describe('Controller Tests', function() {

    describe('UserList Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserList, MockUserInfo, MockCompany, MockApplication;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserList = jasmine.createSpy('MockUserList');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            MockCompany = jasmine.createSpy('MockCompany');
            MockApplication = jasmine.createSpy('MockApplication');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserList': MockUserList,
                'UserInfo': MockUserInfo,
                'Company': MockCompany,
                'Application': MockApplication
            };
            createController = function() {
                $injector.get('$controller')("UserListDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hackathonApp:userListUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
