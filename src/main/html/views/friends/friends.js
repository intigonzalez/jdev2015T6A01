'use strict';

angular.module('myApp.friends', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/friends', {
            templateUrl: 'views/friends/friends.html',
            controller: 'FriendsCtrl'
        });
    }])
    .controller('FriendsCtrl', [ function () {
    }])
    .controller('FriendsController', ['$scope', '$http', function ($scope, $http) {

        var friends = this; // This element to get it easily
        friends.list = []; //Friend list into the controller
        friends.addFriendSuccess =null; //boolean to change the color of add Friend button

        // *****************  Get FriendList ****************
        
        this.getFriendList = function() {
            $http.get(PREFIX_RQ+"/api/app/"+userID+"/relation")
                .success(function(data, status, headers, config) {
                    if (angular.isArray(data.listRelation.relation) == false) {
                        friends.list.push(data.listRelation.relation);
                    }
                    else {
                        friends.list = data.listRelation.relation;
                    }
                    //console.log(friends.list);
                    friends.addFriendSuccess =null; //reset the value if the function is called from AddFriend Callback Success
                })
                .error(function (data, status, headers, config){
                    console.log("Failed getting Friend list");
                })
        };
        this.getFriendList();

        //Get Aprouve Status to adapt display
        this.isStatus = function(friend, str) {
            if (friend.aprouve == str) {
                return true;
            }
           else {
                return false;
           }
        }
        this.AprouveFriend = function(friend) {
            friend.aprouve = 3;
            var data = {"relation" : friend};
            $http.put(PREFIX_RQ+"/api/app/"+userID+"/relation/"+friend.email, data)
                .success(function() {
                    console.log("success");
                })
                .error(function() {
                    console.log("error");
                })
        }
        //function to change the color of add Friend button
        this.isFriendAddingSuccess = function() {
            if (friends.addFriendSuccess == null) {
                return "";

            }
            else if ( friends.addFriendSuccess == 'success') {
                return "btn-success";
            }
            else {
                return "btn-danger";
            }
        }

        // ********  Add a Friend to the friendList **********
        $scope.addFriend = function(friend_userID) {

            $http.post(PREFIX_RQ + "/api/app/" + userID + "/relation/"+friend_userID)
                .success(function (data, status, headers, config) {
                    console.log("Succeed");
                    // Friend Added successfully
                    friends.addFriendSuccess = 'success';
                    friends.getFriendList();
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed");
                    friends.addFriendSuccess = 'failed';
                })
        }

    }]);