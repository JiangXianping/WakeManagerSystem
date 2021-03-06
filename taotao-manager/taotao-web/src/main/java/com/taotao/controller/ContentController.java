package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDataResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

@Controller
@RequestMapping("/content")
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/query/list")
	@ResponseBody
	public EUDataResult getContentList(Long categoryId,int page,int rows){
		return contentService.getContentList(categoryId, page, rows);
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult insertTbContent(TbContent content){
		return contentService.insertContent(content);
	}
	
	
}
