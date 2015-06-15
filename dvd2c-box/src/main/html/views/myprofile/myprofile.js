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
    .controller('ProfileController', ['$http','$location', function ($http, $location) {

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
        	user.service=service;
     	   return "http://" + $location.host() + ":9997/api/oauth/" + user.person.userID + "/" + service;
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
            $http.put(PREFIX_RQ + "/api/app/account/",data)// + this.person.userID, data)
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
        	$http.get(PREFIX_RQ + "/api/app/account/")
            .success(function (data, status, headers, config)
            {
 	if (headers('Content-Type').indexOf("text/html")==0) {
					window.location.replace("/");
				} 
 				console.log(data);
            	var json=JSON.parse(angular.toJson(data));
            	console.log(json.user.propertyGroups);
            	if(json.user.PropertyGroups!=undefined){
	            	var l=json.user.propertyGroups.length;
	            	if(l==undefined){
	            		l=0;
	            		var e = json.user.propertyGroups;
	            		if(e.property.length==undefined){
	            			var p = e.property;
	            			user.smtp[p.key] = p.value;
	            		}
	            		else{
	            			for(var j = 0; j < e.property.length; j++) {
	            				var p = e.property[j];
	            				console.log("p: "+j);
	                    		console.log(p);
	            				user.smtp[p.key] = p.value;
	            			}
	            		}
	                }
	            	else{
		            	for(var i = 0; i <= l ; i++) {
		            		var e = json.user.propertyGroups[i];
		            		console.log("e: "+i);
		            		console.log(e);
		            		console.log(e.name);
		            		console.log(e.name == "snapmail")
		            		if(e.name == "snapmail") {
		            			console.log("OK")
		            			if(e.property.length==undefined){
		                			var p = e.property;
		                			user.smtp[p.key] = p.value;
		                		}
		                		else{
		                			for(var j = 0; j < e.property.length; j++) {
		                				var p = e.property[j];
		                				console.log("p: "+j);
		                        		console.log(p);
		                				user.smtp[p.key] = p.value;
		                			}
		                		}
		            			break;
		            		}
		            	}
	            	}
	            	console.log(user.smtp)
	            	if (user.smtp.hasOwnProperty("username") && user.smtp.username != "" && user.smtp.username != undefined && user.smtp.hasOwnProperty("host") && user.smtp.host != "" && user.smtp.host != undefined && user.smtp.hasOwnProperty("port") && user.smtp.port != "" && user.smtp.port != undefined && user.smtp.hasOwnProperty("password") && user.smtp.password != "" && user.smtp.password != undefined)
	            	{
	            		user.smtpManualSettings = true;1
	                	user.smtpTab = 1;
	                	console.log("view 1");
	            	}
	            	else if (user.smtp.hasOwnProperty("google") && user.smtp.google != "" && user.smtp.google != undefined) {
	            		user.smtpTab = 2;
	            	console.log("view 2");
	            	}
	            	else if (user.smtp.hasOwnProperty("yahoo") && user.smtp.yahoo != "" && user.smtp.yahoo != undefined) {
	            		user.smtpTab = 3;
	            	console.log("view 3");
	            	}
	            	else if (user.smtp.hasOwnProperty("microsoft") && user.smtp.microsoft != "" && user.smtp.microsoft != undefined) {
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
            			property: [],
            			name:"Snapmail"
            		}
            };
            
            if(smtp.host != "")
            	data.propertyGroups.property.push({key: "host", value: smtp.host});
           	if(smtp.port != "")
            	data.propertyGroups.property.push({key: "port", value: smtp.port});
            if(smtp.username != "")
            	data.propertyGroups.property.push({key: "username", value: smtp.username});
            if(smtp.password != "")
            	data.propertyGroups.property.push({key: "password", value: smtp.password});
           	if(smtp.google != "")
            	data.propertyGroups.property.push({key: "google", value: smtp.google});
            if(smtp.microsoft != "")
            	data.propertyGroups.property.push({key: "microsoft", value: smtp.microsoft});
            if(smtp.yahoo != "")
            	data.propertyGroups.property.push({key: "yahoo", value: smtp.yahoo});
            	
            $http.put(PREFIX_RQ + "/api/app/" + this.person.uuid + "/properties", data)
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

