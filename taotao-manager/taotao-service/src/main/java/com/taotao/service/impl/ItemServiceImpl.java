package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public TbItem getItemById(Long id) {
		 TbItemExample example = new TbItemExample();
		 Criteria criteria = example.createCriteria();
		 criteria.andIdEqualTo(id);
		 List<TbItem> list = itemMapper.selectByExample(example);
		 System.out.println(list.get(0).getImage());
		 if(list!=null && list.size()>0){
		 TbItem item = list.get(0);
		 return item;
		 }
		return null;
	}

	/**
	 * 商品列表查询
	 */
	@Override
	public EUDataResult getItemList(int page, int rows) {
		TbItemExample example = new TbItemExample();
		// 分页处理
		PageHelper.startPage(page, rows);
		System.out.println("分页处理");
		List<TbItem> list = itemMapper.selectByExample(example);
		// 创建一个返回对象
		EUDataResult result = new EUDataResult();
		result.setRows(list);
		// 取记录总条数
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	@Override
	public List<TbItem> getAllList() {
		return itemMapper.getAllList();
	}

	@Override
	public TaotaoResult createItem(TbItem item) {
		Long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//插入到数据库
		itemMapper.insert(item);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult updateItem(TbItem item) {
		item.setUpdated(new Date());
		item.setStatus((byte)1);
		item.setCreated(new Date());
		itemMapper.updateByPrimaryKey(item);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult deleteItem(Long itemId) {
		itemMapper.deleteByPrimaryKey(itemId);
		return TaotaoResult.ok();
	}
}
