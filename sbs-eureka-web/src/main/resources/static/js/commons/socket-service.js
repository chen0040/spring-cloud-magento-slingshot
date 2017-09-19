(function(){
    var service = function($log, $http, $location, $interval, $timeout) {

        var stompClient = null;
        var socket = null;
        var connected = false;
        var thisCallback = null;
        var thisIdentity = null;
        var thisUrl = "/sbs";
        var thisUrlSecured = "/sbs-secured";
        var thisIntervalHandler = null;
        var thisCounter = 0;

        function stompConnect() {
            console.log('STOMP: Attempting connection');
            stompClient = null;
            connect(thisCallback);
        }

        var setUrl = function(newUrl) {
            thisUrl = newUrl;
        };

        var connectToHost = function() {
            var url = thisUrl;

            if(thisIdentity && thisIdentity.authenticated) {
                url = thisUrlSecured;
            }

            console.log('connecting to ' + url);

            socket = new SockJS(url);
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                connected = true;

                var username = thisIdentity.username;

                if(thisCallback) thisCallback('connect', connected);
                stompClient.subscribe('/topics/' + username + '/cart', function(message) {
                    if(thisCallback) thisCallback('cart', message.body);
                });


            }, function (error) {
               console.log('STOMP: ' + error);
               setTimeout(stompConnect, 5000);
               console.log('STOMP: Reconecting in 5 seconds');
            });
        };

        var connect = function(identity, callback) {
            thisCallback = callback;
            thisIdentity = identity;

            if(stompClient == null) {
                connectToHost();
            }
        };

        var disconnect = function() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            if (thisIntervalHandler != null) {
                thisIntervalHandler.cancel();
                thisIntervalHandler = null;
            }
            connected = false;
            console.log("Disconnected");
        };

        var send = function(topic, obj) {
            if(stompClient != null){
                stompClient.send(topic, {}, JSON.stringify(obj));
            }
        };

        var afterConnected = function(callback) {
            if(connected) {
                if(callback) callback();
            } else {
                $timeout(function(){
                    console.log('waiting to connect ...');
                    afterConnected(callback);
                }, 1000);
            }
        };

        return {
            connect : connect,
            disconnect : disconnect,
            send : send,
            setUrl : setUrl,
            afterConnected : afterConnected
        };
    };

	var module = angular.module('commons');
	module.factory('socketService', ['$log', '$http', '$location', '$interval', '$timeout', service]);
})();
