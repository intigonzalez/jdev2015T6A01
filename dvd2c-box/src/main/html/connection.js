PREFIX_RQ = "";

var ConnectionForm = angular.module('ConnectionForm', [ 'ngMockE2E',
		'ngCookies' ]);

ConnectionForm.run(function($httpBackend, $cookieStore) {
	if (TEST) {
		// returns the current list of phones
		$httpBackend.when('POST', 'api/authentication').respond(
				function(method, url, data, headers) {
					data = data.slice(11,23);
					 if (data == "test@test.fr") {
						$cookieStore.put("JSESSIONID", '7443678124586818821');
						return [ 200, {}, {} ];
					}
					data = data.slice(0,8);
					 if (data == "testtest") {
						var user = '7443678124586818821';
						console.log(user);
						$cookieStore.put('JSESSIONID', user);

						return [ 200, {}, {} ];
					} else {
						return [ 403, {}, {} ];
					}
				});
	} else {
		$httpBackend.whenGET(/.*/).passThrough();
		$httpBackend.whenPUT(/.*/).passThrough();
		$httpBackend.whenDELETE(/.*/).passThrough();
		$httpBackend.whenPOST(/.*/).passThrough();
	}
});

ConnectionForm.controller("mainController", function($scope, $http) {
	var errorConnection = false;
	$scope.CredentialsCheck = function() {
		if (errorConnection == true) {
			return "btn-theme04";
		} else {
			return "btn-theme";
		}
	};
	$scope.submitData = function(person) {

		var data = 'j_username=' + encodeURIComponent(person.userID)
				+ '&j_password=' + encodeURIComponent(person.password)
				+ '&remember-me=' + person.rememberMe + '&submit=Login';
		return $http.post('api/authentication', data, {
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		}).success(function(response) {

			window.location.replace("/home.html");
			return response;
		}).error(function(response) {
			console.log("Failed");
			errorConnection = false;
		});
	}

});