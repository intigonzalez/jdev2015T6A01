/**
 * Created by Charles-Damien on 27/11/14.
 */
var userID = "vince@onehear.nl";
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