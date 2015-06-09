'use strict';

angular.module('myApp.myvideos', ['ngRoute', 'ui.bootstrap'])

    .config(['$routeProvider', function ($routeProvider) {
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
            {"roleID":"Public" , "roleName":"public", "info":"Seen by all your relations"},
            {"roleID":"Family" , "roleName":"Family", "info":"Seen by all your family only"},
            {"roleID":"Friends" , "roleName":"Friends", "info":"Seen by all your friends only"},
            {"roleID":"Pro" , "roleName":"Pro", "info":"Seen by all your professional contacts"},
        ];  //List of role
        this.getVideos = function() {
            $http.get(PREFIX_RQ + "/api/app/content")
                .success(function (data, status, headers, config) {
                	if (headers('Content-Type').indexOf("text/html")==0) {
    					window.location.replace("/");
    				} 
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
            $http.put(PREFIX_RQ+"/api/app/content/"+content.contentsID,  data)
                .success(function() {
                	if (headers('Content-Type').indexOf("text/html")==0) {
    					window.location.replace("/");
    				} 
                	if (headers('Content-Type').indexOf("text/html")==0) {
    					window.location.replace("/");
    				} 
                    console.log("success");
                })
                .error(function() {
                    console.log("error");
                })
        }

        // ***** Remove a video *****
        this.removeVideo = function(content) {
            $http.delete(PREFIX_RQ + "/api/app/content/"+content.contentsID)
                .success(function(data,status,headers,config) {
                	if (headers('Content-Type').indexOf("text/html")==0) {
    					window.location.replace("/");
    				} 
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
                var index = searchItemIntoArrayWithAttribute($scope.roles, "roleID", video.metadata);
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