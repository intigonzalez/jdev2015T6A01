'use strict';

angular.module('myApp.mypictures', ['ngRoute', 'ui.bootstrap'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/mypictures', {
            templateUrl: 'views/mypictures/mypictures.html',
            controller: 'MyPicturesCtrl'
        });
    }])

    .controller('MyPicturesCtrl', [function ($scope, $http) {


    }])

    .controller('MyPicturesController', ['$scope', '$http', '$window', '$modal', '$log', function($scope, $http, $window, $modal, $log) {

        var pictures = this;

        pictures.list = [];
        pictures.roles = [
            {"roleID":"0" , "roleName":"public", "info":"Seen by all your relations"},
            {"roleID":"1" , "roleName":"Family", "info":"Seen by all your family only"},
            {"roleID":"2" , "roleName":"Friends", "info":"Seen by all your friends only"},
            {"roleID":"3" , "roleName":"Pro", "info":"Seen by all your professional contacts"},
        ];  //List of role
        this.getPictures = function() {
            $http.get(PREFIX_RQ + "/api/app/" + userID + "/content")
                .success(function (data, status, headers, config) {
                    if ( data.contents !== "" ) {
                        if (angular.isArray(data.contents.content) == false) {
                        	if(data.contents.content.type === "image")
                        		pictures.list.push(data.contents.content);
                        }
                        else {
                            pictures.list = $.grep(data.contents.content, function(o){return o.type === "image"});
                        }
                    }

                })
                .error(function (data, status, headers, config) {
                    console.log("Failed while getting Pictures Informations");
                })
        };
        this.generateLink = function(content) {
             if (content.status == "success") {
                return content.link+"/original.jpg";
            }
            else {
                return "";
            }
        };
        this.pictureInProgress = function(content) {
             if (content.status == "success") {
                 return "";
             }
             else {
                return "disabled";
             }
         };
        this.getIndex = function(content) {
            return pictures.list.indexOf(content);
        }


        // **** Function to update a content
        this.updateContent = function(content) {
            var data = {"content" : content};
            $http.put(PREFIX_RQ+"/api/app/"+userID+"/content/"+content.contentsID,  data)
                .success(function() {
                    console.log("success");
                })
                .error(function() {
                    console.log("error");
                })
        }

        // ***** Remove a picture *****
        this.removePicture = function(content) {
            $http.delete(PREFIX_RQ + "/api/app/" + userID + "/content/"+content.contentsID)
                .success(function(data,status,headers,config) {
                    var index = pictures.getIndex(content);
                    if (index > -1) {
                        pictures.list.splice(index, 1);
                    }
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed");
                })
        }

        this.showDetails = function(content) {
            $scope.open(content);
        }
        this.getPictures();


        $scope.open = function (content, size) {
            var modalInstance = $modal.open({
                templateUrl: 'PicturesModalContent.html',
                controller: 'PicturesModalInstanceCtrl',
                size: size,
                resolve: {
                	roles: function () {
                        return pictures.roles;
                    },
                    picture: function () {
                        return content;
                    }
                }
            });

            modalInstance.result.then(function (picture) {
                pictures.updateContent(picture);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        }



    }])
    .controller('PicturesModalInstanceCtrl', ['$scope', '$modalInstance', 'roles', 'picture', function ($scope, $modalInstance, roles, picture) {

        $scope.roles = angular.copy(roles);
        if (picture.metadata === undefined) {
        } else {
            if ( angular.isArray(picture.metadata) ) {
                angular.forEach(picture.metadata, function (id) {
                    var index = searchItemIntoArrayWithAttribute($scope.roles, "roleID", id);
                    $scope.roles[index].value = true;
                });
            }
            else {
                var index = searchItemIntoArrayWithAttribute($scope.roles, "roleID", picture.metadata);
                $scope.roles[index].value=true;
            }
        }
        //console.log(roles);

        $scope.ok = function () {
            //console.log($scope.roles);
            picture.metadata= [];
            angular.forEach($scope.roles, function(role) {
                if (role.value == true) {
                    picture.metadata.push(role.roleID)
                }
            });

            $modalInstance.close(picture);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);