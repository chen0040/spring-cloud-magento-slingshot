var springbootmagento = springbootmagento || {};

(function(jss){
    var Pager = function() {
        this.data = [];
        this.pageSize = 9;
        this.pageCount = 0;
        this.content = [];
        this.pageIndex = 0;
    };

    Pager.prototype.loadPage = function(pageIndex) {
        var content = [];
        var start = this.pageIndex * this.pageSize;
        var end = start + this.pageSize;
        for(var i = start; i < end && i < this.data.length; ++i) {
            content.push(this.data[i]);
        }
        this.content = content;
        for(var i=0; i < this.content.length; ++i) {
            if(this.content[i].load){
                this.content[i].load();
            }
        }
    };

    Pager.prototype.load = function(data) {
        this.data = data;
        this.pageIndex = 0;
        this.pageCount = Math.ceil(this.data.length / this.pageSize);
        this.loadPage();
    };

    Pager.prototype.nextPage = function() {
        if(this.pageIndex < this.pageCount - 1) {
            this.pageIndex++;
            this.loadPage();
        }
    };

    Pager.prototype.prevPage = function() {
        if(this.pageIndex > 0) {
            this.pageIndex--;
            this.loadPage();
        }
    };

    Pager.prototype.firstPage = function() {
        this.pageIndex = 0;
        this.loadPage();
    };

    Pager.prototype.lastPage = function() {
        this.pageIndex = this.pageCount - 1;
        this.loadPage();
    };


    jss.Pager = Pager;

})(springbootmagento);
