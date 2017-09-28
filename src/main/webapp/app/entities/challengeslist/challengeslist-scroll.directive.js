(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .directive('scroll', scroll);

    scroll.$inject = ['$window'];

    function scroll($window) {
        return function(scope, element, attrs) {
        
        angular.element($window).bind("scroll", function() {
            var clientWidth = document.documentElement.clientWidth;
            //add class when page offset is greater than 600 and screen size must be greater than 1024
            if (this.pageYOffset >= 600 && clientWidth > 1024) {                
                scope.boolChangeClass = true;
            }
            else { //otherwise remove that class
                scope.boolChangeClass = false;
            }
             scope.$apply();
        });
    };
    }

})();
