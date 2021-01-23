var errorModule = angular.module('errorModule', []).config(function($locationProvider) {
    $locationProvider.html5Mode(true);
});

errorModule.controller('errorCtrl', [ '$scope', '$http', '$location', function($scope, $http, $location) {
    var token = $location.search().token;

    $http({
        method: 'GET',
        url: '/csr-front/check',
        headers: { 'Content-type': 'application/json; charset=UTF-8' },
        params: {
            token:  token,
        }
    }).then(function(response) {
        updateErrors(response.data);
    });

    $scope.correct = function(){
        $http({
            method: 'POST',
            url: '/csr-front/check',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                token:  token,
            }
        }).then(function(response) {
            updateErrors(response.data);
        });
    }
    
    var updateErrors = function(data) {
        $scope.checkErrors = [];
        angular.forEach(data, function(value){
            
            var errorGroup;
            angular.forEach($scope.checkErrors, function(actualError){
                if(actualError.name === value.error){
                    actualError.list.push(value.message);
                    errorGroup = actualError; 
                }
            })
            
            if(errorGroup === undefined){
                errorGroup = {
                    name: value.error,
                    list: [],
                }
                errorGroup.list.push(value.message);
                $scope.checkErrors.push(errorGroup);
            }
        });
        
    }
}])