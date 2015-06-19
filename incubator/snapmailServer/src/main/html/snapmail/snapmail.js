'use strict';

var PREFIX_RQ = "";

var app = angular.module('snapmailApp', ['ngRoute']).config(function($sceDelegateProvider) {
	  $sceDelegateProvider.resourceUrlWhitelist([
	                                     	    // Allow same origin resource loads.
	                                     	    'self',
	                                     	    // Allow loading from our assets domain.  Notice the difference between * and **.
	                                     	    'http://**'
	                                     	  ]);
//	  // The blacklist overrides the whitelist so the open redirect here is blocked.
//	  $sceDelegateProvider.resourceUrlBlacklist([
//	    'http://myapp.example.com/clickThru**'
//	  ]);
});
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
		if (headers('Content-Type').indexOf("text/html")==0) {
			window.location.replace("/");
		} 
		console.log(data);
		$scope.content = data.content;
		$scope.contentID = data.content.contentsID;
		$scope.sender = data.content.actorID;
		$scope.type = data.content.type;

//var json = JSON.parse(angular.toJson(data))
		//$scope.content = json.content;
		//$scope.contentID = json.content.contentsID;
		//$scope.sender = json.content.actorID;
		//$scope.type = json.content.type;
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
			
			if ($scope.content.status == 1)
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
			if ($scope.content.status == 1)
			{
			return 'views/content/image.html';
			}
			else {
				return 'views/content/imageorigin.html';
			}
		else if ($scope.type == null)
			return 'views/content/404.html';
		else
			return 'views/content/generic.html';
	}
	
}]);

//app.controller("registerController", function ($scope, $http) {
//	console.log("Hello");
//    $scope.submitData = function (person) {
//        var data = {};
//        data.user = person;
//        console.log(data);
//        $http.post(PREFIX_RQ + "/api/app/account",data )
//            .success(function (data, status, headers, config)
//            {
//                console.log("Succeed");
//                window.location.replace("/home.html");
//            })
//            .error(function (data, status, headers, config)
//            {
//                console.log("Failed");
//            });
//    };
//   // $(document) Jquery for validating the form -->
//    angular.element(document).ready(function(){
//        $("form").validate({
//            rules: {
//                name:{
//                    minlength: 3,
//                    maxlength: 20,
//                    required: true
//                },
//                email:{
//                    minlength: 3,
//                    maxlength: 20,
//                    required: true
//                }
//            },
//            highlight: function (element) {
//                $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
//            },
//            unhighlight: function (element) {
//                $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
//            }
//        });
//    });
//
//    // End of js part for validating the form inputs
//});

var popup = (function() 
		{
		 
		    function init() {
		 
		        var overlay = $('.overlay');
		 
		        $('.popup-button').each(function(i, el)
		        {
		            var modal = $('#' + $(el).attr('data-modal'));
		            var close = $('.close');
		 
		            // fonction qui enleve la class .show de la popup et la fait disparaitre
		            function removeModal() {
		                modal.removeClass('show');
		            }
		 
		            // evenement qui appelle la fonction removeModal()
		            function removeModalHandler() {
		                removeModal(); 
		            }
		 
		            // au clic sur le bouton on ajoute la class .show a la div de la popup qui permet au CSS3 de prendre le relai
		            $(el).click(function()
		            {   
		                modal.addClass('show');
		                overlay.unbind("click");
		                // on ajoute sur l'overlay la fonction qui permet de fermer la popup
		                overlay.bind("click", removeModalHandler);
		            });
		 
		            // en cliquant sur le bouton close on ferme tout et on arrête les fonctions
		            close.click(function(event)
		            {
		                event.stopPropagation();
		                removeModalHandler();
		            });
		 
		        });
		    }
		 
		    init();
		 
		})();
