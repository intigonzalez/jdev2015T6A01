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

//Controllers
app.controller('snapmailCtrl', ['$scope', '$timeout', '$window', '$http', '$routeParams', '$location', function ($scope, $timeout, $window, $http, $routeParams, $location) {
	$http.get(PREFIX_RQ + "/api/app/" + $routeParams.sender + "/content/" + $routeParams.id)
	.success(function (data, status, headers, config) {
		$scope.content = data.content;
		$scope.contentID = data.content.contentsID;
		$scope.sender = data.content.actorID;
		$scope.type = data.content.type;
	})
	.error(function (data, status, headers, config) {
		$scope.type = null;
	});

	$scope.getType = function () {
		if ($scope.type == 'video')
		{
			var player = 'raw';
			var prefix;
			var suffix
			var userAgent = $window.navigator.userAgent;
			
			if ($scope.content.status == "success")
			{
				if (userAgent.indexOf("Chrome") >= 0 || userAgent.indexOf("Windows") >=0 || userAgent.indexOf("Chromium") >=0)
				{
					prefix = 'dash';
					suffix = 'dash/playlist.mpd';
	
					$scope.startVideo = function() {
						var video, context, player;
						var url = $scope.content.link + '/' + suffix;
	
						video = document.querySelector(".dash-video-player video");
						context = new Dash.di.DashContext();
						player = new MediaPlayer(context);
	
						player.startup();
	
						player.attachView(video);
						player.setAutoPlay(false);
	
						player.attachSource(url);
					}
				}
				else
				{
					prefix = 'hls';
					suffix = 'hls/playlist.m3u8';
	
					$scope.startVideo = function() {
						var url = $scope.content.link + '/' + suffix;
						var video;
	
						video = document.querySelector(".hls-video-player video");
						video.src = url;
						
						console.log($scope.content.link);
						video.load();
						video.play();
					}
				}

				player = prefix;
			}
			
			$scope.getPlayer = function()
			{
				return 'views/snapmail/video/' + player + '.html';
			}

			return 'views/snapmail/video.html';
		}
		else if ($scope.type == 'image')
			return 'views/snapmail/image.html';
		else if ($scope.type == null)
			return 'views/snapmail/404.html';
		else
			return 'views/snapmail/generic.html';
	}
}]);
