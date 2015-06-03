/**
 * Created by Charles-Damien on 10/01/15.
 */
'use strict';

function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ')
			c = c.substring(1);
		if (c.indexOf(name) == 0)
			return c.substring(name.length, c.length);
	}
	return "";
}

function userAuth() {
	console.log("Cookie : "+getCookie("JSESSIONID"));
	 if (getCookie("JSESSIONID") == "") {
		 console.log("Cookie : "+getCookie("JSESSIONID"));
//	 window.location.replace("/");
	 }

}

angular.module('myApp.index', [ 'ngRoute' ]) .controller('IndexController', [ '$http', function($http, localStorageService) {
	this.cookie = getCookie("authentication");
	// $scope.Cookie = getCookie("authentication");
	this.logout = function() {

		// logout from the server
		$http.post('api/logout')

		.success(function(response) {
			document.cookie = "JSESSIONID=; expires=-1";
			// to get a new csrf token call the api
			$http.get('api/app/account')
			.success(function(data, status, headers, config) {
				if (headers('Content-Type').indexOf("text/html")==0) {
					window.location.replace("/");
				} 
			});
			
			// window.location.replace("/");
			return response;
		});
	}
} ]);

//myApp.factory('redirectInterceptor', ['$location', '$q', function($location, $q) {
//    return function(promise) {
//        promise.then(
//            function(response) {
//                if (typeof response.data === 'string') {
//                    if (response.data.indexOf instanceof Function &&
//                        response.data.indexOf('<html id="ng-app" ng-app="loginApp">') != -1) {
//                        $location.path("/logout");
//                        window.location = url + "logout"; // just in case
//                    }
//                }
//                return response;
//            },
//            function(response) {
//                return $q.reject(response);
//            }
//        );
//        return promise;
//    };
//}]);