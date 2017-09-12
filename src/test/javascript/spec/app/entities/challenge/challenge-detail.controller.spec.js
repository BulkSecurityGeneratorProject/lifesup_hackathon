'use strict';

describe('Controller Tests', function() {

    describe('Challenge Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChallenge, MockChallengeInfo, MockApplication, MockCompany;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChallenge = jasmine.createSpy('MockChallenge');
            MockChallengeInfo = jasmine.createSpy('MockChallengeInfo');
            MockApplication = jasmine.createSpy('MockApplication');
            MockCompany = jasmine.createSpy('MockCompany');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Challenge': MockChallenge,
                'ChallengeInfo': MockChallengeInfo,
                'Application': MockApplication,
                'Company': MockCompany
            };
            createController = function() {
                $injector.get('$controller')("ChallengeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hackathonApp:challengeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
