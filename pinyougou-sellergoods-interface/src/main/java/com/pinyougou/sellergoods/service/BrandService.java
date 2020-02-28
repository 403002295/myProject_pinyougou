package com.pinyougou.sellergoods.service;

import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;

import java.util.List;

public interface BrandService {
    /**
     * 查询所有
     * @return
     */
    public List<TbBrand> findAll();
    /**
     * 查询品牌信  伴随分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult findPage(TbBrand brand,int pageNum,int pageSize);

    /**
     * 添加品牌
     * @param brand
     */
    public void add(TbBrand brand);

    /**
     * 修改
     * @param brand
     */
    public void update(TbBrand brand);

    /**
     * 根据ID获取实体
     * @param id
     * @return
     */
    public TbBrand findOne(Long id);

    /**
     * 根据ID删除
     */
    public void delete(Long[] id);
}
