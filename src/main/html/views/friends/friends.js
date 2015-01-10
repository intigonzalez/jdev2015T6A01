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
        if ( userAgent.indexOf("Chrome") >= 0 || userAgent.indexOf("Windows") >=0 ) {
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
            $http.get(PREFIX_RQ+"/api/app/"+userID+"/relation/"+friend.email +"/content")
                .success(function(data, status, headers, config) {
                    if ( data.listContent !== "" ) {
                        if (angular.isArray(data.listContent.content) == false) {
                            $scope.FriendProfileController.videos.push(data.listContent.content);
                        }
                        else {
                            $scope.FriendProfileController.videos = data.listContent.content;
                        }
                        console.log($scope.FriendProfileController.videos);
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
            console.log("Display Friend Profile");
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
        friends.listGroups = [
            {"groupID":"0" , "groupName":"public", "info":"Seen by all your relations"},
            {"groupID":"1" , "groupName":"Family", "info":"Seen by all your family only"},
            {"groupID":"2" , "groupName":"Friends", "info":"Seen by all your friends only"},
            {"groupID":"3" , "groupName":"Acquaintance", "info":"Seen by all your acquaintance"},
        ];  //List of groups

        // *****************  Get GroupList****************
        this.getGroupList = function() {
            $http.get(PREFIX_RQ+"/api/app/"+userID+"/group")
                .success(function(data, status, headers, config) {
                    if ( data.listRelation !== "" ) {
                        if (angular.isArray(data.listGroups.groups) == false) {
                            friends.listGroups.push(data.listGroups.groups);
                        }
                        else {
                            friends.listGroups = data.listGroups.groups;
                        }
                    }
                })
                .error(function (data, status, headers, config){
                    console.log("Failed getting Groups list");
                })
        };
        //this.getGroupList(); //Disabled because the endpoint doesn't exist yet !

        //This function is called to display the list of groups a relation belongs to.
        this.generateGroupList = function(friend) {
            var result = [];
            if (friend.groupID !== undefined) {
                if ( angular.isArray(friend.groupID) ) {
                    angular.forEach(friend.groupID, function (id) {
                        var index = searchItemIntoArrayWithAttribute(friends.listGroups, "groupID", id);
                        result.push(friends.listGroups[index].groupName);
                    });
                }
                else {
                    result[0] = friends.listGroups[friend.groupID].groupName
                }
            }
            return result;
        }

        // *****************  Get FriendList ****************
        
        this.getFriendList = function() {
            $http.get(PREFIX_RQ+"/api/app/"+userID+"/relation")
                .success(function(data, status, headers, config) {
                    if ( data.listRelation !== "" ) {
                        if (angular.isArray(data.listRelation.relation) == false) {
                            friends.list.push(data.listRelation.relation);
                        }
                        else {
                            friends.list = data.listRelation.relation;
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
            $http.put(PREFIX_RQ+"/api/app/"+userID+"/relation/"+friend.email, data)
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
            $http.delete(PREFIX_RQ + "/api/app/" + userID + "/relation/"+friend.email)
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
        this.showgroups = function(friend) {
            $scope.open(friend);
        }

        //Enable Modal to edit groups
        $scope.open = function (friend, size) {
            var modalInstance = $modal.open({
                templateUrl: 'myModalContent.html',
                controller: 'ModalInstanceCtrl',
                size: size,
                resolve: {
                    listGroups: function () {
                        return friends.listGroups;
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
        //    if (friends.collapse == friend.email ) {
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
        //        friends.collapse = friend.email;
        //    }
        //}


    }])


    .controller('ModalInstanceCtrl', ['$scope', '$modalInstance', 'listGroups', 'friend', function ($scope, $modalInstance, listGroups, friend) {


        //console.log(friend);
        $scope.listGroups = angular.copy(listGroups);
        if (friend.groupID === undefined) {
        } else {
            if ( angular.isArray(friend.groupID) ) {
                angular.forEach(friend.groupID, function (id) {
                    var index = searchItemIntoArrayWithAttribute($scope.listGroups, "groupID", id);
                    $scope.listGroups[index].value = true;
                });
            }
            else {
                var index = searchItemIntoArrayWithAttribute($scope.listGroups, "groupID", friend.groupID);
                $scope.listGroups[index].value=true;
            }
        }
        //console.log(listGroups);

        $scope.ok = function () {
            //console.log($scope.listGroups);
            friend.groupID = [];
            angular.forEach($scope.listGroups, function(group) {
                if (group.value == true) {
                    friend.groupID.push(group.groupID);
                }
            });

            //console.log(friend);
            $modalInstance.close(friend);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    }]);
