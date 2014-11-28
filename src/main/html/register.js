var RegisterForm = angular.module('RegisterForm', []);
//Hello
RegisterForm.controller("mainController", function ($scope, $http) {

    $scope.submitData = function (person) {
        var data = {};
        data.user = person;
        $http.post("/api/app/account/",data )
            .success(function (data, status, headers, config)
            {
                console.log("Succeed");
            })
            .error(function (data, status, headers, config)
            {
                console.log("Failed");
            });
    };
});