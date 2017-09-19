(function(){
    var controller = function($scope, $location, productService, cartService, sku) {
        var vm = this;


        $scope.sku = sku;
        $scope.cartItem = {
            sku: '',
            qty: 1
        };


        vm.activate = function() {
            productService.getProductBySku($scope.sku, function(product) {
                $scope.product = product;
                $scope.cartItem.sku = $scope.product.sku;
            });
        };

        $scope.addToCart = function() {
            cartService.addToCart($scope.cartItem.sku, $scope.cartItem.qty, function(cart){
                console.log(cart);
            });
        };


        vm.activate();
    };

    var module = angular.module('commons');
    module.controller('productController', ['$scope', '$location', 'productService', 'cartService', 'sku', controller]);
})();
