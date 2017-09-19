(function(){
    var service = function($log, $http, xschenDialogService) {


        var getCart = function(callback) {
            $http.get('/magento/cart')
            .then(function(response){
                if(callback) callback(response.data);
            }, function(response) {
                $log.debug(response);
            });
        };

        var addToCart = function(sku, qty, shippingId, callback){
            $http.get('/magento/cart/add-item?sku=' + encodeURIComponent(sku) + '&qty=' + qty + '&shippingId=' + shippingId)
            .then(function(response){
                if(callback) callback(response.data);
            }, function(response) {
                $log.debug(response);
            });
        };

        var deleteItem = function(line, callback) {
            $http.delete('/magento/cart/items/' + line.item_id)
            .then(function(response){
                if(callback) callback(response.data);
            }, function(response) {
                $log.debug(response);
            })
        };

        return {
            getCart : getCart,
            addToCart : addToCart,
            deleteItem : deleteItem
        };
    };

    var module = angular.module('commons');
    module.factory('cartService', ['$log', '$http', service]);
})();
