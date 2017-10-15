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
      			isDiscountValid: isDiscountValid,
            isAllDateValid: isAllDateValid
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

    function isAllDateValid(arr) {
        if (arr.every(function(num, i) {
            return i === arr.length - 1 || num < arr[i + 1];
          })) {
            return "date range is valid, please continue to the next section";
          } else return "date range is invalid, please refer to this order: Application Close, " +
                        "Inform Date, Kick Off Webinar, Event Start, Event End, Submission Close, " +
                        "Pilot Phase Start, Pilot Phase End";
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
