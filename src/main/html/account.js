/**
 * Created by Anas on 27/11/2014.
 */
/**
 * Created by Charles-Damien on 27/11/14.
 */
var userID = "vince@onehear.nl";
var app = angular.module('Account', []);

app.controller('UserController', function($http) {
    var user = this;
    user.person = {};
    this.tab = 1;
    this.setTab = function() {
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
        $http.get("/api/app/account/"+userID)
            .success(function( data, status, headers, config) {
                user.person = data.user;
            })
            .error(function( data, status, headers, config) {
                console.log("Failed");
            })
    };
    this.putUser = function (person) {
        var data = {};
        data.user = person;
        $http.put("/api/app/account/"+this.person.userID, data )
            .success(function (data, status, headers, config)
            {
                console.log("Succeed");
            })
            .error(function (data, status, headers, config)
            {
                console.log("Failed");
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

app.controller('listFriendCtrl', ['$scope', function ($scope) {

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

}]);


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
