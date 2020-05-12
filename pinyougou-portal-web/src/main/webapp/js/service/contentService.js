app.service('contentService',function ($http) {

    //根据分类ID查询分类列表
    this.findByCategory = function (categoryId) {
        return $http.get('content/findByCategoryId.do?categoryId='+categoryId);
    }
})