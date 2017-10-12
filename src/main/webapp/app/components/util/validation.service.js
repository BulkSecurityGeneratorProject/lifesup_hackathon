(function() {
    'use strict';

    angular
        .module('hackathonApp')
        .factory('validation', validation);

    function validation() {
        var validate = {
      			isMinMaxValid: isMinMaxValid,
      			isLowerThan100: isLowerThan100,
      			isDateRangeValid: isDateRangeValid,
      			isDiscountValid: isDiscountValid
    		};

		function isMinMaxValid(min, max) {
        if (min && max) {
          if (min >= max) {
      			   return 'min cannot be greater than or equal to max';
      		} else {
      			   return;
      		}
        } else return false;
		};

		function isLowerThan100(value) {
  			if (value > 100) {
  		      return 'total member cannot be over 100';
  			} else return;
		}

		function isDateRangeValid(startDate, endDate) {
        if (startDate && endDate) {
            var startDateMilliseconds = startDate.getTime();
            var endDateMilliseconds = endDate.getTime();
            if (startDateMilliseconds > endDateMilliseconds) {
                return 'date range is invalid, end date must be after start date';
            } else {
                return;
            }
        } else return false;

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
