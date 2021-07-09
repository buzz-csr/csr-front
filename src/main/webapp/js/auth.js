var authModule = angular.module('authModule', []);

authModule.controller('authCtrl', [ '$scope', '$http', function($scope, $http) {
    $scope.playerId = "";
    
    $scope.getAuth = function() {
        $http({
            method : 'get',
            url : '/csr-front/auth',
            params : {
                playerId : $scope.playerId,
                type: 'license',
            },
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
        }).then(function successCallback(response) {
            $scope.result = response.data.key;
        });
    }

} ]);