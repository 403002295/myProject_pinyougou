package com.pinyougou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import com.pinyougou.pojogroup.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		//获取规格实体
		TbSpecification tbspecification = specification.getSpecification();				
		specificationMapper.insert(tbspecification);	
		
		//获取规格选项集合
		List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
		for( TbSpecificationOption option:specificationOptionList){
			option.setSpecId(tbspecification.getId());//设置规格ID
			specificationOptionMapper.insert(option);//新增规格
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification) {
		//1、因为前端的代码有ID 所以更新此ID所对应的规格名称
		specificationMapper.updateByPrimaryKey(specification.getSpecification());
		//删除规格选项并插入新的
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(specification.getSpecification().getId());
		specificationOptionMapper.deleteByExample(example);
		//2、重新插入
		for (TbSpecificationOption tbSpecificationOption : specification.getSpecificationOptionList()) {
			specificationOptionMapper.insert(tbSpecificationOption);
		}
	}

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id) {
		//1、根据ID查询对应的TbSpecification
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		//2、根据id获取TbSpecificationOptionList
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		List<TbSpecificationOption> tbSpecificationOptions = specificationOptionMapper.selectByExample(example);
		//3、封装数据
		Specification specification = new Specification();
		specification.setSpecification(tbSpecification);
		specification.setSpecificationOptionList(tbSpecificationOptions);
		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			//删除规格表数据
			specificationMapper.deleteByPrimaryKey(id);
			
			//删除规格选项表数据		
			TbSpecificationOptionExample example=new TbSpecificationOptionExample();
			com.pinyougou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(id);
			specificationOptionMapper.deleteByExample(example);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		@Override
		public List<Map> selectOptionList() {
			// TODO Auto-generated method stub
			return specificationMapper.selectOptionList();
		}
	
}
