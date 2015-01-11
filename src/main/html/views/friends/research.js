'use strict';

angular.module('myApp.friendSearch', ['ngRoute', 'ui.bootstrap'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/searchfriends', {
            templateUrl: 'views/friends/research.html',
            controller: 'FriendsCtrl'
        });
    }])
    .controller('friendSearchCtrl', ['$scope', '$http', '$window', function ($scope, $http, $window) {    }])

    .controller('friendSearchController', ['$scope', '$http', function ($scope, $http) {
        var search = this;
        search.list = [];
        this.searchRelation = function(name) {
            if (name == undefined) { //Disabled if blank !
                return;
            }
            //console.log("name to search : "+name);
            search.list = [];
            $http.get("http://localhost:9999" + "/api/app/account/name/"+name)
                .success(function (data, status, headers, config) {
                        if (angular.isArray(data.listUser.user)) {
                            search.list = data.listUser.user;
                        }
                        else {
                            search.list.push(data.listUser.user);
                        }
                        //console.log(search.list);
                    })
                .error(function (data, status, headers, config) {
                    console.log("Failed while searching for" +name+ " !! ");
                    })
        }

        this.AskForFriend = function(relation) {
            relation.asked = true;
            $http.post(PREFIX_RQ + "/api/app/" + userID + "/relation/"+relation.userID)
                .success(function (data, status, headers, config) {
                    console.log("Succeed");
                    // Friend Added successfully
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed");
                })
        }
        this.isAsked = function(relation) {
            if (relation.asked == true ) {
                return true;
            }
            else {
                return false;
            }
        }

    }]);
