(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .directive('scroll', scroll);

    function scroll($window) {
 return function(scope, element, attrs) {
      
        angular.element($window).bind("scroll", function() {
            if (this.pageYOffset >= 100) {
                 scope.boolChangeClass = true;
                 console.log('Scrolled below header.');
             } else {
                 scope.boolChangeClass = false;
                 console.log('Header is in view.');
             }
            scope.$apply();
        });
    };
    }

})();
