(function(){
    var controller = function($log, $scope, $timeout, cartService, messageService, logisticsService) {
        var vm = this;

        vm.activate = function() {
            messageService.subscribe('cart', 'cartController', function(channel, message){
                var cart = JSON.parse(message);
                console.log(cart);
                $timeout((function(_cart){
                    return function(){
                        $scope.cart = _cart;
                    };
                })(cart), 200);
            });

            cartService.getCart(function(cart) {
                $log.debug(cart);
                $scope.cart = cart;
                var items = cart.cart.items;
                var shippingOptions = cart.shippingOptions;

                for(var i = 0; i < items.length; ++i) {
                    var line = items[i];
                    var sku = line.sku;
                    var shippingId = 0;
                    if(sku in shippingOptions) {
                        shippingId = shippingOptions[sku];
                    }

                    logisticsService.getShippingOptionById(shippingId,
                        (function(_lineIndex) {
                            return function(shippingOption) {
                                console.log(shippingOption);
                                $scope.cart.cart.items[_lineIndex].shipping = shippingOption;
                            };
                    })(i));
                }
            });
        };

        $scope.deleteItem = function(line){
            cartService.deleteItem(line, function(cart) {
                $timeout((function(_cart){
                    return function(){
                        $scope.cart = _cart;
                    };
                })(cart), 500);
            });
        };

        vm.activate();
    };

    var module = angular.module('commons');
    module.controller('cartController', ['$log', '$scope', '$timeout', 'cartService', 'messageService', 'logisticsService', controller]);
})();
