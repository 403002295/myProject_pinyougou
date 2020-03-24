 //控制层 
app.controller('goodsController' ,function($scope,$controller ,$location  ,goodsService,uploadService,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){
	    var id = $location.search()['id'];
        if (null == id) {
            return;
        }
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				editor.html($scope.entity.goodsDesc.introduction)
				//解析图片
				$scope.entity.goodsDesc.itemImages=JSON.parse($scope.entity.goodsDesc.itemImages);
				//显示扩展属性
				$scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);
				//读取规格
				$scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems);
				//遍历 SKU
				for (var i = 0; i < $scope.entity.itemList.length; i++) {
					$scope.entity.itemList[i].spec=JSON.parse($scope.entity.itemList[i].spec);
				}
			}
		);				
	}
	
	//保存 
	$scope.save=function(){
		//获取富文本编辑器内容
		$scope.entity.goodsDesc.introducation=editor.html();

		var serviceObject;//服务层对象
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					alert('保存成功');
					$scope.entity={};
					editor.html("");
					location.href='goods.html';//跳转到商品列表页面
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//add 添加
	$scope.add = function () {
		goodsService.add($scope.entity).success(
			function (response) {
				if (response.success){
					alert("保存成功")
					$scope.entity={}
				}else {
					alert(response.message)
				}
			}
		)
	}

	//上传文件
	$scope.uploadFile = function () {
		uploadService.uploadFile().success(function (response) {
			if (response.success){
				$scope.image_entity.url = response.message;//设置文件地址
			}else {
				alert(response.message)
			}
		}).error(function () {
			alert("文件上传发生错误")
		})
	}
    $scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}}
    //添加图片列表
	$scope.add_image_entity=function () {
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);

	}

	//移除图片
	$scope.remove_image_entity = function (index) {
		$scope.entity.goodsDesc.itemImages.splice(index,1);
	}

	//创建一级分类
	$scope.selectItemCatList= function () {
		itemCatService.findByParentId(0).success(
			function (response) {
				$scope.itemCat1List=response;
			}
		)
	}
	//监控entity.goods.category1Id 如果发生改变则查询此ID对应的下一级子分类
	$scope.$watch('entity.goods.category1Id',function (newValue, oldValue) {
		//根据选指查询二级分类
		itemCatService.findByParentId(newValue).success(
			function (response) {
				$scope.itemCat2List=response;
				$scope.itemCat3List=null;
				$scope.typeTemplate=null;
				$scope.entity.goods.typeTemplateId=null;
			}
		)
	})

	//监控二级分类的entity.goods.category1Id ID数值变化
	$scope.$watch('entity.goods.category2Id',function (newValue, oldValue) {
		//调用方法查询
		itemCatService.findByParentId(newValue).success(
			function (response) {
				$scope.itemCat3List=response;
			}
		)
	})
	//继续检查三级分类id的变化  根据此ID 查找
	$scope.$watch('entity.goods.category3Id',function (newValue, oldValue) {
		itemCatService.findOne(newValue).success(
			function (response) {
				$scope.entity.goods.typeTemplateId=response.typeId;
			}
		)
	})

	//检测模板ID的数值变化查询品牌信息
	$scope.$watch('entity.goods.typeTemplateId',function (newValue, oldValue) {
		if (undefined == newValue){
			newValue = $scope.entity.goods.typeTemplateId;
		}
		typeTemplateService.findOne(newValue).success(
			function (response) {
				$scope.typeTemplate=response;//获取类型模板
				$scope.typeTemplate.brandIds= JSON.parse($scope.typeTemplate.brandIds);//获取品牌列表

				//在模板类型发生变化的时候 更改更改模板之后的拓展属性
				//在修改时 也就是有指定ID的时候 由findOne方法封装扩展属性
				if ($location.search()['id'] == null){
					$scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.typeTemplate.customAttributeItems);
				}
			}
		)
		typeTemplateService.findSpecList(newValue).success(
			function (response) {
				$scope.specList = response;
			}
		)
	})

	//勾选规格选项则更新变量的值
	$scope.updateSpecAttribute = function ($event, name, value) {
		var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems ,'attributeName',name);
		if (null != object){
			if ($event.target.checked){
				object.attributeValue.push(value);
			}else {
				object.attributeValue.splice(object.attributeValue.indexOf(value),1);//移除选项
				if (object.attributeValue.length < 1) {
					$scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object),1);
				}
			}
		}else {
			$scope.entity.goodsDesc.specificationItems.push({'attributeName':name,'attributeValue':[value]})
		}
	}

	//创建SKU列表
	$scope.createItemList = function () {
		//初始化 SKU列表
		$scope.entity.itemList=[{spec:{},price:0,num:0,status:'0',isDefault:'0'}];
		//获取本商品的规格和规格选项
		var items = $scope.entity.goodsDesc.specificationItems;
		// items 的结构 [{attributeName:name,attributeValue:[]},{attributeName:name,attributeValue:[]}]
		//循环items 将各个规格选项排列组合
		for (var i = 0; i < items.length; i++) {
			//写一个拼装的方法
			$scope.entity.itemList=addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
		}

	}
	addColumn=function (list, columnName, columnValues) {
		var newList = [];
		for (var i = 0; i < list.length; i++) {
			var oldRow=list[i];
			for (var j = 0; j < columnValues.length; j++) {
				var newRow=JSON.parse(JSON.stringify(oldRow));
				newRow.spec[columnName]=columnValues[j];
				newList.push(newRow);
			}
		}
		return newList;
	}

	$scope.itemCatList=[];
	//查询出所有的分类信息 并把其ID作为数组的索引 把名称作为索引值
    $scope.findItemCatList = function () {
        itemCatService.findAll().success(
            function (response) {
                var itemCat={};
                for (var i = 0; i < response.length; i++) {
                     itemCat = response[i];
                     $scope.itemCatList[itemCat.id]=itemCat.name;
                }
            }
        )
    }

    //状态数组
    $scope.status = ['未审核','已审核','审核未通过','关闭'];


    //checkAttributeValue  根据规格名称和选项名称返回是否被勾选
	$scope.checkAttributeValue= function (specName, optionName) {
		//获取规格及选项信息
		var item = $scope.entity.goodsDesc.specificationItems;
		var object = $scope.searchObjectByKey(item,'attributeName',specName);
		if (null == object){
			return false;
		}else {
			if (object.attributeValue.indexOf(optionName)>=0){
				return true;
			}else {
				return false;
			}
		}
	}
});	
