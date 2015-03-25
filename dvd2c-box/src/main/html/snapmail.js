'use strict';

//TO DO
var PREFIX_RQ = "";

var app = angular.module('snapmailApp', ['ngRoute']);

app.config(function($routeProvider) {
	$routeProvider
	.when('/', {
		templateUrl: 'views/snapmail/home.html'
	})
	.when('/:sender/:id', {
		templateUrl: 'views/snapmail/file.html',
		controller: 'snapmailCtrl'
	})
	.otherwise({redirectTo: '/'});
});

//Controller
app.controller('snapmailCtrl', ['$scope', '$http', '$routeParams', '$location', function ($scope, $http, $routeParams, $location) {
	$http.get(PREFIX_RQ + "/api/app/" + $routeParams.sender + "/content/" + $routeParams.id)
	.success(function (data, status, headers, config) {
		$scope.contentID = data.content.contentsID;
		$scope.sender = data.content.actorID;
		$scope.type = 'videos';
	})
	.error(function (data, status, headers, config) {
		$scope.type = null;
	});
	
	$scope.getType = function () {
		if ($scope.type == 'video')
			return 'views/snapmail/video.html';
		else if ($scope.type == 'image')
			return 'views/snapmail/image.html';
		else if ($scope.type == null)
			return 'views/snapmail/404.html';
		else
			return 'views/snapmail/generic.html';
	}
}]);