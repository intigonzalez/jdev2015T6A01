'use strict';

angular.module('myApp.myprofile', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/myprofile', {
            templateUrl: 'views/myprofile/myprofile.html',
            controller: 'ProfileCtrl'
        });
    }])
    .controller('ProfileCtrl',[function() {

    }])
    .controller('ProfileController', ['$http', function ($http) {

        var user = this;
        user.person = {};
        user.class= "";
        this.tab = 0;
        this.setTab = function () {
            user.class="";
            if (this.tab == 0) {
                this.tab = 1;
            } else {
                this.tab = 0;
            }
        };
        this.isSetTab = function (value) {
            return this.tab === value;
        };

        this.getUser = function () {
            $http.get(PREFIX_RQ + "/api/app/account/" + userID)
                .success(function (data, status, headers, config) {
                    user.person = data.user;
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed while getting User Informations");
                })
        };
        this.putUser = function (person) {
            var data = {};
            data.user = person;
            $http.put(PREFIX_RQ + "/api/app/account/" + this.person.userID, data)
                .success(function (data, status, headers, config) {
                    console.log("Succeed");
                    user.class = "btn-success";
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed while editing User Informations");
                    user.class = "btn-danger";
                });
        };
        this.getUser();

    }]);