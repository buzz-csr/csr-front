var modModule = angular.module('modModule', []).config(function($locationProvider) {
    $locationProvider.html5Mode(true);
});

modModule.controller('modCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {

    $scope.loading = "load";
    $scope.activities = [];
    $scope.fileEdited;
    $scope.userId;
    $scope.editedCar;
    $scope.carsId = [];
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
    $scope.eliteGreen = 15000;
    $scope.eliteBlue = 10000;
    $scope.eliteRed = 5000;
    $scope.eliteYellow = 500;
    $scope.fuelNb = 2000;
    $scope.eliteTokens = {
        Green: 0,
        Blue: 0,
        Red: 0,
        Yellow: 0,
    }
    $scope.eliteTokensMax = {
        Green: 0,
        Blue: 0,
        Red: 0,
        Yellow: 0,
    }
    $scope.eliteTokensSpent = {
        Green: 0,
        Blue: 0,
        Red: 0,
        Yellow: 0,
    }    
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
    $scope.legendsCarSelected;
    $scope.legendsRestorationQty = 10000;
    $scope.stage6car;
    $scope.hasLicenseFree;
    $scope.eliteFirst = true;
    $scope.token;
    
    var userId = $location.search().userId;
    
    $scope.matchSearch = function(carId) {
        return carId != -1 && ($scope.localSearch == undefined || $scope.carNames[$scope.fileEdited.caow[carId].crdb].toLowerCase().includes($scope.localSearch.toLowerCase()));
    }

    function addActivity(text) {
        $scope.activities.push(text);
    }

    $scope.errorUrl = function() {
        return '/csr-front/errors.html?token=' + encodeURIComponent($scope.token);
    }

    $http({
        method: 'GET',
        url: '/csr-front/GetFileV2',
        headers: { 'Content-type': 'application/json; charset=UTF-8' },
        params: {
            userId: userId,
            type: 'profile',
        }
    }).then(function(response) {
        $scope.loading = "hidden";
        if(response.data.error){
            $scope.errorMessage = response.data.error;                 
        }else{
            $scope.token = response.data.token;
            $scope.fileEdited = response.data.nsb;
            $scope.brand = $scope.fileEdited.brands[0];
            $scope.expectedCash = $scope.fileEdited.casp * 0.7;
            $scope.expectedGold = $scope.fileEdited.gosp * 0.7;
            $scope.expectedKeyBronze = $scope.fileEdited.gbks * 0.7;
            $scope.expectedKeySilver = $scope.fileEdited.gsks * 0.7;
            $scope.expectedKeyGold = $scope.fileEdited.ggks * 0.7;
            $scope.eliteTokensMax = response.data.nsb.afme;
            $scope.eliteTokensSpent = response.data.nsb.afms;
            $scope.hasLicenseFree = licenseFree();
            
            $http({
                method: 'POST',
                url: '/csr-front/car',
                headers: { 'Content-type': 'application/json; charset=UTF-8' },
                params: {
                    userId: userId,
                    token   : $scope.token,
                    action: 'list',
                }
            }).then(function(response) {
                $scope.carsId = response.data;
                $scope.searchedIdCar = response.data;
            });
        }
    });
    
    $scope.updateProfile = function() {
       $scope.updateStatus = "";
       $scope.errorMessage = "";
       $scope.loading = true;
       
       $http({
            method: 'POST',
            url: '/csr-front/GetFileV2',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                userId: userId,
                token   : $scope.token,
            }
        }).then(function(response) {
            if(response.data.error){
                $scope.errorMessage = response.data.error;                 
            }else{
                $scope.updateStatus = "ok";
            }
            $scope.loading = false;
        });
    }
    
    $scope.addCarId = function(carId){
        $scope.loading = "load";
       $http({
            method: 'POST',
            url: '/csr-front/car',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                token   : $scope.token,
                action  : 'addId',
                id      : carId,
            }
        }).then(function(response) {
            $scope.fileEdited = response.data;
            addActivity("Ajout " + carId);
            $scope.loading = "hidden";
        });

    }
    
    $scope.fullCar = function(caowEdited) {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/car',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                id: caowEdited.unid,
                token: $scope.token,
                action: 'full',
            }
        }).then(function(response) {
            caowEdited = response.data;

            $scope.fileEdited.caow.forEach(function(car, index) {
                if (car.unid == caowEdited.unid) {
                    $scope.fileEdited.caow[index] = caowEdited;
                }
            });
            addActivity("Remplissage " + caowEdited.crdb);
            $scope.loading = "hidden";
        });
    }

    $scope.resetCash = function() {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/reset',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                cash: $scope.expectedCash,
                gold: $scope.expectedGold,
                token: $scope.token,
                type: 'cash',
            }
        }).then(function(response) {
            $scope.fileEdited = response.data;
            $scope.expectedCash = $scope.fileEdited.casp * 0.7;
            $scope.expectedGold = $scope.fileEdited.gosp * 0.7;
            $scope.expectedKeyBronze = $scope.fileEdited.gbks;
            $scope.expectedKeySilver = $scope.fileEdited.gsks;
            $scope.expectedKeyGold = $scope.fileEdited.ggks;
            addActivity("Reset cash + or ");
            $scope.loading = "hidden";
        });
    }

    $scope.resetKeys = function() {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/reset',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                bronze: $scope.expectedKeyBronze,
                silver: $scope.expectedKeySilver,
                gold: $scope.expectedKeyGold,
                token: $scope.token,
                type: 'key',
            }
        }).then(function(response) {
            $scope.fileEdited = response.data;
            $scope.expectedCash = $scope.fileEdited.casp * 0.7;
            $scope.expectedGold = $scope.fileEdited.gosp * 0.7;
            addActivity("Reset clés ");
            $scope.loading = "hidden";
        });
    }

    $scope.replace = function(path, car) {
        $scope.loading = "load";
        if ($scope.editedCar == undefined) {
            $http({
                method: 'POST',
                url: '/csr-front/car',
                headers: { 'Content-type': 'application/json; charset=UTF-8' },
                params: {
                    path: path + "/" + car,
                    token: $scope.token,
                    action: 'add',
                }
            }).then(function(response) {
                $scope.fileEdited = response.data;

                addActivity("Ajout " + $scope.fileEdited.caow[$scope.fileEdited.caow.length - 1].crdb);
                $scope.loading = "hidden";
            });
        } else {
            $http({
                method: 'POST',
                url: '/csr-front/car',
                headers: { 'Content-type': 'application/json; charset=UTF-8' },
                params: {
                    id: $scope.editedCar.unid,
                    path: path + "/" + car,
                    token: $scope.token,
                                        action: 'replace',
                }
            }).then(function(response) {
                caowEdited = response.data;

                $scope.fileEdited.caow.forEach(function(car, index) {
                    if (car.unid == caowEdited.unid) {
                        $scope.fileEdited.caow[index] = caowEdited;
                    }
                });

                addActivity("Ajout " + caowEdited.crdb);
                $scope.loading = "hidden";
            });
        }
    }

    $scope.saveEditedCar = function(editedCar) {
        $scope.editedCar = editedCar;
        $(function() {
            $('[data-toggle="tooltip"]').tooltip()
        })
    }

    $scope.getCarName = function(id) {
        var name = "";
        angular.forEach($scope.collection, function(value) {
            if (value.code == id) {
                name = value.name;
            }
        });
        return name;
    }

    $scope.getCarImg = function(path, id) {
        return "https://raw.githubusercontent.com/wear87/CSR2-Racing-Collection/master" + encodeURIComponent(path + "/" + id + ".png").replaceAll("'", "%27");
    }

    $scope.isTop = function(index) {
        return index % 6 == 0;
    }

    $scope.getGarageNumber = function(index) {
        return Math.floor(index / 6 + 1)
    }

    $scope.getGarageClass = function(index) {
        var style = "garage";
        if (index % 6 == 0) {
            style += " garage-top";
        } else if (index % 6 == 5) {
            style += " garage-bottom";
        } else {
            style += " garage-middle";
        }
        return style;
    }

    $http({
        method: 'GET',
        url: '/csr-front/carNames.json',
        headers: { 'Content-type': 'application/json; charset=UTF-8' }
    }).then(function(response) {
        $scope.carNames = response.data;
        angular.element(document).ready(function() {
            $('.stage6cars').selectpicker();
        });
    });

    $http({
        method: 'GET',
        url: '/csr-front/collections.json',
        headers: { 'Content-type': 'application/json; charset=UTF-8' }
    }).then(function(response) {
        $scope.collections = response.data;
        $scope.collectionsDir.push(response.data.content);
    });

    $scope.addSelect = function(dir) {
        if (dir.content != undefined && dir.content.length > 0) {
            $scope.collectionsDir.push(dir.content);
        }
    }

    $scope.closeCollection = function() {
        $scope.collectionsDir = [];
        $scope.collectionsDir.push($scope.collections.content);
        $scope.selectDir = undefined;
    }

    $scope.downloadFile = function() {
        $http({
            method: 'GET',
            url: '/csr-front/pack',
            responseType: 'arraybuffer',
            params: {
                token: $scope.token,
            }
        }).then(function(data) {
            var blob = new Blob([data.data], { type: 'application/octet-stream' });
            var downloadLink = document.createElement('a');
            downloadLink.setAttribute('download', 'files.zip');
            downloadLink.setAttribute('href', window.URL.createObjectURL(blob));
            downloadLink.click();
        });
    }
    
    $scope.searchId = function() {
        $scope.searchedIdCar = [];
        if ($scope.searchCarId != undefined && $scope.searchCarId.length >= 3) {
            angular.forEach($scope.carsId, function(car) {
                if (car.toLowerCase().includes($scope.searchCarId.toLowerCase())) {
                    $scope.searchedIdCar.push(car);
                }
            });
        }
    }
    
    $scope.search = function() {
        $scope.searchedCar = [];
        if ($scope.searchCar != undefined && $scope.searchCar.length >= 3) {
            searchCar($scope.collections);
        }
    }

    var searchCar = function(content) {
        if ($scope.searchCar != undefined && $scope.searchCar.length >= 3) {
            if (content.directory.toLowerCase().includes($scope.searchCar.toLowerCase()) &&
                content.cars && content.cars.length > 0) {
                angular.forEach(content.cars, function(car) {
                    $scope.searchedCar.push({
                        path: content.path,
                        carName: car,
                    })
                })
            }
            if (content.cars && content.cars.length > 0) {
                angular.forEach(content.cars, function(car) {
                    if (car.toLowerCase().includes($scope.searchCar.toLowerCase())) {
                        $scope.searchedCar.push({
                            path: content.path,
                            carName: car,
                        })
                    }
                })
            }

            angular.forEach(content.content, function(actual) {
                searchCar(actual)
            })
        }
    }

    $scope.addCar = function() {
        $scope.editedCar = undefined;
    }

    $scope.sort = function(sorting) {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/car',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                token       : $scope.token,
                action      : 'sort',
                sort        : sorting,
                eliteFirst  : $scope.eliteFirst,
            }
        }).then(function(response) {
            $scope.fileEdited = response.data;
            addActivity("Trie du garage");
            $scope.loading = "hidden";
        });
    }

    $scope.elite = function(caowEdited) {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/car',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                id: caowEdited.unid,
                token: $scope.token,
                action: 'elite',
            }
        }).then(function(response) {
            caowEdited = response.data;

            $scope.fileEdited.caow.forEach(function(car, index) {
                if (car.unid == caowEdited.unid) {
                    $scope.fileEdited.caow[index] = caowEdited;
                }
            });

            addActivity("Elite " + caowEdited.crdb);
            $scope.hasLicenseFree = licenseFree();

            $scope.loading = "hidden";
        });
    }

    $scope.allowLicense = function(car) {
        return car != undefined && car.elcl == 0 && licenseFree();
    }

    function licenseFree() {
        var licenseNumber = 0;
        $scope.fileEdited.caow.forEach(function(car) {
            if (car.elcl > 0) {
                licenseNumber++;
            }
        });

        return licenseNumber < 15;
    }


    $scope.removeElite = function(caowEdited) {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/car',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                id: caowEdited.unid,
                token: $scope.token,
                action: 'remove-elite',
            }
        }).then(function(response) {
            caowEdited = response.data;

            $scope.fileEdited.caow.forEach(function(car, index) {
                if (car.unid == caowEdited.unid) {
                    $scope.fileEdited.caow[index] = caowEdited;
                }
            });
            $scope.hasLicenseFree = licenseFree();
            addActivity("Remove elite " + caowEdited.crdb);
            $scope.loading = "hidden";
        });
    }

    $scope.fuel = function() {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/gift',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                token: $scope.token,
                action: 'fuel',
                qty: $scope.fuelNb,
            }
        }).then(function(response) {
            addActivity("Ajout Essence");
            $scope.loading = "hidden";
        });
    }

    $scope.restoToken = function() {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/gift',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                token: $scope.token,
                action: 'restoration',
                carId: $scope.legendsCarSelected,
                qty: $scope.legendsRestorationQty,
            }
        }).then(function(response) {
            $scope.loading = "hidden";
            addActivity("Ajout " + $scope.legendsRestorationQty + " jetons restauration pour " + $scope.carNames[$scope.legendsCarSelected]);
        });
    }

    $scope.fusionsThree = function() {
        fusions($scope.greenFusion, $scope.blueFusion, $scope.redFusion);
    }

    $scope.fusionsRed = function() {
        fusions(null, null, $scope.onlyRedFusion);
    }

    function fusions(green, blue, red) {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/gift',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                token: $scope.token,
                action: 'fusions',
                green: green,
                blue: blue,
                red: red,
                brand: $scope.brand,
            }
        }).then(function(response) {
            addActivity("Ajout Fusions " + green + " - " + blue + " - " + red);
            $scope.loading = "hidden";
        });
    }

    $scope.stage6Gift = function() {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/gift',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                token: $scope.token,
                action: 'stage6',
                carId: $scope.stage6car,
            }
        }).then(function(response) {
            $scope.loading = "hidden";
            addActivity("Ajout n6 " + $scope.carNames[$scope.stage6car]);
        });
    }

    $scope.eliteTokensGift = function() {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/gift',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                token: $scope.token,
                action: 'elite',
                green: $scope.eliteGreen,
                blue: $scope.eliteBlue,
                red: $scope.eliteRed,
                yellow: $scope.eliteYellow,
            }
        }).then(function(response) {
            $scope.loading = "hidden";
            addActivity("Ajout Composants élite " + $scope.eliteGreen + " - " + $scope.eliteBlue + " - " + $scope.eliteRed + " - " + $scope.eliteYellow);
        });
    }

    $scope.eliteTokensReset = function() {
        $scope.loading = "load";
        $http({
            method: 'POST',
            url: '/csr-front/reset',
            headers: { 'Content-type': 'application/json; charset=UTF-8' },
            params: {
                token: $scope.token,
                type: 'elite',
                tokens: {
                    Green: $scope.fileEdited.afms.Green - $scope.eliteTokens.Green,
                    Blue: $scope.fileEdited.afms.Blue - $scope.eliteTokens.Blue,
                    Red: $scope.fileEdited.afms.Red - $scope.eliteTokens.Red,
                    Yellow: $scope.fileEdited.afms.Yellow - $scope.eliteTokens.Yellow,
                },
            }
        }).then(function(response) {
            $scope.fileEdited = response.data;
            $scope.eliteTokensSpent = response.data.afms;
            $scope.eliteTokens = {
                Green: 0,
                Blue: 0,
                Red: 0,
                Yellow: 0,
            }
            $scope.loading = "hidden";
            addActivity("Reset Composants elites");
        });
    }
}]);