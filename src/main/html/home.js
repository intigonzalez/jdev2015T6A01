/**
 * Created by Charles-Damien on 10/01/15.
 */
'use strict';

angular.module('myApp.index', [])

    .controller('IndexController', [ function () {
        this.logout = function() {
        	document.cookie = "authentication=; expires=Thu, 01 Jan 2000 00:00:00 GMT"; 
            window.location.replace("/");
        }
    }]);
