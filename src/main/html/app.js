'use strict';

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
var PREFIX_RQ = "";
//var PREFIX_RQ = "http://purple:9998";
var userID = params["email"];

var searchItemIntoArrayWithAttribute = function(array, attr, value) {
    for (var i = 0; i < array.length; i++) {
        //console.log("check value "+ array[i][attr] + " and "+value);
        if (array[i][attr] == value) {
            return i;
        }
    }
    return null;
}

// Declare app level module which depends on views, and components
angular.module('myApp', [
    'ngRoute',
    'angularFileUpload',
    'ui.bootstrap',
    'myApp.index',
    'myApp.home',
    'myApp.myvideos',
    'myApp.myprofile',
    'myApp.friends',
    'myApp.newvideo'
]).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.otherwise({redirectTo: '/home'});
    }]);
