'use strict';

angular.module('myApp.mycloud', ['ngRoute', 'ui.bootstrap'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/mycloud', {
            templateUrl: 'views/mycloud/mycloud.html',
            controller: 'MyCloudCtrl'
        });
    }])

    .controller('MyCloudCtrl', [function ($scope, $http) {


    }])

    .controller('MyCloudController', ['$scope', '$http', '$window', '$modal', '$log', function($scope, $http, $window, $modal, $log) {

        var documents = this;

        documents.list = [];
        documents.roles = [
            {"roleID":"Public" 	, "roleName":"public"	, "info":"Seen by all your relations"},
            {"roleID":"Family" 	, "roleName":"Family"	, "info":"Seen by all your family only"},
            {"roleID":"Friends" , "roleName":"Friends"	, "info":"Seen by all your friends only"},
            {"roleID":"Pro" 	, "roleName":"Pro"		, "info":"Seen by all your professional contacts"},
        ];  // List of role
        this.getDocuments = function() {
            $http.get(PREFIX_RQ + "/api/app/content")
                .success(function (data, status, headers, config) {
                	if (headers('Content-Type').indexOf("text/html")==0) {
    					window.location.replace("/");
    				} 
                    if ( data.contents !== "" ) {
                        if (angular.isArray(data.contents.content) == false) {
                        	if(data.contents.content.type != "image" && data.contents.content.type != "video")
                        		documents.list.push(data.contents.content);
                        }
                        else {
                            documents.list = $.grep(data.contents.content, function(o){return (o.type != "image" && o.type != "video")});
                        }
                    }

                })
                .error(function (data, status, headers, config) {
                    console.log("Failed while getting Documents Informations");
                })
        };
        this.generateLink = function(content) {
             if (content.status == "success") {
                return content.link+"/"+content.name;
            }
            else {
                return "";
            }
        };
        this.documentInProgress = function(content) {
             if (content.status == "success") {
                 return "";
             }
             else {
                return "disabled";
             }
         };
        this.getIndex = function(content) {
            return documents.list.indexOf(content);
        }


        // **** Function to update a content
        this.updateContent = function(content) {
            var data = {"content" : content};
            $http.put(PREFIX_RQ+"/api/app/content/"+content.contentsID,  data)
                .success(function() {
                	if (headers('Content-Type').indexOf("text/html")==0) {
    					window.location.replace("/");
    				} 
                    console.log("success");
                })
                .error(function() {
                    console.log("error");
                })
        }

        // ***** Remove a document *****
        this.removeDocument = function(content) {
            $http.delete(PREFIX_RQ + "/api/app/content/"+content.contentsID)
                .success(function(data,status,headers,config) {
                	if (headers('Content-Type').indexOf("text/html")==0) {
    					window.location.replace("/");
    				} 
                    var index = documents.getIndex(content);
                    if (index > -1) {
                        documents.list.splice(index, 1);
                    }
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed");
                })
        }

        this.showDetails = function(content) {
            $scope.open(content);
        }
        this.getDocuments();


        $scope.open = function (content, size) {
            var modalInstance = $modal.open({
                templateUrl: 'DocumentsModalContent.html',
                controller: 'DocumentsModalInstanceCtrl',
                size: size,
                resolve: {
                	roles: function () {
                        return documents.roles;
                    },
                    document: function () {
                        return content;
                    }
                }
            });

            modalInstance.result.then(function (document) {
                documents.updateContent(document);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        }



    }])
    .controller('DocumentsModalInstanceCtrl', ['$scope', '$modalInstance', 'roles', 'document', function ($scope, $modalInstance, roles, document) {

        $scope.roles = angular.copy(roles);
        if (document.metadata === undefined) {
        } else {
            if ( angular.isArray(document.metadata) ) {
                angular.forEach(document.metadata, function (id) {
                    var index = searchItemIntoArrayWithAttribute($scope.roles, "roleID", id);
                    $scope.roles[index].value = true;
                });
            }
            else {
                var index = searchItemIntoArrayWithAttribute($scope.roles, "roleID", document.metadata);
                $scope.roles[index].value=true;
            }
        }
        // console.log(roles);

        $scope.ok = function () {
            // console.log($scope.roles);
            document.metadata= [];
            angular.forEach($scope.roles, function(role) {
                if (role.value == true) {
                    document.metadata.push(role.roleID)
                }
            });

            $modalInstance.close(document);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);