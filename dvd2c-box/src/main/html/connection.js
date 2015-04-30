
PREFIX_RQ = "";
//PREFIX_RQ = "http://localhost:9998";


var ConnectionForm = angular.module('ConnectionForm', ['ngMockE2E','ngCookies']);

ConnectionForm.run(function($httpBackend,$cookieStore ) {

	// returns the current list of phones	
	$httpBackend.when('POST', '/api/app/account/Connect').respond(
			function(method, url, data, headers) {
					data=JSON.parse(data);
					if(data.user.userID=="testtest"){
						var user='testtest';
						console.log(user);
						$cookieStore.put('authentication',user); 
						 
						return [ 200, {}, {} ];
					}
					else if(data.user.userID=="test@test.fr"){
						$cookieStore.put("authentication",'test@test.fr'); 
						return [ 200, {}, {} ];
					}
					else{
						return [ 403, {}, {} ];
					}
			});
});


ConnectionForm.controller("mainController", function ($scope, $http) {
    var errorConnection = false;
    $scope.CredentialsCheck = function(){
      if(errorConnection == true){
          return "btn-theme04";
      }
        else{
          return "btn-theme";
      }
    };
    $scope.submitData = function (person) {
        var data = {};
        data.user = person;
        $http.post(PREFIX_RQ+"/api/app/account/Connect",data )
            .success(function (data, status, headers, config)
            {
                console.log("Succeed");
                //console.log(person.firstname);
                window.location.replace("/home.html");
                
            })
            .error(function (data, status, headers, config)
            {
                errorConnection = true;
                console.log("Failed");
            });
    };
 
    //$(document).ready(function(){
    //    $("form").validate({
    //        rules: {
    //            name:{
    //                minlength: 3,
    //                maxlength: 20,
    //                required: true
    //            }
    //        },
    //        highlight: function (element) {
    //            $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
    //        },
    //        unhighlight: function (element) {
    //            $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
    //        }
    //    });
    //});

    // End of js part for validating the form inputs


});