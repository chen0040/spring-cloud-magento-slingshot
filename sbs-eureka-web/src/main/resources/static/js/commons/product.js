var springbootmagento = springbootmagento || {};

(function(jss){
    var Product = function(sku, http) {
        this.sku = sku;
        this.name = sku;
        this.id = 0;
        this.type_id = 'loading ...';
        this.status = 0;
        this.price = 0;
        this.http = http;
        this.weight = 1;
        this.updated_at = '';
        this.loaded = false;
        this.imageUrl = '';
    };

    Product.prototype.load = function(callback) {
        this.imageUrl = '';
        var sku = this.sku;
        this.http.get('/magento/products/'+encodeURIComponent(sku)).then((function(this_, callback_){
            return function(response) {

                var p = response.data;
                console.log(p);
                for (key in p) {
                    this_[key] = p[key];
                }
                this_.loaded = true;
                if(callback_ != null) {
                    callback_(this_);
                }
            };
        })(this, callback), function(reason){
        });
    };

    Product.prototype.createNew = function(callback) {
        this.http.get('/magento/new-product').then((function(this_, callback_){
            return function(response) {

                var p = response.data;
                console.log(p);
                for (key in p) {
                    this_[key] = p[key];
                }
                this_.loaded = true;
                if(callback_ != null) {
                    callback_(this_);
                }
            };
        })(this, callback), function(reason){
        });
    };

    Product.prototype.saveToCategory = function(categoryId, callback) {
        this.http.post('/magento/products', this, {headers: {'Content-Type': 'application/json'} })
        .then((function(this_, callback_, categoryId_){
           return function(response) {

               var p = response.data;
               console.log(p);
               for (key in p) {
                   this_[key] = p[key];
               }
               this_.loaded = true;
               this_.http.get('/magento/add-product-to-category?sku=' + encodeURIComponent(this_.sku) + '&categoryId=' + categoryId_)
                .then(function(response){
                    console.log(response);
                    console.log('added!');
                });
               if(callback_ != null) {
                   callback_(this_);
               }
           };
       })(this, callback, categoryId), function(reason){
       });

    }


    jss.Product = Product;

})(springbootmagento);
