
PREFIX_RQ = "";
//PREFIX_RQ = "http://localhost:9998";


var RegisterForm = angular.module('RegisterForm', []);
//Hello
RegisterForm.controller("mainController", function ($scope, $http) {

    $scope.submitData = function (person) {
        var data = {};
        data.user = person;
        $http.post(PREFIX_RQ+"/api/app/account/",data )
            .success(function (data, status, headers, config)
            {
                console.log("Succeed");
            })
            .error(function (data, status, headers, config)
            {
                console.log("Failed");
            });
    };
   // $(document) Jquery for validating the form -->
    angular.element(document).ready(function(){
        $("form").validate({
            rules: {
                name:{
                    minlength: 3,
                    maxlength: 20,
                    required: true
                },
                email:{
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

$(document).ready(function(){
    $("registerForm").validate({
        rules: {
            name:{
                minlength: 3,
                maxlength: 20,
                required: true
            },
            email:{
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