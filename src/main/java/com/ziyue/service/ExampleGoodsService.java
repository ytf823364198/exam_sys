package com.ziyue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ziyue.dao.ExampleGoodsDao;
import com.ziyue.entity.ExampleGoods;


@Service
public class ExampleGoodsService {
	@Autowired
	private ExampleGoodsDao exampleGoodsDao;
	
	
	public ExampleGoods findGoodsById(String id) {
		return exampleGoodsDao.findGoodsById(id);
	}

	public List<ExampleGoods> findGoods() {
		return exampleGoodsDao.findGoods();
	}


}
