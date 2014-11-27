/**
 * Created by Anas on 27/11/2014.
 */
/**
 * Created by Charles-Damien on 27/11/14.
 */
var userID = "vince@oneear.nl";
var app = angular.module('Account', []);

app.controller('UserController', function($http) {
    var user = this;
    user.person = {};

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

app.controller('listCtrl', ['$scope', function ($scope) {

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
