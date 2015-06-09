'use strict';

var PREFIX_RQ = "";

var app = angular.module('snapmailApp', ['ngRoute']);

app.config(function($routeProvider) {
	$routeProvider
	.when('/', {
		templateUrl: 'views/content/home.html'
	})
	.when('/:sender/:id', {
		templateUrl: 'views/content/file.html',
		controller: 'snapmailCtrl'
	})
	.otherwise({redirectTo: '/'});
});

//Controllers
app.controller('snapmailCtrl', ['$scope', '$timeout', '$window', '$http', '$routeParams', '$location', function ($scope, $timeout, $window, $http, $routeParams, $location) {
	$http.get(PREFIX_RQ + "/api/app/" + $routeParams.sender + "/content/" + $routeParams.id)
	.success(function (data, status, headers, config) {
		var json = JSON.parse(angular.toJson(data));
		$scope.content = json.content;
		$scope.contentID = json.content.contentsID;
		$scope.sender = json.content.actorID;
		$scope.type = json.content.type;
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
				return 'views/content/video/' + player + '.html';
			}

			return 'views/content/video.html';
		}
		else if ($scope.type == 'image')
			return 'views/content/image.html';
		else if ($scope.type == null)
			return 'views/content/404.html';
		else
			return 'views/content/generic.html';
	}
}]);
