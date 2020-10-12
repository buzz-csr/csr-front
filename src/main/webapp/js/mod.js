var modModule = angular.module('modModule', []).config(function($locationProvider) {
                                                       $locationProvider.html5Mode(true);
                                                   });;

modModule.controller('modCtrl', ['$scope', '$http', '$location', function($scope,$http,$location){
	
	$scope.activities = [];
	$scope.fileEdited;
	$scope.editedCar;
	$scope.collections;
	$scope.collectionsDir = [];
	$scope.selectDir;
	$scope.resultFiles = [];
	$scope.searchedCar = [];

	function addActivity(text){
		$scope.activities.push(text);
	}

	var directory = $location.search().temp;
	$http({
		method: 'GET',
		url: '/csr-front/GetFile',
		headers : {'Content-type' : 'application/json; charset=UTF-8'},
		params : {
            dir : directory,
		}
	}).then(function(response){
		$scope.fileEdited = response.data;
	});	

	$scope.fullCar = function(caowEdited){
		$http({
			method: 'POST',
			url: '/csr-front/car',
			headers : {'Content-type' : 'application/json; charset=UTF-8'},
			params :{
			    id      : caowEdited.unid,
			    dir     : directory,
			    action  : 'full',
			}
		}).then(function(response){
		    caowEdited = response.data;

            $scope.fileEdited.caow.forEach(function(car,index){
                if(car.unid == caowEdited.unid){
                    $scope.fileEdited.caow[index] = caowEdited;
                }
            });
		});
		addActivity("Remplissage " + caowEdited.crdb);
	}

	$scope.replace = function(path,car){
		$http({
                method: 'POST',
                url: '/csr-front/car',
                headers : {'Content-type' : 'application/json; charset=UTF-8'},
                params :{
                    id      : $scope.editedCar.unid,
                    path      : path+"/"+car,
                    dir     : directory,
                    action  : 'replace',
                }
            }).then(function(response){
                caowEdited = response.data;

                $scope.fileEdited.caow.forEach(function(car,index){
                    if(car.unid == caowEdited.unid){
                        $scope.fileEdited.caow[index] = caowEdited;
                    }
                });

                addActivity("Ajout " + caowEdited.crdb);
            });
	}
	
	$scope.saveEditedCar = function(editedCar){
		$scope.editedCar = editedCar;
		$(function () {
			$('[data-toggle="tooltip"]').tooltip()
		})
	}

	$scope.getCarName = function(id){
		var name = "";
		angular.forEach($scope.collection, function(value){
			if(value.code == id){
				name = value.name;
			}
		});
		return name;
	}
	
	$scope.getCarImg = function(path, id){
		return "https://raw.githubusercontent.com/wear87/CSR2-Racing-Collection/master" + encodeURIComponent(path + "/" + id + ".png").replaceAll("'","%27");
	}

	$scope.isTop = function(index){
		return index%6 == 0;
	}
	
	$scope.getGarageNumber = function(index){
		return Math.floor(index/6+1)
	}
	
	$scope.getGarageClass = function(index){
		var style = "garage";
		if(index%6 == 0){
			style += " garage-top";
		}else if(index%6 == 5){
			style += " garage-bottom";
		}else{
			style += " garage-middle";
		}
		return style;
	}
	
	$http({
		method: 'GET',
		url: '/csr-front/collections.json',
		headers : {'Content-type' : 'application/json; charset=UTF-8'}
	}).then(function(response){
	    $scope.collections = response.data;
	    $scope.collectionsDir.push(response.data.content);
	});

	$scope.saveNsb = function(){
		$scope.save($scope.fileEdited, "nsb.json");
	}

	$scope.save = function(content, filename){
		var blob = new Blob([angular.toJson(content, true)], { type: "text/plain;charset=utf-8" });
		var downloadLink = document.createElement('a');
		downloadLink.setAttribute('download', filename);
		downloadLink.setAttribute('href', window.URL.createObjectURL(blob));
		downloadLink.click();
	}

    $scope.addSelect = function(dir){
        if(dir.content != undefined && dir.content.length > 0){
            $scope.collectionsDir.push(dir.content);
        }
    }

    $scope.closeCollection = function(){
        $scope.collectionsDir = [];
        $scope.collectionsDir.push($scope.collections.content);
        $scope.selectDir = undefined;
    }

    $scope.pack = function(){
        $http({
            method: 'POST',
            url: '/csr-front/pack',
            params : {
                  dir : directory,
            }
        }).then(function(data){
            $scope.resultFiles = data.data;
        });
    }

    $scope.downloadFile = function(name){
        $http({
            method: 'GET',
            url: '/csr-front/pack',
            responseType: 'arraybuffer',
            params : {
                  dir : directory,
                  type: name,
            }
        }).then(function(data){
            var blob = new Blob([data], { type: 'application/gzip' });
            var downloadLink = document.createElement('a');
            downloadLink.setAttribute('download', name);
            downloadLink.setAttribute('href', window.URL.createObjectURL(blob));
            downloadLink.click();
        });
    }
    
    $scope.search = function(){
        $scope.searchedCar = [];
        if($scope.searchCar != undefined && $scope.searchCar.length >= 3){
            searchCar($scope.collections);
        }
    }
    
    var searchCar = function(content){
        if(content.directory.toLowerCase().includes($scope.searchCar.toLowerCase()) && 
                content.cars && content.cars.length > 0){
            angular.forEach(content.cars, function(car){
                $scope.searchedCar.push({
                    path   : content.path,
                    carName : car,
                })
            })
        }
        if(content.cars && content.cars.length > 0){
            angular.forEach(content.cars, function(car){
                if(car.toLowerCase().includes($scope.searchCar.toLowerCase())){
                    $scope.searchedCar.push({
                        path   : content.path,
                        carName : car,
                    })
                }
            })
        }

        angular.forEach(content.content, function(actual){
            searchCar(actual)
        })
    }
    
}]);