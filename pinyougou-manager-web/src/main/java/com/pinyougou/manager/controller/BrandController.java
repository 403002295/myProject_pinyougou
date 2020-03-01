package com.pinyougou.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {
    private Logger logger = Logger.getLogger(BrandController.class);

    @Reference
    BrandService brandService;
    @RequestMapping("/search")
    public PageResult findPage(@RequestBody TbBrand brand,int page, int rows) {
        return brandService.findPage(brand,page,rows);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand brand){
        try {

            brandService.add(brand);
            return new Result(true,"增加成功");
        }catch (Exception e){
            logger.error("BrandController--add 添加品牌时出错"+e.getMessage());
            return new Result(false,"增加失败");
        }

    }

    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand brand){
        try {
            brandService.update(brand);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            logger.error("BrandController--update 报错："+e.getMessage());
            return new Result(false, "修改失败");
        }
    }

    @RequestMapping("/findOne")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);
    }

    @RequestMapping("/delete")
    public Result delete(Long []ids) {
        try {
            brandService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            logger.error("BrandController -- delete 失败："+e.getMessage());
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {
        return brandService.selectOptionList();
    }
}
