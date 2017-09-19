(function(){
    var service = function($log, $http, xschenDialogService) {
        var productImage = null;
        var productImageType = null;
        var productModel = null;
        var productModel2 = null;


        var deleteProduct = function(sku, callback) {
            $http.delete('/magento/products/' + encodeURIComponent(sku)).then(
            function(response) {
                console.log(response);
                if(callback) callback(response.data);
            }, function(response) {

            });
        };


        var checkSku = function(sku, callback) {
            $http.get('/magento/products/'+encodeURIComponent(sku)).then(
            function(response) {
                    var p = response.data;
                    if(callback) callback(p);

            }, function(reason){
            });
        };


        var getProductBySku = function(sku, callback) {
            var product = new springbootmagento.Product(sku, $http);
            product.load(callback);
        };

        var hasProductImage = function() {
            return productImage != null;
        };

        var hasProductModel = function() {
            return productModel != null;
        };

        var hasProductModel2 = function() {
            return productModel2 != null;
        };

        var storeProductImage = function(img) {

            var name = img.name;

            var fileExtension = name.toLowerCase().split('.').pop();

            $log.debug(fileExtension);

            if(fileExtension != 'png' && fileExtension != 'jpg') {
                xschenDialogService.alert('Image format not supported! (Must be PNG or JPEG)', 'Image Upload Error');
                return;
            }

            productImage = img;
            productImageType = fileExtension;

        };

        var storeProductModel = function(model) {
            productModel = model;
        };

        var storeProductModel2 = function(model) {
            productModel2 = model;
        };

        var uploadProductImage =  function(productImageSku, callback) {
             var fd = new FormData();

             fd.append('file', productImage);

             var url = "/magento/upload/product-image-" + productImageType + "/" + productImageSku;

             $log.debug(url);

             $http({
                 method: 'POST',
                 url: url,
                 headers: {
                     'Content-Type': undefined
                 },
                 transformRequest: angular.identity,
                 data: fd
             }).success(function(response) {
                $log.debug(response);
                productImage = null;
                productImageType = null;
                if(callback) callback(response);
             }).error(function(response) {
                 $log.debug(response.exception);
                 $log.debug(response.message);
             });
        };

        var uploadProductModel =  function(productSku, callback) {
             var fd = new FormData();

             fd.append('file', productModel);

             var url = "/product-3d/" + productSku;

             $log.debug(url);

             $http({
                 method: 'POST',
                 url: url,
                 headers: {
                     'Content-Type': undefined
                 },
                 transformRequest: angular.identity,
                 data: fd
             }).success(function(response) {
                $log.debug(response);
                productModel = null;
                if(callback) callback(response);
             }).error(function(response) {
                 $log.debug(response.exception);
                 $log.debug(response.message);
             });
        };

        var uploadProductModel2 =  function(productSku, callback) {
             var fd = new FormData();

             fd.append('file', productModel2);

             var url = "/product-3d-model-2/" + productSku;

             $log.debug(url);

             $http({
                 method: 'POST',
                 url: url,
                 headers: {
                     'Content-Type': undefined
                 },
                 transformRequest: angular.identity,
                 data: fd
             }).success(function(response) {
                $log.debug(response);
                productModel = null;
                if(callback) callback(response);
             }).error(function(response) {
                 $log.debug(response.exception);
                 $log.debug(response.message);
             });
        };

        return {
            getProductBySku : getProductBySku,
            storeProductImage : storeProductImage,
            hasProductImage : hasProductImage,
            uploadProductImage : uploadProductImage,
            storeProductModel : storeProductModel,
            hasProductModel : hasProductModel,
            uploadProductModel : uploadProductModel,
            storeProductModel2 : storeProductModel2,
            hasProductModel2 : hasProductModel2,
            uploadProductModel2 : uploadProductModel2,
            checkSku : checkSku,
            deleteProduct : deleteProduct
        };
    };

    var module = angular.module('commons');
    module.factory('productService', ['$log', '$http', service]);
})();
