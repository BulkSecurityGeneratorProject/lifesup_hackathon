(function () {
    'use strict';

    angular
        .module('hackathonApp')
        .directive('timeServer', timeServer);

    timeServer.$inject = ['$interval', 'dateFilter', 'TimeServer'];

    function timeServer($interval, dateFilter, TimeServer) {
        return function (scope, element, attrs) {
            var format;
            var start = new Date();
            TimeServer.get(function (result) {
                var end = new Date();
                var delay = end - start; // Time for response
                var now = new Date(result.timeServer);
                var temp = now.getTime() + delay;
                
                function updateTime() {
                    now.setTime(temp);
                    element.text(dateFilter(new Date(now), format));
                    temp += 1000;
                }

                scope.$watch(attrs.timeServer, function (value) {
                    format = value;
                    updateTime();
                });
                $interval(updateTime, 1000);
            });
        }
    }
})();
