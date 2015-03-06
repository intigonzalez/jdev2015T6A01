'use strict';

angular.module('myApp.friends', ['ngRoute', 'ui.bootstrap'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/friends', {
            templateUrl: 'views/friends/friends.html',
            controller: 'FriendsCtrl'
        });
    }])
    .controller('FriendsCtrl', ['$scope', '$http', '$window', function ($scope, $http, $window) {

        var videos = this;
        //Determine the right steraming protocol.
        var userAgent = $window.navigator.userAgent;
        if ( userAgent.indexOf("Chrome") >= 0 || userAgent.indexOf("Windows") >=0 || userAgent.indexOf("Chromium") >=0 ) {
            videos.prefix = 'dash';
            videos.suffix = 'dash/playlist.mpd';
        }
        else {
            videos.prefix = 'hls';
            videos.suffix = 'hls/playlist.m3u8';
        }

        //Init Objects
        $scope.FriendsCtrl ={}; // Just use for tabs

        $scope.FriendProfileController = {}; // use to give all the items to the other controller
        $scope.FriendProfileController.getVideoContent = function(friend) {
            $scope.FriendProfileController.videos = []; // object to store videos
            $http.get(PREFIX_RQ+"/api/app/"+userID+"/relation/"+friend.actorID +"/content")
                .success(function(data, status, headers, config) {
                    if ( data.contents !== "" ) {
                        if (angular.isArray(data.contents.content) == false) {
                            $scope.FriendProfileController.videos.push(data.contents.content);
                        }
                        else {
                            $scope.FriendProfileController.videos = data.contents.content;
                        }
                        //console.log($scope.FriendProfileController.videos);
                    }
                })
                .error(function (data, status, headers, config){
                    console.log("Failed getting Video Content from your relation");
                })

        }
        $scope.FriendProfileController.generateLink = function(content) {
            if (content.status == "success") {
                return videos.prefix + ".html?url=" + content.link + "/" + videos.suffix;
            }
            else {
                return "";
            }
        }
        $scope.FriendProfileController.videoInProgress = function(content) {
            if (content.status == "success") {
                return "";
            }
            else {
                return "disabled";
            }
        };

        $scope.FriendsCtrl.tabValue = 0;
        $scope.FriendsCtrl.isSet = function(int) {
            if ($scope.FriendsCtrl.tabValue == int) {
                return true
            }
            else {
                return false;
            }
        }
        $scope.FriendsCtrl.SetTab = function(int) {
            $scope.FriendsCtrl.tabValue = int;
        }
        $scope.DisplayFriendProfile = function(friend) {
            //console.log("Display Friend Profile");
            $scope.FriendsCtrl.SetTab(1);
            $scope.FriendProfileController.friend= friend;
            $scope.FriendProfileController.getVideoContent(friend);
        }
        $scope.HideFriendProfile = function() {
            $scope.FriendProfileController.friend= null;
            $scope.FriendsCtrl.SetTab(0);
            console.log("Hide Friend Profile");
        }


    }])
    .controller('FriendsListController', ['$scope', '$http', '$modal', '$log', function ($scope, $http, $modal, $log) {

        var friends = this; // This element to get it easily
        friends.list = []; //Friend list into the controller
        friends.addFriendSuccess =null; //boolean to change the color of add Friend button
        friends.listRoles = [
            {"roleID":"0" , "roleName":"public", "info":"Seen by all your relations"},
            {"roleID":"1" , "roleName":"Family", "info":"Seen by all your family only"},
            {"roleID":"2" , "roleName":"Friends", "info":"Seen by all your friends only"},
            {"roleID":"3" , "roleName":"Pro", "info":"Seen by all your professional contacts"},
        ];  //List of roles

        // *****************  Get RoleList****************
        this.getRoleList = function() {
            $http.get(PREFIX_RQ+"/api/app/"+userID+"/role")
                .success(function(data, status, headers, config) {
                    if ( data.relations !== "" ) {
                        if (angular.isArray(data.listRoles.roles) == false) {
                            friends.listRoles.push(data.listRoles.roles);
                        }
                        else {
                            friends.listRoles = data.listRoles.roles;
                        }
                    }
                })
                .error(function (data, status, headers, config){
                    console.log("Failed getting Roles list");
                })
        };
        //this.getRoleList(); //Disabled because the endpoint doesn't exist yet !

        //This function is called to display the list of roles a relation belongs to.
        this.generateRoleList = function(friend) {
            var result = [];
            if (friend.roleID !== undefined) {
                if ( angular.isArray(friend.roleID) ) {
                    angular.forEach(friend.roleID, function (id) {
                        var index = searchItemIntoArrayWithAttribute(friends.listRoles, "roleID", id);
                        result.push(friends.listRoles[index].roleName);
                    });
                }
                else {
                    result[0] = friends.listRoles[friend.roleID].roleName
                }
            }
            return result;
        }

        // *****************  Get FriendList ****************
        
        this.getFriendList = function() {
            $http.get(PREFIX_RQ+"/api/app/"+userID+"/relation")
                .success(function(data, status, headers, config) {
                    if ( data.relations !== "" ) {
                        if (angular.isArray(data.relations.relation) == false) {
                            friends.list.push(data.relations.relation);
                        }
                        else {
                            friends.list = data.relations.relation;
                        }
                    }
                    //console.log(friends.list);
                    friends.addFriendSuccess =null; //reset the value if the function is called from AddFriend Callback Success
                })
                .error(function (data, status, headers, config){
                    console.log("Failed getting Friend list");
                })
        };
        this.getFriendList();

        //Get Aprouve Status to adapt display
        this.isStatus = function(friend, str) {
            if (friend.aprouve == str) {
                return true;
            }
           else {
                return false;
           }
        }

        // **** Function to update a friend with PUT Request
        this.updateRelation = function(friend) {
            var data = {"relation" : friend};
            $http.put(PREFIX_RQ+"/api/app/"+userID+"/relation/"+friend.actorID, data)
                .success(function() {
                    console.log("success");
                })
                .error(function() {
                    console.log("error");
                })
        }

        //Function to call to accept a relation request
        this.AprouveRelation = function(friend) {
            friend.aprouve = 3;
            friends.updateRelation(friend);
        }

        //function to change the color of add Friend button
        this.isFriendAddingSuccess = function() {
            if (friends.addFriendSuccess == null) {
                return "";

            }
            else if ( friends.addFriendSuccess == 'success') {
                return "btn-success";
            }
            else {
                return "btn-danger";
            }
        }

        // ********  Add a Friend to the friendList **********
        $scope.addRelation = function(friend_userID) {

            $http.post(PREFIX_RQ + "/api/app/" + userID + "/relation/"+friend_userID)
                .success(function (data, status, headers, config) {
                    console.log("Succeed");
                    // Friend Added successfully
                    friends.addFriendSuccess = 'success';
                    friends.getFriendList();
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed");
                    friends.addFriendSuccess = 'failed';
                })
        }

        this.getIndex = function(friend) {
            return friends.list.indexOf(friend);
        }

        // ***** Remove a friend *****
        this.removeRelation = function(friend) {
            $http.delete(PREFIX_RQ + "/api/app/" + userID + "/relation/"+friend.actorID)
                .success(function(data,status,headers,config) {
                    var index = friends.getIndex(friend);
                    if (index > -1) {
                        friends.list.splice(index, 1);
                    }
                })
                .error(function (data, status, headers, config) {
                    console.log("Failed");
                })
        }

        // ***** Modal *****
        this.showRoles = function(friend) {
            $scope.open(friend);
        }

        //Enable Modal to edit roles
        $scope.open = function (friend, size) {
            var modalInstance = $modal.open({
                templateUrl: 'myModalContent.html',
                controller: 'ModalInstanceCtrl',
                size: size,
                resolve: {
                    listRoles: function () {
                        return friends.listRoles;
                    },
                    friend: function() {
                        return friend;
                    }
                }
            });

            modalInstance.result.then(function (friend) {
                friends.updateRelation(friend);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

        // ***** Collapse *****
        //friends.collapse = null;
        //friends.isCollapsed = function(friend) {
        //    if (friends.collapse == friend.actorID ) {
        //        return false;
        //    }
        //    else {
        //        return true;
        //    }
        //}
        //friends.setCollapsed = function(friend) {
        //    if (friends.isCollapsed(friend) == false) {
        //        friends.collapse = null;
        //    }
        //    else {
        //        friends.collapse = friend.actorID;
        //    }
        //}


    }])


    .controller('ModalInstanceCtrl', ['$scope', '$modalInstance', 'listRoles', 'friend', function ($scope, $modalInstance, listRoles, friend) {


        //console.log(friend);
        $scope.listRoles = angular.copy(listRoles);
        if (friend.roleID === undefined) {
        } else {
            if ( angular.isArray(friend.roleID) ) {
                angular.forEach(friend.roleID, function (id) {
                    var index = searchItemIntoArrayWithAttribute($scope.listRoles, "roleID", id);
                    $scope.listRoles[index].value = true;
                });
            }
            else {
                var index = searchItemIntoArrayWithAttribute($scope.listRoles, "roleID", friend.roleID);
                $scope.listRoles[index].value=true;
            }
        }
        //console.log(listRoles);

        $scope.ok = function () {
            //console.log($scope.listRoles);
            friend.roleID = [];
            angular.forEach($scope.listRoles, function(role) {
                if (role.value == true) {
                    friend.roleID.push(role.roleID);
                }
            });

            //console.log(friend);
            $modalInstance.close(friend);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);

