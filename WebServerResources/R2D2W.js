R2D2W = function() {
	return {
		object: function(o) {
			function F() {};
			F.prototype = o;
			return new F();
		}
	};	
}();