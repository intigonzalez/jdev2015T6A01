'use strict';

angular.module('myApp.friends', ['ngRoute', 'ui.bootstrap'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/friends', {
            templateUrl: 'views/friends/friends.html',
            controller: 'FriendsCtrl'
        });
    }])
    .controller('FriendsCtrl', [ function () {
    }])
    .controller('FriendsController', ['$scope', '$http', '$modal', '$log', function ($scope, $http, $modal, $log) {

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

        // **** Function to update a friend
        this.updateFriend = function(friend) {
            var data = {"relation" : friend};
            $http.put(PREFIX_RQ+"/api/app/"+userID+"/relation/"+friend.email, data)
                .success(function() {
                    console.log("success");
                })
                .error(function() {
                    console.log("error");
                })
        }


        this.AprouveFriend = function(friend) {
            friend.aprouve = 3;
            friends.updateFriend(friend);
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
        $scope.addFriend = function(friend_userID) {

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

        this.showgroups = function(friend) {
            $scope.open(friend);
        }


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
                friends.updateFriend(friend);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
        };

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
                $scope.listGroups[friend.groupID].value=true; //to improve, quite dirty
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