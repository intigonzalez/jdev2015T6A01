'use strict';

var mod = angular.module('myApp.friendSearch', [ 'ngRoute', 'ui.bootstrap',
                                                 'ngMockE2E' ]);

mod 
.run(function($httpBackend) {

	// returns the current list of phones

	if (TEST) {

		$httpBackend
		.when('GET', /^\/api\/app\/account\/firstname\/[^//]+$/)
		.respond(
				function(method, url, data, headers) {
					if (url == "/api/app/account/firstname/clement") {
						var data = '{"users":{"user":[{"userID":"cgonzalez@viotech.net","firstname":"Clement","surname":"Gonzalez","boxID":"BOX_ORANGE"},{"userID":"clement@gmail.com","firstname":"Clement","surname":"Calais","boxID":"BOX_BLUE"}]}}'
					} else if (url == "/api/app/account/firstname/david") {
						var data = '{"users":{"user":{"userID":"dbourasseau@viotech.net","firstname":"David","surname":"Bourasseau","boxID":"BOX_BLUE"}}}'
					} else {
						var data = '{"users":""}'
					}
					return [ 200, data, {} ];
				});
		$httpBackend.when('POST',
				/^\/api\/app\/[^//]+\/relation\/[^//]+$/).respond(
				               function(method, url, data, headers) {

					return [ 200, data, {} ];
				});
	} else {
		$httpBackend.whenGET(/.*/).passThrough();
		$httpBackend.whenPUT(/.*/).passThrough();
		$httpBackend.whenDELETE(/.*/).passThrough();
		$httpBackend.whenPOST(/.*/).passThrough();
	}

	$httpBackend.whenGET(/views/).passThrough();

});

mod
.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/searchfriends', {
		templateUrl : 'views/friends/research.html',
		controller : 'FriendsCtrl'
	});
} ])
.controller(
		'friendSearchCtrl',
		[ '$scope', '$http', '$window',
		  function($scope, $http, $window) {
		} ])

		.controller(
				'friendSearchController',
				[
				 '$scope',
				 '$http',
				 function($scope, $http) {
					 var search = this;
					 search.friends = [];
					 this.getFriendList = function() {
						 $http
						 .get(PREFIX_RQ + "/api/app/relation")
						 .success(
								 function(data, status, headers,
										 config) {
									 if (headers('Content-Type')
											 .indexOf(
											 "text/html") == 0) {
										 window.location
										 .replace("/");
									 }
									else if (data.contactXSDs !== "") {
										 if (angular
												 .isArray(data.contactXSDs.contactXSD) == false) {
											 search.friends
											 .push(data.contactXSDs.contactXSD);
										 } else {
											 search.friends = data.contactXSDs.contactXSD;
										 }
									 }
									 // console.log(friends.list);
								 })
								 .error(
										 function(data, status, headers,
												 config) {
											 console
											 .log("Failed getting Friend list");
										 })
					 };
					 this.getFriendList();

					 search.list = [];
					 this.searchRelation = function(name) {
						 if (name == undefined) { // Disabled if blank
							 // !
							 return;
						 }
						 // console.log("name to search : "+name);
						 search.list = [];
						 // $http.get("http://localhost:9999" +
						 // "/api/app/account/firstname/"+name)
						 $http
						 .get(
								 PREFIX_RQ
								 + "/api/app/account/firstname/"
								 + name)
								 .success(
										 function(data, status, headers,
												 config) {
											 if (headers('Content-Type')
													 .indexOf(
													 "text/html") == 0) {
												 window.location
												 .replace("/");
											 }
											 if (data.users !== "") {
												 if (angular
														 .isArray(data.users.user)) {
													 search.list = data.users.user;
												 } else {
													 search.list
													 .push(data.users.user);
												 }
												 // console.log(search.list);
												 angular
												 .forEach(
														 search.friends,
														 function(
																 relation) {
															 var index = search.list
															 .indexOf(relation);
															 if (index >= 0) {
																 search.list[index].asked = true;
															 }
														 })
											 }
										 })
										 .error(
												 function(data, status, headers,
														 config) {
													 console
													 .log("Failed while searching for"
															 + name
															 + " !! ");
												 })
					 }

					 this.AskForFriend = function(relation) {
						 relation.asked = true;
						 $http
						 .post(
								 PREFIX_RQ
								 + "/api/app/relation/"
								 + relation.uuid)
								 .success(
										 function(data, status, headers,
												 config) {
											 if (headers('Content-Type')
													 .indexOf(
													 "text/html") == 0) {
												 window.location
												 .replace("/");
											 }
											 console.log("Succeed");
											 // Friend Added successfully
										 }).error(
												 function(data, status, headers,
														 config) {
													 console.log("Failed");
												 })
					 }
					 this.isAsked = function(relation) {
						 if (relation.asked == true) {
							 return true;
						 } else {
							 return false;
						 }
					 }

				 } ]);
