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
        search.friends = [];
        this.getFriendList = function() {
            $http.get(PREFIX_RQ+"/api/app/"+userID+"/relation")
                .success(function(data, status, headers, config) {
                    if ( data.relations !== "" ) {
                        if (angular.isArray(data.relations.relation) == false) {
                            search.friends.push(data.relations.relation);
                        }
                        else {
                            search.friends = data.relations.relation;
                        }
                    }
                    //console.log(friends.list);
                })
                .error(function (data, status, headers, config){
                    console.log("Failed getting Friend list");
                })
        };
        this.getFriendList();

        search.list = [];
        this.searchRelation = function(name) {
            if (name == undefined) { //Disabled if blank !
                return;
            }
            //console.log("name to search : "+name);
            search.list = [];
            //$http.get("http://localhost:9999" + "/api/app/account/firstname/"+name)
            $http.get(PREFIX_RQ + "/api/app/account/firstname/"+name)
                .success(function (data, status, headers, config) {
                    if (data.users !== "") {
                        if (angular.isArray(data.users.user)) {
                            search.list = data.users.user;
                        }
                        else {
                            search.list.push(data.users.user);
                        }
                        //console.log(search.list);
                        angular.forEach(search.friends, function(relation) {
                            var index = search.list.indexOf(relation);
                            if ( index >= 0) {
                                search.list[index].asked = true;
                            }
                        })
                    }
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
