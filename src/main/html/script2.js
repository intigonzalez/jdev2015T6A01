$http({
            url: '/user_to_itsr',
            method: "POST",
            data: {application:app, from:d1, to:d2},
            headers: {'Content-Type': 'application/json'}
        }).success(function (data, status, headers, config) {
                $scope.users = data.users; // assign  $scope.persons here as promise is resolved here 
            }).error(function (data, status, headers, config) {
                $scope.status = status + ' ' + headers;
            });
};

angular.module("mainModule", [])
  .controller("mainController", function ($scope, $http)
  {
    $scope.person1 = {};
    $scope.person2 = {};
    $scope.person3 = {};

    $scope.submitData = function (person, resultVarName)
    {
      var config = {
        params: {
          person: person
        }
      };

   $http({
            url: '/user_to_itsr',
            method: "POST",
            data: {application:app, from:d1, to:d2},
            headers: {'Content-Type': 'application/json'}
        }).success(function (data, status, headers, config) {
                $scope.users = data.users; // assign  $scope.persons here as promise is resolved here 
            }).error(function (data, status, headers, config) {
                $scope.status = status + ' ' + headers;
            });
};
    };
  });
