(function(){
    var app = angular.module('admin', [
    'ui.bootstrap',
    'ngRoute', 'ngSanitize',
    'commons']);

    app.config(function($routeProvider, $logProvider) {
         $logProvider.debugEnabled(false);
         $routeProvider


            .when('/account', {
                templateUrl : 'html/commons/account',
                controller  : 'accountController'
            })

            .otherwise({redirectTo:'/account'});
    });

    var controller = function($timeout, $log, $scope, socketService, messageService, categoryService) {
        var vm = this;
        vm.activate = function() {
            messageService.subscribe('ping', 'adminController', function(channel, message){
                var eventMessage = JSON.parse(message);
                $log.debug(eventMessage);
            });

            categoryService.getCategories(function(categories){
                $scope.categories = categories;
            });
            
            $timeout(function(){
                socketService.connect(function(state, message){
                    messageService.route(state, message);
                });
            }, 500);
        };

        vm.activate();
    };

    app.controller("adminController", ['$timeout', '$log', '$scope', 'socketService', 'messageService', 'categoryService', controller]);


})();
