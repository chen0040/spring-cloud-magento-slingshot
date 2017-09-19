(function(){
    var service = function($log, $http, xschenDialogService) {


        var getCategories = function(callback) {
            $http.get('/magento/categories')
            .then(function(response){
                if(callback) callback(response.data);
            }, function(response) {
                $log.debug(response);
            });
        };

        var getCategoryById = function(categoryId, callback) {
            $http.get('/magento/categories/' + categoryId)
            .then(function(response){
                if(callback) callback(response.data);
            }, function(response) {
                $log.debug(response);
            });
        };

        var getProductsInCategory = function(categoryId, callback) {
            $http.get('/magento/categories/' + categoryId + '/products')
            .then(function(response){
                if(callback) callback(response.data);
            }, function(response) {
                $log.debug(response);
            });
        };

        var getProducts = function(callback) {
            $http.get('/magento/products')
            .then(function(response) {
                if(callback) callback(response.data);
            }, function(response) {
                $log.debug(response);
            })
        };



        return {
            getCategories : getCategories,
            getCategoryById : getCategoryById,
            getProductsInCategory : getProductsInCategory,
            getProducts : getProducts
        };
    };

    var module = angular.module('commons');
    module.factory('categoryService', ['$log', '$http', service]);
})();
