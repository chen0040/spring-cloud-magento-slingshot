(function(){
    var app = angular.module('client', [
    'ui.bootstrap',
    'ngRoute', 'ngSanitize',
    'commons']);

    app.config(function($routeProvider, $logProvider) {
         $logProvider.debugEnabled(true);
         $routeProvider

        .when('/category/:id', {
            templateUrl : 'html/public/category',
            controller  : 'categoryController',
            resolve: {
               id : ['$route', function($route) { return $route.current.params.id; }]
            }
        })

        .when('/products/:sku', {
            templateUrl : 'html/public/product',
            controller  : 'productController',
            resolve: {
               sku : ['$route', function($route) { return $route.current.params.sku; }]
            }
        })

        .when('/root-category', {
            templateUrl : 'html/public/category',
            controller  : 'categoryController',
            resolve: {
               id : function() { return 2; }
            }
        })

        .when('/cart', {
            templateUrl : 'html/public/cart',
            controller  : 'cartController'
        })

        .otherwise({redirectTo:'/root-category'});
    });

    var controller = function($timeout, $log, $scope, socketService, messageService, categoryService, cartService, userService) {
        var vm = this;
        vm.activate = function() {
            messageService.subscribe('cart', 'clientController', function(channel, message){
                var cart = JSON.parse(message);
                console.log(cart);
                $timeout((function(_cart){
                    return function(){
                        $scope.cart = _cart;
                    };
                })(cart), 500);
            });

            categoryService.getCategories(function(categories){
                $scope.categories = categories;
            });

            cartService.getCart(function(cart) {
                $log.debug(cart);
                $scope.cart = cart;
            });

            $timeout(function(){
                userService.getIdentity(function(identity){

                    console.log(identity);
                    socketService.connect(identity, function(state, message){
                        messageService.route(state, message);
                    });
                });
            }, 500);
        };

        vm.activate();
    };

    app.controller("clientController", ['$timeout', '$log', '$scope', 'socketService', 'messageService', 'categoryService', 'cartService', 'userService', controller]);


})();
