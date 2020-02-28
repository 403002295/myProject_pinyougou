app.controller('brandController',function ($scope,brandService,$controller) {

    //继承 baseController  两个控制器 共享$scope
    $controller('baseController',{$scope:$scope})

    //读取列表数据绑定到表单当中
    $scope.findPage = function (page, rows) {
        $http.get('../brand/findPage.do?page='+page+'&rows='+rows).success(function (response) {
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;//更新总记录数
        })
    }
    //定义搜索对象
    $scope.searchEntity={}
    $scope.search = function(page, rows){
        brandService.search(page,rows,$scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        )

    }




    //	添加数据
    $scope.save = function () {
        //将更新和添加放在一个方法里
        var methodName = 'add';//方法名称
        if ($scope.entity.id != null) { // 如果查询的ID存在 则变为更新
            methodName = "update";
        }

        brandService.save(methodName,$scope.entity).success(function (response) {
            if (response.success){
                //重新加载
                $scope.reloadList();
            }else {
                alert(response.message)
            }

        });

    }
    //实体查询
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        )
    }

    //批量删除
    $scope.dele = function () {
        //获取复选框的值
        brandService.dele($scope.selectIds).success(
            function (response) {
                if (response.success){
                    $scope.reloadList();
                }

            }
        )

    }

})