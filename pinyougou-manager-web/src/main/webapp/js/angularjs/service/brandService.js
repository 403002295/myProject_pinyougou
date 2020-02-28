app.service('brandService',function($http){

    //search
    this.search = function (page, rows, brand) {
        return $http.post('../brand/search.do?page='+page+'&rows='+rows,brand);
    }
    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?id='+id);
    }
    this.dele = function (ids) {
        return $http.get('../brand/delete.do?ids='+ids);
    }
    this.save = function (methodName,brand) {
        return $http.post('../brand/'+methodName+'.do',brand);
    }
})