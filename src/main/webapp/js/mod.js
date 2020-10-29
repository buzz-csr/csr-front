var modModule = angular.module('modModule', []).config(function($locationProvider) {
                                                       $locationProvider.html5Mode(true);
                                                   });

modModule.controller('modCtrl', ['$scope', '$http', '$location', function($scope,$http,$location){
	
	$scope.activities = [];
	$scope.fileEdited;
	$scope.userId;
	$scope.editedCar;
	$scope.collections;
	$scope.collectionsDir = [];
	$scope.selectDir;
	$scope.searchedCar = [];
	$scope.expectedCash;
	$scope.expectedGold;
	$scope.expectedKeyBronze;
	$scope.expectedKeySilver;
	$scope.expectedKeyGold;
    $scope.localSearch;
    $scope.greenFusion = 10;
    $scope.blueFusion = 8;
    $scope.redFusion = 6;
    $scope.onlyRedFusion = 20;
    $scope.eliteGreen   = 10000;
    $scope.eliteBlue    = 10000;
    $scope.eliteRed     = 5000;
    $scope.eliteYellow  = 500;
    $scope.eliteTokens = {
        Green : 0,
        Blue : 0,
        Red : 0,
        Yellow : 0,
    }

    $scope.matchSearch = function(carId){
        return carId != -1 && ($scope.localSearch == undefined || $scope.fileEdited.caow[carId].crdb.toLowerCase().includes($scope.localSearch.toLowerCase()));
    }

	function addActivity(text){
		$scope.activities.push(text);
	}

	var directory = $location.search().temp;
	var user = $location.search().user;
	
	$http({
		method: 'GET',
		url: '/csr-front/GetFile',
		headers : {'Content-type' : 'application/json; charset=UTF-8'},
		params : {
            dir     : directory,
            user    : user,
            type    : 'profile', 
		}
	}).then(function(response){
		$scope.fileEdited = response.data;
		$scope.brand = $scope.fileEdited.brands[0];
		$scope.expectedCash = $scope.fileEdited.casp*0.7;
		$scope.expectedGold = $scope.fileEdited.gosp*0.7;
		$scope.expectedKeyBronze = $scope.fileEdited.gbks*0.7;
		$scope.expectedKeySilver = $scope.fileEdited.gsks*0.7;
		$scope.expectedKeyGold = $scope.fileEdited.ggks*0.7;
		$scope.$apply();
	});	
	
	$http({
	    method: 'GET',
	    url: '/csr-front/GetFile',
	    headers : {'Content-type' : 'application/json; charset=UTF-8'},
	    params : {
	        dir     : directory,
	        user    : user,
	        type    : 'id', 
	    }
	}).then(function(response){
	    $scope.userId = response.data;
	});	

	$scope.fullCar = function(caowEdited){
		$http({
			method: 'POST',
			url: '/csr-front/car',
			headers : {'Content-type' : 'application/json; charset=UTF-8'},
			params :{
			    id      : caowEdited.unid,
			    dir     : directory,
	            user : user,
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
	
	$scope.resetCash = function(){
	    $http({
	        method: 'POST',
	        url: '/csr-front/reset',
	        headers : {'Content-type' : 'application/json; charset=UTF-8'},
	        params :{
	            cash       : $scope.expectedCash,
	            gold       : $scope.expectedGold,
	            dir        : directory,
	            user        : user,
                type       : 'cash',
	        }
	    }).then(function(response){
	        $scope.fileEdited = response.data;
	        $scope.expectedCash = $scope.fileEdited.casp * 0.7;
	        $scope.expectedGold = $scope.fileEdited.gosp * 0.7;
	        $scope.expectedKeyBronze = $scope.fileEdited.gbks;
	        $scope.expectedKeySilver = $scope.fileEdited.gsks;
	        $scope.expectedKeyGold = $scope.fileEdited.ggks;
	    });
	    addActivity("Reset cash + or ");
	}
	
	$scope.resetKeys = function(){
	    $http({
	        method: 'POST',
	        url: '/csr-front/reset',
	        headers : {'Content-type' : 'application/json; charset=UTF-8'},
	        params :{
	            bronze         : $scope.expectedKeyBronze,
	            silver         : $scope.expectedKeySilver,
	            gold           : $scope.expectedKeyGold,
	            dir            : directory,
	            user            : user,
                type           : 'key',
	        }
	    }).then(function(response){
	        $scope.fileEdited = response.data;
	        $scope.expectedCash = $scope.fileEdited.casp * 0.7;
	        $scope.expectedGold = $scope.fileEdited.gosp * 0.7;
	    });
	    addActivity("Reset clés ");
	}

	$scope.replace = function(path,car){
	    if($scope.editedCar == undefined){
	        $http({
                method: 'POST',
                url: '/csr-front/car',
                headers : {'Content-type' : 'application/json; charset=UTF-8'},
                params :{
                    path      : path+"/"+car,
                    dir     : directory,
                    user : user,
                    action  : 'add',
                }
            }).then(function(response){
                $scope.fileEdited = response.data;

                addActivity("Ajout " + $scope.fileEdited.caow[$scope.fileEdited.caow.length-1].crdb);
            });
	    }else{
	        $http({
                method: 'POST',
                url: '/csr-front/car',
                headers : {'Content-type' : 'application/json; charset=UTF-8'},
                params :{
                    id      : $scope.editedCar.unid,
                    path      : path+"/"+car,
                    dir     : directory,
                    user : user,
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

    $scope.downloadFile = function(){
        $http({
            method: 'GET',
            url: '/csr-front/pack',
            responseType: 'arraybuffer',
            params : {
                  dir : directory,
                  user : user,
            }
        }).then(function(data){
            var blob = new Blob([data.data], { type: 'application/octet-stream' });
            var downloadLink = document.createElement('a');
            downloadLink.setAttribute('download', 'files.zip');
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
        if($scope.searchCar != undefined && $scope.searchCar.length >= 3){
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
    }
    
    $scope.addCar = function(){
        $scope.editedCar = undefined;
    }
    
    $scope.elite = function(caowEdited){
        $http({
            method: 'POST',
            url: '/csr-front/car',
            headers : {'Content-type' : 'application/json; charset=UTF-8'},
            params :{
                id      : caowEdited.unid,
                dir     : directory,
                user : user,
                action  : 'elite',
            }
        }).then(function(response){
            caowEdited = response.data;

            $scope.fileEdited.caow.forEach(function(car,index){
                if(car.unid == caowEdited.unid){
                    $scope.fileEdited.caow[index] = caowEdited;
                }
            });

            addActivity("Elite " + caowEdited.crdb);
        });
    }

    $scope.fuel = function(){
        $http({
            method: 'POST',
            url: '/csr-front/gift',
            headers : {'Content-type' : 'application/json; charset=UTF-8'},
            params :{
                dir     : directory,
                user    : user,
                action  : 'fuel',
            }
        }).then(function(response){
            addActivity("Ajout Essence");
        });
    }

    $scope.fusionsThree = function(){
        fusions($scope.greenFusion, $scope.blueFusion, $scope.redFusion);
    }

    $scope.fusionsRed = function(){
        fusions(null, null, $scope.onlyRedFusion);
    }

    function fusions(green, blue, red){
        $http({
            method: 'POST',
            url: '/csr-front/gift',
            headers : {'Content-type' : 'application/json; charset=UTF-8'},
            params :{
                dir     : directory,
                user    : user,
                action  : 'fusions',
                green   : green,
                blue    : blue,
                red     : red,
                brand   : $scope.brand,
            }
        }).then(function(response){
            addActivity("Ajout Fusions " + green + " - " + blue + " - " + red);
        });
    }

    $scope.eliteTokensGift = function(){
        $http({
            method: 'POST',
            url: '/csr-front/gift',
            headers : {'Content-type' : 'application/json; charset=UTF-8'},
            params :{
                dir     : directory,
                user    : user,
                action  : 'elite',
                green   : $scope.eliteGreen,
                blue    : $scope.eliteBlue,
                red     : $scope.eliteRed,
                yellow  : $scope.eliteYellow,
            }
        }).then(function(response){
            addActivity("Ajout Composants élite " + $scope.eliteGreen + " - " + $scope.eliteBlue + " - " + $scope.eliteRed  + " - " + $scope.eliteYellow);
        });
    }

	$scope.eliteTokensReset = function(){
	    $http({
	        method: 'POST',
	        url: '/csr-front/reset',
	        headers : {'Content-type' : 'application/json; charset=UTF-8'},
	        params : {
	            dir         : directory,
	            user        : user,
	            type        : 'elite',
                tokens      : {
                                Green   : $scope.fileEdited.afms.Green - $scope.eliteTokens.Green,
                                Blue    : $scope.fileEdited.afms.Blue - $scope.eliteTokens.Blue,
                                Red     : $scope.fileEdited.afms.Red - $scope.eliteTokens.Red,
                                Yellow  : $scope.fileEdited.afms.Yellow - $scope.eliteTokens.Yellow,
                            },
	        }
	    }).then(function(response){
	        $scope.fileEdited = response.data;
	    addActivity("Reset Composants elites");
	    });
	}
}]);