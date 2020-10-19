var upModule = angular.module('upModule', []);

upModule.directive('ngFile', ['$parse', function ($parse) {
  return {
   restrict: 'A',
   link: function(scope, element, attrs) {
     element.bind('change', function(){

     $parse(attrs.ngFile).assign(scope,element[0].files)
     scope.$apply();
   });
  }
 };
}]);

upModule.controller('upCtrl', ['$scope', '$http', function ($scope, $http) {
  $scope.user;
  
    $scope.upload = function(){
  
   var fd = new FormData();
   angular.forEach($scope.uploadfiles1,function(file){
     fd.append('file[]',file);
   });
    angular.forEach($scope.uploadfiles2,function(file){
     fd.append('file[]',file);
   });
    angular.forEach($scope.uploadfiles3,function(file){
     fd.append('file[]',file);
   });

   $http({
     method: 'post',
     url: '/csr-front/upload',
     data: fd,
     params :{
         user      : $scope.user,
     },
     headers: {'Content-Type': undefined},
   }).then(function successCallback(response) {
     // Store response data
     $scope.response = response.data;
   });
 }

}]);