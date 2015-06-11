'use strict';

angular.module('myApp.mySnapmailprofile', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/mySnapmailprofile', {
            templateUrl: 'snapmail/views/profile/profile.html',
            controller: 'ProfileCtrl'
        });
    }])
    .controller('ProfileCtrl',[function() {

    }])
    .controller('ProfileController', ['$http', function ($http) {

        var user = this;
       
        user.person = {};
        user.smtp = {};
        user.class= "";
        this.tab = 0;
        this.smtpManualSettings = false;
        this.smtpTab = 0;
        
        this.setTab = function (value) {
            user.class="";
            if (this.tab == value) {
                this.tab = 0;
            } else {
                this.tab = value;
            }
        };
        this.isSetTab = function (value) {
            return this.tab === value;
        };
        
        this.openOauth = function (service) {
     	   return "/api/oauth/" + service;
         };

        this.getUser = function () {
            $http.get(PREFIX_RQ + "/api/app/account/")// + userID)
                .success(function (data, status, headers, config) {
                	if (headers('Content-Type').indexOf("text/html")==0) {
    					window.location.replace("/");
    				} 
                    user.person = data.user;
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed while getting User Informations");
                })
        };
        this.putUser = function (person) {
            var data = {};
            data.user = person;
            $http.put(PREFIX_RQ + "/api/app/account/")// + this.person.userID, data)
                .success(function (data, status, headers, config) {
                	if (headers('Content-Type').indexOf("text/html")==0) {
    					window.location.replace("/");
    				} 
                    console.log("Succeed");
                    user.class = "btn-success";
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed while editing User Informations");
                    user.class = "btn-danger";
                });
        };
        
        this.getSmtp = function ()
        {
        	$http.get(PREFIX_RQ + "/api/app/snapmail/smtp")
            .success(function (data, status, headers, config)
            {if (headers('Content-Type').indexOf("text/html")==0) {
				window.location.replace("/");
			} 
                user.smtp = data.smtpProperty;
                if (user.smtp.hasOwnProperty("token") && user.smtp.token != "")
                	user.smtpTab = 1;
                else if (user.smtp.hasOwnProperty("username") && user.smtp.username != "" && user.smtp.hasOwnProperty("host") && user.smtp.host != "" && user.smtp.hasOwnProperty("port") && user.smtp.port != "" && user.smtp.hasOwnProperty("password") && user.smtp.password != "")
                {
                	user.smtpManualSettings = true;
                	user.smtpTab = 2;
                }
                else
                	user.smtp = {
                		host: "",
                		port: "",
                		username: "",
                		password: "",
                		token: ""
                };
            })
            .error(function (data, status, headers, config) {
                console.log("Failed while getting User Informations");
            })
        };
        
        this.putSmtp = function (smtp)
        {
            var data = {};
            data.smtpProperty = smtp;
            console.log(smtp);
            $http.put(PREFIX_RQ + "/api/app/snapmail/" + this.person.userID + "/smtp", data)
                .success(function (data, status, headers, config) {
                	if (headers('Content-Type').indexOf("text/html")==0) {
    					window.location.replace("/");
    				} 
                    console.log("Succeed");
                    user.class = "btn-success";
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed while editing SMTP settings");
                    user.class = "btn-danger";
                });
        }

        this.getUser();
        this.getSmtp();
        
        this.setSMTPTab = function(value)
        {
        	this.smtpTab = value;
        	if (value == 5)
        	{
        		if (user.smtp.host == "" || user.smtp.username == "" || user.smtp.password == "" || user.smtp.port == "")
        			user.setSMTPTab(0);
        		else
        			user.setSMTPTab(2);
        	}
        }
        
        this.isSMTPTab = function(value)
        {
        	return (this.smtpTab == value);
        }
        
    
    }]);

