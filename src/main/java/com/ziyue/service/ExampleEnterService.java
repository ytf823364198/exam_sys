package com.ziyue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ziyue.dao.ExampleEnterDao;
import com.ziyue.dao.ExampleGoodsDao;
import com.ziyue.entity.ExampleEnter;

@Service
public class ExampleEnterService  {
	@Autowired
	private ExampleGoodsDao exampleGoodsDao;
	@Autowired
	private ExampleEnterDao exampleEnterDao;
	
	public ExampleEnter findEnterById(String id) {
		ExampleEnter enter = exampleEnterDao.findEnterById(id);
		if( null != enter ){
			enter.setGoodses(exampleGoodsDao.findGoodsByEnterId(enter.getId()));
		}
		return enter;
	}
	
	public ExampleEnter findEnterBaseById(String id) {
		ExampleEnter enter = exampleEnterDao.findEnterById(id);
		return enter;
	}
	
	public int findEnterGoodsCountByEnterId(String enterId){
		return exampleGoodsDao.findGoodsCountByEnterId(enterId);
	}
	
	public List<ExampleEnter> findEnters() {
		List<ExampleEnter> enters =  exampleEnterDao.findEnters();
		if(null != enters && enters.size() > 0){
			for(ExampleEnter enter : enters){
				if( null != enter ){
					enter.setGoodses(exampleGoodsDao.findGoodsByEnterId(enter.getId()));
				}
			}
		}
		return enters;
	}
	
	

}
