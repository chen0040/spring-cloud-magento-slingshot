(function(){
    var controller = function($scope, $location, productService, cartService, logisticsService, sku) {
        var vm = this;


        $scope.sku = sku;
        $scope.cartItem = {
            sku: '',
            qty: 1,
            shippingId: 0
        };
        $scope.shippingOptions = [];


        vm.activate = function() {
            productService.getProductBySku($scope.sku, function(product) {
                $scope.product = product;
                $scope.cartItem.sku = $scope.product.sku;
            });
            logisticsService.getShippingOptions($scope.sku, function(options){
                console.log(options);
                $scope.shippingOptions = options;
            });
        };

        $scope.addToCart = function() {
            cartService.addToCart($scope.cartItem.sku, $scope.cartItem.qty, $scope.cartItem.shippingId, function(cart){
                console.log(cart);
            });
        };


        vm.activate();
    };

    var module = angular.module('commons');
    module.controller('productController', ['$scope', '$location', 'productService', 'cartService', 'logisticsService', 'sku', controller]);
})();
