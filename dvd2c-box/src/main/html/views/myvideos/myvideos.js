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
        videos.listGroups = [
            {"groupID":"0" , "groupName":"public", "info":"Seen by all your relations"},
            {"groupID":"1" , "groupName":"Family", "info":"Seen by all your family only"},
            {"groupID":"2" , "groupName":"Friends", "info":"Seen by all your friends only"},
            {"groupID":"3" , "groupName":"Pro", "info":"Seen by all your professional contacts"},
        ];  //List of groups
        this.getVideos = function() {
            $http.get(PREFIX_RQ + "/api/app/" + userID + "/content")
                .success(function (data, status, headers, config) {
                    if ( data.listContent !== "" ) {
                        if (angular.isArray(data.listContent.content) == false) {
                            videos.list.push(data.listContent.content);
                        }
                        else {
                            videos.list = data.listContent.content;
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
                    listGroups: function () {
                        return videos.listGroups;
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
    .controller('VideosModalInstanceCtrl', ['$scope', '$modalInstance', 'listGroups', 'video', function ($scope, $modalInstance, listGroups, video) {

        $scope.listGroups = angular.copy(listGroups);
        if (video.authorization === undefined) {
        } else {
            if ( angular.isArray(video.authorization) ) {
                angular.forEach(video.authorization, function (item) {
                    var index = searchItemIntoArrayWithAttribute($scope.listGroups, "groupID", item.groupID);
                    $scope.listGroups[index].value = true;
                });
            }
            else {
                var index = searchItemIntoArrayWithAttribute($scope.listGroups, "groupID", video.authorization.groupID);
                $scope.listGroups[index].value=true;
            }
        }
        //console.log(listGroups);

        $scope.ok = function () {
            //console.log($scope.listGroups);
            video.authorization= [];
            angular.forEach($scope.listGroups, function(group) {
                if (group.value == true) {
                    var newAuthorization = {"groupID": group.groupID, "action": "action"};
                    video.authorization.push(newAuthorization)
                }
            });

            $modalInstance.close(video);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);