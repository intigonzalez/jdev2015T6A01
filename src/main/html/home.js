/**
 * Created by Charles-Damien on 10/01/15.
 */
'use strict';


function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}

function userAuth(){
	if(getCookie("authentication")=="" ){
        window.location.replace("/");	
	}
	
}

angular.module('myApp.index', [])

    .controller('IndexController', [ function () {
        this.logout = function() {
        	document.cookie = "authentication=; expires=Thu, 01 Jan 2000 00:00:00 GMT"; 
            window.location.replace("/");
        }
    }]);
