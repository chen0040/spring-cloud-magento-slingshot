(function(){
    var service = function($log, $http){
        var getShippingOptions = function(sku, callback) {
            $http.get('/magento/logistics/shipping-options?sku=' + encodeURIComponent(sku))
            .then(function(response){
                if(callback) callback(response.data);
            }, function(response) {
                $log.debug(response);
            });
        };

        var getShippingOptionById = function(id,callback) {
            $http.get('/magento/logistics/shipping-options/' + id)
            .then(function(response){
                if(callback) callback(response.data);
            }, function(response) {
                $log.debug(response);
            });
        }

        return {
            getShippingOptions: getShippingOptions,
            getShippingOptionById: getShippingOptionById
        };
    };

    var module = angular.module('commons');
    module.factory('logisticsService', ['$log', '$http', service]);


})();
