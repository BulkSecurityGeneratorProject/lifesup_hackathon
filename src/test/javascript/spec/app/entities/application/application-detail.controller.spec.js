'use strict';

describe('Controller Tests', function() {

    describe('Application Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockApplication, MockChallenge, MockUserList;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockApplication = jasmine.createSpy('MockApplication');
            MockChallenge = jasmine.createSpy('MockChallenge');
            MockUserList = jasmine.createSpy('MockUserList');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Application': MockApplication,
                'Challenge': MockChallenge,
                'UserList': MockUserList
            };
            createController = function() {
                $injector.get('$controller')("ApplicationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hackathonApp:applicationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
