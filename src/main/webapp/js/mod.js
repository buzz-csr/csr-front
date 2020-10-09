var modModule = angular.module('modModule', []).config(function($locationProvider) {
                                                       $locationProvider.html5Mode(true);
                                                   });;

modModule.controller('modCtrl', ['$scope', '$http', '$location', function($scope,$http,$location){
	
	$scope.activities = [];
	$scope.collection;
	$scope.collections;
	$scope.fileMax;
	$scope.fileEdited;
	$scope.scbFile;
	$scope.editedCar;
	$scope.hasKeysReset = false;
	$scope.hasCashGoldReset = false;
	$scope.hasEliteReset = false;
	$scope.hasEliteGreenReset = false;
	$scope.hasEliteRedReset = false;
	$scope.hasEliteBlueReset = false;
	$scope.hasEliteYellowReset = false;
	$scope.legendFirst = false;
	$scope.errors = [];
	$scope.collections;
	$scope.collectionsDir = [];
	$scope.selectDir;
    $scope.selectCars = [];

	$scope.brands = [
		{name: "id_abarth"},
		{name: "id_alfaromeo"},
		{name: "id_alpine"},
		{name: "id_amc"},
		{name: "id_apollo"},
		{name: "id_astonmartin"},
		{name: "id_atsautomobili"},
		{name: "id_audi"},
		{name: "id_bac"},
		{name: "id_bentley"},
		{name: "id_bmw"},
		{name: "id_brabham"},
		{name: "id_bugatti"},
		{name: "id_cadillac"},
		{name: "id_chevrolet"},
		{name: "id_dodge"},
		{name: "id_donkervoort"},
		{name: "id_ferrari"},
		{name: "id_fnf"},
		{name: "id_ford"},
		{name: "id_ginetta"},
		{name: "id_gtamotor"},
		{name: "id_hennessey"},
		{name: "id_honda"},
		{name: "id_hyundai"},
		{name: "id_infiniti"},
		{name: "id_jaguar"},
		{name: "id_jeep"},
		{name: "id_kia"},
		{name: "id_koenigsegg"},
		{name: "id_ktm"},
		{name: "id_lamborghini"},
		{name: "id_landrover"},
		{name: "id_localmotor"},
		{name: "id_lotus"},
		{name: "id_maserati"},
		{name: "id_mazda"},
		{name: "id_mazzanti"},
		{name: "id_mclaren"},
		{name: "id_mercedes"},
		{name: "id_mini"},
		{name: "id_mitsubishi"},
		{name: "id_nissan"},
		{name: "id_noble"},
		{name: "id_pagani"},
		{name: "id_plymouth"},
		{name: "id_pontiac"},
		{name: "id_porsche"},
		{name: "id_saleen"},
		{name: "id_scg"},
		{name: "id_shelby"},
		{name: "id_spyker"},
		{name: "id_ssc"},
		{name: "id_subaru"},
		{name: "id_toyota"},
		{name: "id_tvr"},
		{name: "id_ultima"},
		{name: "id_volkswagen"},
		{name: "id_vuhl"},
		{name: "id_wmotors"},
		{name: "id_zenvo"}]
	$scope.selectedBrand = $scope.brands[0];
	$scope.selectedBrandRed = $scope.brands[0];
	$scope.legendsCars = [
		{ name: "Ford_Mustang302_1970" },
		{ name: "AstonMartin_DB5Classic_1964" },
		{ name: "Ferrari_250GTOClassic_1962" },
		{ name: "MercedesBenz_300SLClassic_1954" },
		{ name: "Chevrolet_CorvetteZR1Classic_1970" },
		{ name: "Shelby_Cobra427SCClassic_1965" },
		{ name: "Pontiac_GTOTheJudgeClassic_1969" },
		{ name: "Honda_NSXRClassic_1992" },
		{ name: "Plymouth_HemiCudaClassic_1971" },
		{ name: "Ford_GT40MkII_1966" },
		{ name: "Lamborghini_CountachClassic_1988" },
		{ name: "Bugatti_EB110SSClassic_1992" },
		{ name: "Porsche_CarreraGTClassic_2003" },
		{ name: "Jaguar_XJ220Classic_1993" },
		{ name: "Saleen_S7Classic_2004" }
	];
	$scope.legendCarSelected = $scope.legendsCars[0];
	
	function addActivity(text){
		$scope.activities.push(text);
	}

	var directory = $location.search().temp;
	$http({
		method: 'GET',
		url: 'http://localhost:8080/csr-front/GetFile',
		headers : {'Content-type' : 'application/json; charset=UTF-8'},
		params : {
            file: 'nsb',
            dir : directory,
		}
	}).then(function(response){
		$scope.fileEdited = response.data;
	});	
	
	$http({
		method: 'GET',
		url: 'http://localhost:8080/csr-front/GetFile',
		headers : {'Content-type' : 'application/json; charset=UTF-8'},
        params : {
              file: 'scb',
              dir : directory,
        }
	}).then(function(response){
		$scope.scbFile = response.data;
	});
	
	$scope.fullCar = function(caowEdited){
		$http({
			method: 'POST',
			url: 'http://localhost:8080/csr-front/car',
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
	
	function updateStages(caowEdited){
		angular.forEach($scope.fileMax.caow, function(caowMax){
			if(caowMax.crdb === caowEdited.crdb){
				caowEdited.upst = caowMax.upst;
			}
		});
	}
	
	$scope.replace = function(path,car){
		$http({
                method: 'POST',
                url: 'http://localhost:8080/csr-front/car',
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

	$scope.resetCashGold = function(){
		addActivity("Reset cash et or" );
	}
	
	$scope.resetKeys = function(){
		addActivity("Reset clés" );
	}
	
	function addEliteGift(partGrade, amount){
		$scope.fileEdited.picl.push("eRewards");
		$scope.fileEdited.playinbitms.push({
		  "reason": "TEXT_TAG_CS_COMPENSATION",
		  "ttl": 0,
		  "id": "1592077438_24_0",
		  "rank": 0,
		  "CSR2ApplyableReward": {
			"rewardType": 24,
			"name": "",
			"partType": 7,
			"partGrade": partGrade,
			"gachaConfig": -1,
			"amount": amount
		  }
		});
	}
	
	$scope.resetElites = function(){
		$scope.hasEliteReset = true;
		addEliteGift(1, 10000);
		addEliteGift(2, 10000);
		addEliteGift(3, 5000);
		addEliteGift(4, 500);
		
		addActivity("Reset composants elites" );
	}
	
	$scope.resetGreenElites = function(){
		$scope.hasEliteGreenReset = true;
		addEliteGift(1, 10000);
		
		addActivity("Reset composant elite vert" );
	}	
	$scope.resetBlueElites = function(){
		$scope.hasEliteBlueReset = true;
		addEliteGift(2, 10000);
		addActivity("Reset composant elite bleu" );
	}	
	$scope.resetRedElites = function(){
		$scope.hasEliteRedReset = true;
		addEliteGift(3, 5000);
		addActivity("Reset composant elite rouge" );
	}
	$scope.resetYellowElites = function(){
		$scope.hasEliteYellowReset = true;
		addEliteGift(4, 500);
		addActivity("Reset composant elite jaune" );
	}
	
	$scope.elite = function(car){
		car.elcl = 2;
		addActivity("Elite sur " + car.crdb);
	}
	
	$scope.allColorsAllBrands = function(){
		var index = 0;
		console.log("Nombre de marque : " + $scope.brands.length);
		
		angular.forEach($scope.brands, function(value){
			
			for(var i=0; i<=6; i++){
				for(partGrade=1; partGrade<=3; partGrade++){
					$scope.fileEdited.picl.push("eRewards");
					$scope.fileEdited.playinbitms.push({
					  "reason": "TEXT_TAG_CS_COMPENSATION",
					  "ttl": 0,
					  "id": "" + index,
					  "rank": 0,
					  "CSR2ApplyableReward": {
						"rewardType": 13,
						"name": value.name,
						"partType": i,
						"partGrade": partGrade,
						"gachaConfig": -1,
						"amount": getFusionsNb(partGrade)
					  }
					});	
					index++;
				}
			}
		});		
		addActivity("Fusions 3 couleurs toutes marques");
	}	
	
	$scope.specialChris = function(){
		var counter = [0, 0, 0, 0, 0, 0, 0];
		var index = 0;
		console.log("Nombre de marque : " + $scope.brands.length);
		
		angular.forEach($scope.brands, function(value){
			
			for(var i=0; i<=6; i++){
				$scope.fileEdited.picl.push("eRewards");
				$scope.fileEdited.playinbitms.push({
				  "reason": "TEXT_TAG_CS_COMPENSATION",
				  "ttl": 0,
				  "id": "" + index,
				  "rank": 0,
				  "CSR2ApplyableReward": {
					"rewardType": 13,
					"name": value.name,
					"partType": i,
					"partGrade": 3,
					"gachaConfig": -1,
					"amount": 20
				  }
				});	
				index++;
				counter[i]++;
			}
		});
		console.log("Nombre de cadeau : " + $scope.fileEdited.playinbitms.length);
		console.log(counter);
		
		addActivity("Fusions rouges toutes marques");
	}
	
	$scope.addFusionsGift = function(){
		var partType;
		var partGrade;
		
		for(partType=0; partType <=6; partType++){
			for(partGrade=1; partGrade<=3; partGrade++){
				$scope.fileEdited.picl.push("eRewards");
				$scope.fileEdited.playinbitms.push({
				  "reason": "TEXT_TAG_CS_COMPENSATION",
				  "ttl": 0,
				  "id": "1111_" + partType,
				  "rank": 0,
				  "CSR2ApplyableReward": {
					"rewardType": 13,
					"name": $scope.selectedBrand.name,
					"partType": partType,
					"partGrade": partGrade,
					"gachaConfig": -1,
					"amount": getFusionsNb(partGrade)
				  }
				});				
			}
		}
		
		addActivity("Fusions 3 couleurs " + $scope.selectedBrand.name);
	}	
	
	$scope.addRedFusionsGift = function(){
		var partType;		
		for(partType=0; partType <=6; partType++){
			$scope.fileEdited.picl.push("eRewards");
			$scope.fileEdited.playinbitms.push({
			  "reason": "TEXT_TAG_CS_COMPENSATION",
			  "ttl": 0,
			  "id": "1111_" + partType,
			  "rank": 0,
			  "CSR2ApplyableReward": {
				"rewardType": 13,
				"name": $scope.selectedBrandRed.name,
				"partType": partType,
				"partGrade": 3,
				"gachaConfig": -1,
				"amount": 20
			  }
			});				
		}
		
		addActivity("Fusions rouge " + $scope.selectedBrandRed.name);
	}
	
	function getFusionsNb(grade){
		if(grade == 1){
			return 10;
		}
		if(grade == 2){
			return 8;
		}
		if(grade == 3){
			return 6;
		}
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
	
	$scope.getFusionsCarImg = function(id){
		var img = "";
		angular.forEach($scope.collection, function(value){
			if(value.code == id){
				img = value.img;
			}
		});
		return "https://raw.githubusercontent.com/wear87/CSR2-Racing-Collection/master" + encodeURIComponent(img).replaceAll("'","%27").replace('.png','_fusions.png');
	}
	
	$scope.addFuel = function(){
		$scope.fileEdited.picl.push("eRewards");
		$scope.fileEdited.playinbitms.push({
		  "reason": "TEXT_TAG_CS_COMPENSATION",
		  "ttl": 0,
		  "id": "0_0",
		  "rank": 0,
		  "CSR2ApplyableReward": {
			"rewardType": 10,
			"name": "",
			"partType": 7,
			"partGrade": 0,
			"gachaConfig": -1,
			"amount": 2000
		  }
		});				
		addActivity("Essence");
	}	
	
	$scope.addRestoTokens = function(){
		$scope.fileEdited.picl.push("eRewards");
		$scope.fileEdited.playinbitms.push({
		  "reason": "TEXT_TAG_CS_COMPENSATION",
		  "ttl": 0,
		  "id": "0_1",
		  "rank": 0,
		  "CSR2ApplyableReward": {
			"rewardType": 22,
			"name": $scope.legendCarSelected.name,
			"partType": 7,
			"partGrade": 0,
			"gachaConfig": -1,
			"amount": 20000
		  }
		});				
		addActivity("Jetons restauration " + $scope.legendCarSelected.name);
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
		url: 'http://localhost:8003/nsb.full.txt',
		headers : {'Content-type' : 'application/json; charset=UTF-8'}
	}).then(function(response){
		$scope.fileMax = response.data;
	});
	
	$http({
		method: 'GET',
		url: 'http://localhost:8003/server/cars_mapping.json',
		headers : {'Content-type' : 'application/json; charset=UTF-8'}
	}).then(function(response){
		$scope.collection = response.data.collection;
	});

	$http({
		method: 'GET',
		url: 'http://localhost:8080/csr-front/collections.json',
		headers : {'Content-type' : 'application/json; charset=UTF-8'}
	}).then(function(response){
	    $scope.collections = response.data;
	    $scope.collectionsDir.push(response.data.content);
	});
	
	$scope.sortByBrand = function(){
		$scope.fileEdited.cgpi = $scope.fileEdited.cgpi.sort(compare);
	}
		
	$scope.sortByTier = function(){
		$scope.fileEdited.cgpi = $scope.fileEdited.cgpi.sort(compareTier);
	}
	
	function compareTier(left, right){
		var result= 0;
		
		if(left == -1){
			result = 1;
		}else if(right == -1){
			result = -1;
		}else{
			var carLeft = $scope.fileEdited.caow[left];
			var carRight = $scope.fileEdited.caow[right];
			
			if(carLeft.cmlv == carRight.cmlv || !$scope.legendFirst){
				if(carLeft.ctie == carRight.ctie){ // meme tier
					result = carLeft.tthm < carRight.tthm;
				}else{
					result = carLeft.ctie > carRight.ctie;
				}
			}else if(carLeft.cmlv < carRight.cmlv){
				result = 1;
			}else if(carLeft.cmlv > carRight.cmlv){
				result = -1;
			}
		}
		return result;
	}
	
	function compare(left, right){
		var result= 0;
		
		if(left == -1){
			result = 1;
		}else if(right == -1){
			result = -1;
		}else{
			var carLeft = $scope.fileEdited.caow[left];
			var carRight = $scope.fileEdited.caow[right];
			
			var brandLeft = carLeft.crdb.split('_')[0];
			var brandRight = carRight.crdb.split('_')[0];
			
			if(carLeft.cmlv == carRight.cmlv || !$scope.legendFirst){
				if($scope.legendFirst && isLegend(carLeft) && isLegend(carRight) || !$scope.legendFirst){
					if(brandLeft == brandRight){ // meme marque
						
						if(carLeft.ctie == carRight.ctie){ // meme tier
							
							result = carLeft.tthm < carRight.tthm;
						}else{
							result = carLeft.ctie > carRight.ctie;
						}
					}else{
						result = brandLeft > brandRight;
					}
				}else if($scope.legendFirst && isLegend(carLeft) && !isLegend(carRight)){
					result = -1;
				}else if($scope.legendFirst && !isLegend(carLeft) && isLegend(carRight)){
					result = 1;
				}else{
					if(brandLeft == brandRight){ // meme marque
						
						if(carLeft.ctie == carRight.ctie){ // meme tier
							
							result = carLeft.tthm < carRight.tthm;
						}else{
							result = carLeft.ctie > carRight.ctie;
						}
					}else{
						result = brandLeft > brandRight;
					}

				}
			}else if(carLeft.cmlv < carRight.cmlv){
				result = 1;
			}else if(carLeft.cmlv > carRight.cmlv){
				result = -1;
			}
		}
		return result;
	}
	
	function isLegend(car){
		var result = false;
		$scope.fileMax.ccac['IconicCars_V1'].forEach(function(value){
			result = result || car.crdb == value;
		});
		$scope.fileMax.ccac['IconicCars_V2'].forEach(function(value){
			result = result || car.crdb == value;
		});
		$scope.fileMax.ccac['Sportscar_V1'].forEach(function(value){
			result = result || car.crdb == value;
		});
		$scope.fileMax.ccac['Americana_V1'].forEach(function(value){
			result = result || car.crdb == value;
		});
		
		return result;
	}
	
	$scope.unban = function(){
		angular.forEach($scope.fileEdited.caow, function(car){
			car.cmlv = 0;
			
			angular.forEach($scope.fileMax.caow, function(caowMax){
				if(caowMax.crdb === car.crdb){
					angular.forEach(caowMax.upst, function(upsetMax,indexUpset){
						angular.forEach(upsetMax.lvls, function(lvlsMax, indexLvls){
							if(lvlsMax.fsg[0] == 0 && car.upst[indexUpset].lvls[indexLvls].fsg[0] != 0){
								car.upst[indexUpset].lvls[indexLvls].fsg[0] = 0;
							}
							if(lvlsMax.fsg[1] == 0 && car.upst[indexUpset].lvls[indexLvls].fsg[1] != 0){
								car.upst[indexUpset].lvls[indexLvls].fsg[1] = 0;
							}
							if(lvlsMax.fsg[2] == 0 && car.upst[indexUpset].lvls[indexLvls].fsg[2] != 0){
								car.upst[indexUpset].lvls[indexLvls].fsg[2] = 0;
							}
							if(lvlsMax.fsg[3] == 0 && car.upst[indexUpset].lvls[indexLvls].fsg[3] != 0){
								car.upst[indexUpset].lvls[indexLvls].fsg[3] = 0;
							}
							if(lvlsMax.fsg[4] == 0 && car.upst[indexUpset].lvls[indexLvls].fsg[4] != 0){
								car.upst[indexUpset].lvls[indexLvls].fsg[4] = 0;
							}
						});
					});
				}
			});
		});
		
		$scope.fileEdited.tcbl = 0;
		$scope.fileEdited.rcbp = false;
				
		$scope.fileEdited.afme.Green = 15;
		$scope.fileEdited.afme.Blue = 0;
		$scope.fileEdited.afme.Red = 0;
		$scope.fileEdited.afme.Yellow = 0;
		$scope.fileEdited.afms.Green = 15;
		$scope.fileEdited.afms.Blue = 0;
		$scope.fileEdited.afms.Red = 0;
		$scope.fileEdited.afms.Yellow = 0;
		
		$scope.scbFile.AMPartGreenEarned = 15;
		$scope.scbFile.AMPartBlueEarned = 0;
		$scope.scbFile.AMPartRedEarned = 0;
		$scope.scbFile.AMPartYellowEarned = 0;
		$scope.scbFile.AMPartGreenSpent = 15;
		$scope.scbFile.AMPartBlueSpent = 0;
		$scope.scbFile.AMPartRedSpent = 0;
		$scope.scbFile.AMPartYellowSpent = 0;
		
		addActivity("Clone reset");
	}
	
	$scope.checkCars = function(){
		$scope.fileEdited.caow.forEach(function(car){
			
			if(car.csrc == "IAP" || car.csrc == "IAPSpecial"){
				$scope.fileMax.cid2.forEach(function(cid2){
					if(cid2.id == car.crdb){
						var fund = false;
						$scope.fileEdited.cid2.forEach(function(editedCid2){
							fund = fund || editedCid2.id == car.crdb;
						});
						if(!fund){
							$scope.errors.push({
								"car": car.crdb,
								"reason": "cid2"
							});
						}
					}
				});
			}
			$scope.fileMax.cidc.forEach(function(cidc){
				if(cidc == car.crdb){
					var fund = false;
					$scope.fileEdited.cidc.forEach(function(editedCidc){
						fund = fund || editedCidc == car.crdb;
					});
					if(!fund){
						$scope.errors.push({
							"car": car.crdb,
							"reason": "cidc"
						});
					}
				}
			});	

			$scope.fileMax.caow.forEach(function(carMax){
					if(carMax.crdb == car.crdb){
						angular.forEach(carMax, function(value, key){
							if(car[key] == undefined && key != "$$hashKey"){
								console.log(car.crdb + " -> " + key);
							} else if (angular.isArray(car[key])){
								
							}
							
						});
					}
			});
		});
	}
		
	$scope.correctCar = function(error){
		if(error.reason == 'cid2'){
			$scope.fileEdited.cid2.push({
				"id": error.car,
				"ct": 1
			});
		}else if(error.reason == 'cidc'){
			$scope.fileEdited.cidc.push(error.car);
		}
	}
	
	$scope.isCorrect = function(error){
		var result = true;
		if(error.reason == 'cid2'){
			$scope.fileEdited.cid2.forEach(function(value){
				result = result & (value.id != error.car);
			});
		}else if(error.reason == 'cidc'){
			$scope.fileEdited.cidc.forEach(function(value){
				result = result & (value != error.car);
			});
		}
		return result;
	}

	$scope.saveNsb = function(){
		$scope.save($scope.fileEdited, "nsb.json");
	}
	
	$scope.saveScb = function(){
		$scope.save($scope.scbFile, "scb.json");
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
            if(dir.cars != undefined && dir.cars.length > 0){
                $scope.selectCars = dir.cars;
            }
        }
    }

    $scope.closeCollection = function(){
        $scope.collectionsDir = [];
        $scope.collectionsDir.push($scope.collections.content);
        $scope.selectDir = undefined;
    }
}]);