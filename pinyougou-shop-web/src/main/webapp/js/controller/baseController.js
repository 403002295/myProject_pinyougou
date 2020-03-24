app.controller('baseController',function ($scope) {

    //重新加载列表
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    }
    //分页控件
    $scope.paginationConf ={
        currentPage:1,
        totalItems : 10,
        itemsPerPage : 10,
        perPageOptions : [10, 20, 30, 40, 50],
        onChange : function () {
            $scope.reloadList();// 以上参数任意一个发生变化 重新加载

        }
    }

    //选中的ID
    $scope.selectIds = [];
    //跟新复选
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            $scope.selectIds.push(id);
        }else {
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx,1);//删除没有选中的ID
        }
    }

    //从集合中按照key查找对象
    $scope.searchObjectByKey = function (list, key, keyValue) {
        for (var i = 0; i < list.length; i++) {
            if (list[i][key] == keyValue) {
                return list[i];
            }
        }
        return null;
    }
})