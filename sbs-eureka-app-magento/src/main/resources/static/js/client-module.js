(function(){
    var app = angular.module('client', [
    'ui.bootstrap',
    'ngRoute', 'ngSanitize']);

    app.config(function($routeProvider, $logProvider) {
         $logProvider.debugEnabled(false);
         $routeProvider


            .when('/events', {
                templateUrl : 'html/events',
                controller  : 'eventsController'
            })

            .otherwise({redirectTo:'/events'});
    });

    var controller = function($timeout, $log, $scope, socketService, messageService) {
        var vm = this;
        vm.activate = function() {
            messageService.subscribe('event', 'clientController', function(channel, message){
                var eventMessage = JSON.parse(message);
                $timeout((function(_event){
                    return function() {
                        $scope.lastEvent = _event;
                    };
                })(eventMessage), 300);

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
