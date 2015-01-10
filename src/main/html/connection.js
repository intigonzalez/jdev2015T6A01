
PREFIX_RQ = "";
//PREFIX_RQ = "http://localhost:9998";


var ConnectionForm = angular.module('ConnectionForm', []);
//Hello
ConnectionForm.controller("mainController", function ($scope, $http) {

    $scope.submitData = function (person) {
        var data = {};
        data.user = person;
        $http.post(PREFIX_RQ+"/api/app/account/Connect",data )
            .success(function (data, status, headers, config)
            {
                console.log("Succeed");
                //console.log(person.name);
                window.location.replace("http://localhost:9998/index.html?email="+person.name);
                
            })
            .error(function (data, status, headers, config)
            {
                console.log("Failed");
            });
    };
 
    angular.element(document).ready(function(){
        $("form").validate({
            rules: {
                name:{
                    minlength: 3,
                    maxlength: 20,
                    required: true
                }
            },
            highlight: function (element) {
                $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
            },
            unhighlight: function (element) {
                $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
            }
        });
    });

    // End of js part for validating the form inputs


});