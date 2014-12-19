'use strict';

angular.module('myApp.friends', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/friends', {
            templateUrl: 'views/friends/friends.html',
            controller: 'FriendsCtrl'
        });
    }])

    .controller('FriendsCtrl', ['$scope', '$http', function ($scope, $http) {


        // *****************  Get FriendList ****************
        var friendList = this;
        this.getFriendList = function() {
            $http.get(PREFIX_RQ+"/api/app/"+userID+"/relation")
                .success(function(data, status, headers, config) {
                    friendList.friend = data.listRelation;
                    $scope.friendList=friendList;
                    // ==> What to Do If success !!
                })
                .error(function (data, status, headers, config){
                    console.log("Failed getting Friend list");
                })
        };
        friendList.friend = this.getFriendList();


        // ********  Add a Friend to the friendList **********
        $scope.addFriend = function(friend_userID) {

            $http.post(PREFIX_RQ + "/api/app/" + userID + "/relation/"+friend_userID)
                .success(function (data, status, headers, config) {
                    console.log("Succeed");
                    // Friend Added successfully
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed");
                })
        }
        //  this.addFriend();



        /*
         $scope.friendList = {};

         $scope.friendList.friend = [{
         "name": "Steve Jobs",
         "surname": "The Boss",
         "email": "i-mNotHere@paradise.com"
         },{
         "name": "Ellie Goulding",
         "surname": "Ms Beauty",
         "email": "heyu@dreams.zz"
         },{
         "name": "Michael Stipe",
         "surname": "Everybody",
         "email": "lol@LAD.bib"
         },{
         "name": "Jeremy Clarkson",
         "surname": "Mr car",
         "email": "iduA@6.mir"
         }];
         */


    }]);