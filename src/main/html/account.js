/**
 * Created by Anas on 27/11/2014.
 */


var parseQueryString = function() {

    var str = window.location.search;
    var objURL = {};

    str.replace(
        new RegExp( "([^?=&]+)(=([^&]*))?", "g" ),
        function( $0, $1, $2, $3 ){
            objURL[ $1 ] = $3;
        }
    );
    return objURL;
};
var params = parseQueryString();

var userID = params["email"];
var app = angular.module('MediaHome', []);
PREFIX_RQ = "";
//PREFIX_RQ = "http://localhost:9998";

app.controller('UserController', function($http) {
    var user = this;
    user.person = {};
    this.tab = 1;
    this.setTab = function() {
        user.class = ""; // Reset the buttons.
        if (this.tab==1) {
            this.tab = 2;
        } else {
            this.tab = 1;
        }
    };
    this.isSetTab = function(value) {
        return this.tab === value;
    };
    this.getUser = function() {
        $http.get(PREFIX_RQ+"/api/app/account/"+userID)
            .success(function( data, status, headers, config) {
                user.person = data.user;
            })
            .error(function( data, status, headers, config) {
                console.log("Failed while getting User Informations");
            })
    };
    this.putUser = function (person) {
        var data = {};
        data.user = person;
        $http.put(PREFIX_RQ+"/api/app/account/"+this.person.userID, data )
            .success(function (data, status, headers, config)
            {
                console.log("Succeed");
                user.class = "btn-success";
            })
            .error(function (data, status, headers, config)
            {
                console.log("Failed while editing User Informations");
                user.class = "btn-danger";
            });
    };
    this.getUser();


});

app.controller('DisplayController', function() {
    this.tab = 1;
    this.setTab = function(value) {
        this.tab=value;
    };
    this.isSetTab = function(value) {
        return this.tab === value;
    };
});

app.controller('listFriendCtrl' , function ($http,$scope) {

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

});


app.controller('listVideoCtrl', ['$scope', function ($scope) {

    $scope.videoList = {};

    $scope.videoList.video = [{
        "owner": "Steve Jobs",
        "name": "Ipad presentation",
        "email": "i-mNotHere@paradise.com"
    },{
        "owner": "Ellie Goulding",
        "name": "Birthday party",
        "email": "heyu@dreams.zz"
    },{
        "owner": "B4Ever",
        "name": "Festival",
        "email": "heyu@dreams.zz"
    }];

}]);
