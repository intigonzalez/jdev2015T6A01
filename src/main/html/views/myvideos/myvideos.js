'use strict';

angular.module('myApp.myvideos', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/myvideos', {
            templateUrl: 'views/myvideos/myvideos.html',
            controller: 'MyVideosCtrl'
        });
    }])

    .controller('MyVideosCtrl', [function ($scope, $http) {


    }])

    .controller('MyVideosController', ['$scope', '$http', '$window', function($scope, $http, $window) {

        var videos = this;

        //Determine the right protocol.
        var userAgent = $window.navigator.userAgent;
        console.log(userAgent);
        if ( userAgent.indexOf("Chrome") >= 0 || userAgent.indexOf("Windows") >=0 ) {
            videos.prefix = 'dash';
            videos.suffix = 'dash/playlist.mpd';
        }
        else {
            videos.prefix = 'hls';
            videos.suffix = 'hls/playlist.m3u8';
        }


        videos.list = [];
        this.getVideos = function() {
            $http.get(PREFIX_RQ + "/api/app/" + userID + "/content")
                .success(function (data, status, headers, config) {
                    if (angular.isArray(data.listContent.content) == false) {
                        videos.list.push(data.listContent.content);
                    }
                    else {
                        videos.list = data.listContent.content;
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

        this.getVideos();

    }]);