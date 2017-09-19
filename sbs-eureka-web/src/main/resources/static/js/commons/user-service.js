(function(){
    var service = function($log, $http){
        var getIdentity = function(callback){
            $http.get('/magento/identity')
            .then(function(response){
                if(callback) callback(response.data);
            }, function(response) {
                $log.debug(response);
            });
        };

        return {
            getIdentity: getIdentity
        };
    };

    var module = angular.module('commons');
    module.factory('userService', ['$log', '$http', service]);
})();
