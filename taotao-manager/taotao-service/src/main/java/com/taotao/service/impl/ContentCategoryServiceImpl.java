package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.taotao.common.pojo.EUDataResult;
import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Override
	public List<EUTreeNode> getContentCateList(Long parentid) {
		TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
		Criteria criteria = tbContentCategoryExample.createCriteria();
		criteria.andParentIdEqualTo(parentid);
		
		//执行查询
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
		List<EUTreeNode> resultList = new ArrayList<>();
		for(TbContentCategory tbContentCategory:list){
			EUTreeNode euTreeNode = new EUTreeNode();
			euTreeNode.setId(tbContentCategory.getId());
			euTreeNode.setState(tbContentCategory.getIsParent()?"closed":"true");
			euTreeNode.setText(tbContentCategory.getName());
			
			resultList.add(euTreeNode);
		}
		return resultList;
	}

	@Override
	public TaotaoResult insertConentCategory(Long parentId, String name) {
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setName(name);
		tbContentCategory.setIsParent(false);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setUpdated(new Date());
		tbContentCategory.setCreated(new Date());
		//1.正常，2删除
		tbContentCategory.setStatus(1);
		tbContentCategory.setParentId(parentId);
		//添加记录
		tbContentCategoryMapper.insert(tbContentCategory);
		TbContentCategory parentCat = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		//判断是否为true
		if(!parentCat.getIsParent()){
			parentCat.setIsParent(true);
			//更新父节点
			tbContentCategoryMapper.updateByPrimaryKey(parentCat);
		}
		//返回结果
		return TaotaoResult.ok(tbContentCategory);
	}

	@Override
	public TaotaoResult deleteConectCategory(Long parentId, Long id) {
		//删除记录
		tbContentCategoryMapper.deleteByPrimaryKey(id);
		TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
		Criteria criteria = tbContentCategoryExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
		if(list.size()==0){
			TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
			tbContentCategory.setIsParent(false);
			tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
		}
		return TaotaoResult.ok();
	}

	@Override
	public Long selectContentCategory(Long id) {
		TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
		return tbContentCategory.getParentId();
	}

	@Override
	public TaotaoResult updateContentCategory(Long id, String name) {
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setId(id);
		tbContentCategory.setName(name);
		//更新名称
		tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
		return TaotaoResult.ok();
	}


	

}
