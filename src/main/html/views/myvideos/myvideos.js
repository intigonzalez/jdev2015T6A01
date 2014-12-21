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
            videos.suffix = 'dash/low_dash.mpd';
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
        /*this.videoInProgress = function(status) {
         if (status.equals("success")) {
         return "";
         }
         else {
         return disabled;
         }
         };*/
        this.getVideos();
        var toto = {"listContent": {"content": [
            {
                "contentsID": "66a5550230cf4f4796c596fcd3fc86b3",
                "name": "vince@onehear.nl8471597667882151009.mp4",
                "login": "vince@onehear.nl",
                "unix_time": 1419016140,
                "link": "/videos/vince@onehear.nl/66a55502-30cf-4f47-96c5-96fcd3fc86b3",
                "status": "success"
            },
            {
                "contentsID": "66a5550230cf4f4796c596fcd3fc86b4",
                "name": "vince@onehear.nl8471597667882151009.mp4",
                "login": "vince@onehear.nl",
                "unix_time": 1419016140,
                "link": "/videos/vince@onehear.nl/66a55502-30cf-4f47-96c5-96fcd3fc86b3",
                "status": "success"
            }
        ]}}

    }]);