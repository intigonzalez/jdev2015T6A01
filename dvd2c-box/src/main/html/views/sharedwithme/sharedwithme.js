'use strict';

angular.module('myApp.sharedwithme', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/sharedwithme', {
            templateUrl: 'views/sharedwithme/sharedwithme.html',
            controller: 'SharedwithmeCtrl'
        });
    }])

    .controller('SharedwithmeCtrl', [function ($scope, $http) {
    	

    }]);
