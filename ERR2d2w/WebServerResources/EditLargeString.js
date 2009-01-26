R2D2W.EditLargeString = function() {
	return {
		textCounter: function (field, countfield, maxlimit) {
			if (field.value.length > maxlimit) {
				field.value = field.value.substring(0, maxlimit);
			} else { 
				countfield.value = maxlimit - field.value.length;
			}
		}
	};
}();