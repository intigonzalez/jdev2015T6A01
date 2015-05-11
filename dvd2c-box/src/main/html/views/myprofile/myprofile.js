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
        user.smtp = {};
        user.person = {};

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
        	user.service=service;
     	   return "/api/oauth/" + user.person.userID + "/" + service;
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
        	// Temporary until endpoints work
        	$http.get(PREFIX_RQ + "/api/app/account/" + userID)
            .success(function (data, status, headers, config)
            {
            	for(var i = 0, l = data.user.propertyGroups.length; i < l; i++) {
            		var e = data.user.propertyGroups[i];
            		console.log("e: "+i);
            		console.log(e);
            		console.log(e.name);
            		console.log(e.name == "snapmail")
            		if(e.name == "snapmail") {
            			console.log("OK")
            			for(var j = 0, m = e.props.length; j < m; j++) {
            				var p = e.props[j];
            				console.log("p: "+j);
                    		console.log(p);
            				user.smtp[p.key] = p.value;
            			}
            			break;
            		}
            	}
            	console.log(user.smtp)
            	if (user.smtp.hasOwnProperty("username") && user.smtp.username != "" && user.smtp.username !=  && user.smtp.hasOwnProperty("host") && user.smtp.host != "" && user.smtp.hasOwnProperty("port") && user.smtp.port != "" && user.smtp.hasOwnProperty("password") && user.smtp.password != "")
            	{
            		user.smtpManualSettings = true;
                	user.smtpTab = 1;
                	console.log("view 1");
            	}
            	else if (user.smtp.hasOwnProperty("google") && user.smtp.google != "") {
            		user.smtpTab = 2;
            	console.log("view 2");
            	}
            	else if (user.smtp.hasOwnProperty("yahoo") && user.yahoo != "") {
            		user.smtpTab = 3;
            	console.log("view 3");
            	}
            	else if (user.smtp.hasOwnProperty("microsoft") && user.microsoft != "") {
            		user.smtpTab = 4;
            	console.log("view 4");
            	}
            	else {
            		user.smtp = {
            			host: "",
            			port: "",
            			username: "",
            			password: "",
            			google: "",
            			yahoo: "",
            			microsoft: "",
            		};
            		console.log("view else");
            	}
            })
            .error(function (data, status, headers, config) {
            	console.log("Failed while getting User Informations");
            })
        };

        this.putSmtp = function (smtp)
        {
            var data = {
            		propertyGroups: {
            			props: []
            		}
            };
            
            if(smtp.host != "")
            	data.propertyGroups.props.push({key: "host", value: smtp.host});
           	if(smtp.port != "")
            	data.propertyGroups.props.push({key: "port", value: smtp.port});
            if(smtp.username != "")
            	data.propertyGroups.props.push({key: "username", value: smtp.username});
            if(smtp.password != "")
            	data.propertyGroups.props.push({key: "password", value: smtp.password});
           	if(smtp.google != "")
            	data.propertyGroups.props.push({key: "google", value: smtp.google});
            if(smtp.microsoft != "")
            	data.propertyGroups.props.push({key: "microsoft", value: smtp.microsoft});
            if(smtp.yahoo != "")
            	data.propertyGroups.props.push({key: "yahoo", value: smtp.yahoo});
            	
            $http.put(PREFIX_RQ + "/api/app/account/" + this.person.userID + "/properties/snapmail", data)
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
        	if (value == 7)
        	{
        		if (user.smtp.host == "" || user.smtp.username == "" || user.smtp.password == "" || user.smtp.port == "")
        			user.setSMTPTab(0);
        		else
        			user.setSMTPTab(1);
        	}
        }
        
        this.isSMTPTab = function(value)
        {
        	return (this.smtpTab == value);
        }
        
    
    }]);

