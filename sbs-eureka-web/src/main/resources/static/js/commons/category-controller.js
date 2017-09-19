(function(){
    var controller = function($scope, $location, $http, categoryService, id) {
        var vm = this;


        $scope.products = [];
        $scope.categoryId = id;
        $scope.pageSize = 10;
        $scope.pager = new springbootmagento.Pager();

        vm.activate = function() {
            categoryService.getCategoryById($scope.categoryId, function(category){
                $scope.category = category;
            });
            loadProducts();
        };

        function loadProducts() {
            $scope.pageLoading = true;
            categoryService.getProductsInCategory($scope.categoryId, function(data) {
                $scope.pageLoading = false;
                $scope.products = [];
                for(var i=0; i < data.length; ++i){
                    $scope.products.push(new springbootmagento.Product(data[i].sku, $http));
                }
                $scope.pager.load($scope.products);
            });
        }

        $scope.goBack = function() {
            window.history.back();
        };

        $scope.newProduct = function() {
            $location.path('/new-product/' + $scope.categoryId);
        };

        vm.activate();
    };

    var module = angular.module('commons');
    module.controller('categoryController', ['$scope', '$location', '$http', 'categoryService', 'id', controller]);
})();
