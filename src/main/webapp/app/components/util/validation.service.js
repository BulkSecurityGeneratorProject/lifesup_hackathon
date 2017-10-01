(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .factory('validation', validation);

    validation.$inject = ['$scope'];

    function validation ($scope) {
        var validate = {
			isMinMaxValid: isMinMaxValid,
			isLowerThan100: isLowerThan100,
			isDateRangeValid: isDateRangeValid,
			isDiscountValid: isDiscountValid
		};
		
		function isMinMaxValid(min, max) {
			if (min >= max) {
				return 'min should not be greater than max';
			} else {
				return 'min max value is valid';
			}
		};
		
		function isLowerThan100(value) {
			if (value > 100) {
				return 'total member should not be over than 100';
			} else {
				return 'member value is valid';
			}
		}
		
		function isDateRangeValid(startDate, endDate) {
			var startDateMilliseconds = startDate.getTime();
			var endDateMilliseconds = endDate.getTime();
			if (startDateMilliseconds > endDateMilliseconds) {
				return 'date range is invalid, end date must be after start date';
			} else {
				return 'date range is valid';
			}
		}
		
		function isDiscountValid(discountPercentage) {
			if (discountPercentage > 100 || discountPercentage <= 0) {
				return "discount percentage is invalid";
			} else {
				return "discount percentage is valid";
			}
		}
		
        return validate;
    }
})();
