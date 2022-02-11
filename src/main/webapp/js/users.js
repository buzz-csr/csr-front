var usersModule = angular.module('usersModule', []);

usersModule.controller('usersCtrl', [ '$scope', '$http', function($scope, $http) {
    $scope.getUsers = function() {
        $http({
            method : 'get',
            url : '/csr-front/users',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
        }).then(function successCallback(response) {
            $scope.users = response.data;
        });
    }
    $scope.getUsers();
} ]);