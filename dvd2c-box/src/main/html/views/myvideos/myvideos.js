'use strict';
var mod = angular.module('myApp.myvideos', ['ngRoute', 'ui.bootstrap','ngMockE2E'])
mod.run(function($httpBackend) {

	// returns the current list of phones
	
	
	$httpBackend
			.when('GET', /^\/api\/app\/[^//]+\/content$/)
			.respond(
					function(method, url, data, headers) {
						var data = '{"contents":{"content":[{"contentsID":"1d3742b64cb04324b5ba68f9ce5ee48f","name":"holiday.mp4","actorID":"test@test.fr","metadata":[2,3],"unix_time":1430376221,"link":"\/dev\/videos","status":"success","type":"video"},{"contentsID":"120d1872788b4f908666bf3020e72119","name":"week.mp4","actorID":"test@test.fr","unix_time":1430377750,"link":"\/dev\/videos","status":"success","type":"video"},{"contentsID":"7ad7ac91918246b29262dcdcca317eb5","name":"doc.pdf","actorID":"test@test.fr","unix_time":1430378104,"link":"\/dev\/document","status":"success","type":"application"},{"contentsID":"ff0a9ae6265e41e1a7dae732d2630134","name":"info.jpg","actorID":"test@test.fr","unix_time":1430378104,"link":"\/dev\/picture","status":"success","type":"image"},{"contentsID":"f8224d615764402f88c0686653e99a65","name":"parameter.txt","actorID":"test@test.fr","unix_time":1430378145,"link":"\/dev\/document","status":"success","type":"application"},{"contentsID":"f97103baf72344309fc1c04c0b87aea5","name":"me.jpg","actorID":"test@test.fr","unix_time":1430378145,"link":"\/dev\/picture","status":"success","type":"image","metadata":[0,1]}]}}'
						var header = {};
						return [ 200, data , {} ];

					});
				
	$httpBackend.when('DELETE', /api\/app\/[^//]+\/content\/[^//]+$/)
	.respond(
			function(method, url, data, headers) {				
					return [ 200, {} , {} ];			
			});

	$httpBackend.when('PUT', /api\/app\/[^//]+\/content\/[^//]+$/)
	.respond(
			function(method, url, data, headers) {				
					return [ 200, {} , {} ];			
			});
	
	
	
	$httpBackend.whenGET(/views/).passThrough();
	
	
});
    mod.config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/myvideos', {
            templateUrl: 'views/myvideos/myvideos.html',
            controller: 'MyVideosCtrl'
        });
    }])

    .controller('MyVideosCtrl', [function ($scope, $http) {


    }])

    .controller('MyVideosController', ['$scope', '$http', '$window', '$modal', '$log', function($scope, $http, $window, $modal, $log) {

        var videos = this;

        //Determine the right steraming protocol.
        var userAgent = $window.navigator.userAgent;
        console.log(userAgent);
        if ( userAgent.indexOf("Chrome") >= 0 || userAgent.indexOf("Windows") >=0 || userAgent.indexOf("Chromium") >=0 ) {
            videos.prefix = 'dash';
            videos.suffix = 'dash/playlist.mpd';
        }
        else {
            videos.prefix = 'hls';
            videos.suffix = 'hls/playlist.m3u8';
        }


        videos.list = [];
        videos.roles = [
            {"roleID":"0" , "roleName":"public", "info":"Seen by all your relations"},
            {"roleID":"1" , "roleName":"Family", "info":"Seen by all your family only"},
            {"roleID":"2" , "roleName":"Friends", "info":"Seen by all your friends only"},
            {"roleID":"3" , "roleName":"Pro", "info":"Seen by all your professional contacts"},
        ];  //List of role
        this.getVideos = function() {
            $http.get(PREFIX_RQ + "/api/app/" + userID + "/content")
                .success(function (data, status, headers, config) {
                    if ( data.contents !== "" ) {
                        if (angular.isArray(data.contents.content) == false) {
                        	if(data.contents.content.type === "video")
                        		videos.list.push(data.contents.content);
                        }
                        else {
                            videos.list = $.grep(data.contents.content, function(o){return o.type === "video"});
                        }
                    }

                })
                .error(function (data, status, headers, config) {
                    console.log("Failed while getting Videos Informations");
                })
        };
        this.generateLink = function(content) {
             if (content.status == "success") {
                return videos.prefix + ".html?url=" + content.link + "/" + videos.suffix;
            }
            else {
                return "";
            }
        };
        this.videoInProgress = function(content) {
             if (content.status == "success") {
                 return "";
             }
             else {
                return "disabled";
             }
         };
        this.getIndex = function(content) {
            return videos.list.indexOf(content);
        }


        // **** Function to update a content
        this.updateContent = function(content) {
            var data = {"content" : content};
            $http.put(PREFIX_RQ+"/api/app/"+userID+"/content/"+content.contentsID,  data)
                .success(function() {
                    console.log("success");
                })
                .error(function() {
                    console.log("error");
                })
        }

        // ***** Remove a video *****
        this.removeVideo = function(content) {
            $http.delete(PREFIX_RQ + "/api/app/" + userID + "/content/"+content.contentsID)
                .success(function(data,status,headers,config) {
                    var index = videos.getIndex(content);
                    if (index > -1) {
                        videos.list.splice(index, 1);
                    }
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed");
                })
        }

        this.showDetails = function(content) {
            $scope.open(content);
        }
        this.getVideos();


        $scope.open = function (content, size) {
            var modalInstance = $modal.open({
                templateUrl: 'VideosModalContent.html',
                controller: 'VideosModalInstanceCtrl',
                size: size,
                resolve: {
                	roles: function () {
                        return videos.roles;
                    },
                    video: function () {
                        return content;
                    }
                }
            });

            modalInstance.result.then(function (video) {
                videos.updateContent(video);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        }



    }])
    .controller('VideosModalInstanceCtrl', ['$scope', '$modalInstance', 'roles', 'video', function ($scope, $modalInstance, roles, video) {

        $scope.roles = angular.copy(roles);
        if (video.metadata === undefined) {
        } else {
            if ( angular.isArray(video.metadata) ) {
                angular.forEach(video.metadata, function (id) {
                    var index = searchItemIntoArrayWithAttribute($scope.roles, "roleID", id);
                    $scope.roles[index].value = true;
                });
            }
            else {
                var index = searchItemIntoArrayWithAttribute($scope.roles, "roleID", video.metadata );
                $scope.roles[index].value=true;
            }
        }
        //console.log(roles);

        $scope.ok = function () {
            //console.log($scope.roles);
            video.metadata= [];
            angular.forEach($scope.roles, function(role) {
                if (role.value == true) {
                    video.metadata.push(role.roleID)
                }
            });

            $modalInstance.close(video);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);