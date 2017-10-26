(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .directive('hasAuthorityExcept', hasAuthorityExcept);

    hasAuthorityExcept.$inject = ['Principal'];

    function hasAuthorityExcept(Principal) {
        var directive = {
            restrict: 'A',
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs) {
            var authority = attrs.hasAuthorityExcept.replace(/\s+/g, '');

            var setVisible = function () {
                    element.removeClass('hidden');
                },
                setHidden = function () {
                    element.addClass('hidden');
                },
                defineVisibility = function (reset) {

                    if (reset) {
                        setVisible();
                    }

                    Principal.hasAuthorityExcept(authority)
                        .then(function (result) {
                            if (result) {
                                setHidden();
                            } else {
                                setVisible();
                            }
                        });
                };

            if (authority.length > 0) {
                defineVisibility(true);

                scope.$watch(function() {
                    return Principal.isAuthenticated();
                }, function() {
                    defineVisibility(true);
                });
            }
        }
    }
})();
