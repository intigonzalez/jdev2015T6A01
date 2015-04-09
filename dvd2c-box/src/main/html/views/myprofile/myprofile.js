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
        
        this.openOauth = function () {
     	   var url="https://accounts.google.com/o/oauth2/auth?access_type=offline&approval_prompt=force&client_id=547107646254-uh9ism7k6qoho9jdcbg4v4rg4tt5pid0.apps.googleusercontent.com&redirect_uri=urn:ietf:wg:oauth:2.0:oob&response_type=code&scope=https://www.googleapis.com/auth/gmail.compose";
         	window.open(url, "_blank", "toolbar=no, scrollbars=yes, resizable=yes");
         };
         
         this.auth2token = function (value){
        	 var req = {
        			 method: 'POST',
        			 url: 'https://www.googleapis.com/oauth2/v3/token',
        			 headers: {
        			   'Content-Type': 'application/x-www-form-urlencoded'
        			 },
        			 data : "client_id=547107646254-uh9ism7k6qoho9jdcbg4v4rg4tt5pid0.apps.googleusercontent.com&client_secret=JG3LiwiX2gA362mTSGEJ5eC8&code="+value+"&redirect_uri=urn:ietf:wg:oauth:2.0:oob&grant_type=authorization_code&approval_promt=force&access_type=offline"
        			};
 
        
             $http(req)  
             .success(function (data, status, headers, config) {
                 console.log("Succeed");
                 var data_json = angular.toJson(data);
                 var jsonobj=JSON.parse(data_json);
                 user.smtp.token=jsonobj.refresh_token;
                 user.code="";
                 user.putSmtp(user.smtp);
                 user.class = "btn-success";
             })
             .error(function (data, status, headers, config) {
                 console.log("Failed while editing User Informations");
                 user.class = "btn-danger";
             });
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
        
        this.getSmtp = function ()
        {
        	$http.get(PREFIX_RQ + "/api/app/account/" + userID + "/smtp")
            .success(function (data, status, headers, config)
            {
                user.smtp = data.smtpProperty;
                if (user.smtp.hasOwnProperty("token"))
                	user.smtpTab = 1;
                else if (user.smtp.hasOwnProperty("username") && user.smtp.hasOwnProperty("host") && user.smtp.hasOwnProperty("port") && user.smtp.hasOwnProperty("password"))
                {
                	user.smtpManualSettings = true;
                	user.smtpTab = 2;
                }
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
            $http.put(PREFIX_RQ + "/api/app/account/" + this.person.userID + "/smtp", data)
                .success(function (data, status, headers, config) {
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
        }
        
        this.isSMTPTab = function(value)
        {
        	return (this.smtpTab == value);
        }
        
    
    }]);

