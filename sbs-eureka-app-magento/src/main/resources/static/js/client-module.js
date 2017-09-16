(function(){
    var app = angular.module('client', [
    'ui.bootstrap',
    'ngRoute', 'ngSanitize']);

/*
    app.config(function($routeProvider, $logProvider) {
         $logProvider.debugEnabled(false);
         $routeProvider


            .when('/account', {
                templateUrl : 'html/commons/account',
                controller  : 'accountController'
            })

            .otherwise({redirectTo:'/account'});
    });
*/

    var controller = function($timeout, $log, $scope, socketService, messageService) {
        var vm = this;
        vm.activate = function() {
            messageService.subscribe('ping', 'clientController', function(channel, message){
                var eventMessage = JSON.parse(message);
                $log.debug(eventMessage);
            });

            $timeout(function(){
                socketService.connect(function(state, message){
                    messageService.route(state, message);
                });
            }, 500);
        };

        vm.activate();
    };

    app.controller("clientController", ['$timeout', '$log', '$scope', 'socketService', 'messageService', controller]);


})();
