package com.ziyue.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ziyue.dao.BaseAttachDao;
import com.ziyue.entity.BaseAttach;

@Service
public class BaseAttachService{
	@Autowired
	private BaseAttachDao baseAttachDao;
	
	public String addAttach(BaseAttach attach) {
		return baseAttachDao.addAttach(attach);
	}

	public BaseAttach findAttachById(String id) {
		return baseAttachDao.findAttachById(id);
	}
	
	public List<BaseAttach> findAttachsByObjectId(String objectId){
		return baseAttachDao.findAttachsByObjectId(objectId);
	}
	
	public List<BaseAttach> findAttachByObjectFlag(String objectId,String flag){
		return baseAttachDao.findAttachByObjectFlag(objectId, flag);
	}
	
	public List<BaseAttach> findAttachByObjectFormat(String objectId,String format){
		return baseAttachDao.findAttachByObjectFormat(objectId, format);
	}
	
	public List<BaseAttach> findAttachByObject(String objectId,String flag,String format){
		return baseAttachDao.findAttachByObject(objectId, flag, format);
	}

	public void delAttachById(String id){
		baseAttachDao.delAttachById(id);
	}

	public void delAttachByObjectId(String objectId){
		baseAttachDao.delAttachByObjectId(objectId);
	}
	
	public void delAttachByObject(String objectid,String flag){
		baseAttachDao.delAttachByObject(objectid, flag);
	}
	
	public void delAttachByObjectIdAndIds(String tableid,List<String> ids){
		baseAttachDao.delAttachByObjectIdAndIds(tableid, ids);
	}
	
	public void delAttachByObjectIdExclusiveIds(String tableid,List<String> ids){
		baseAttachDao.delAttachByObjectIdExclusiveIds(tableid, ids);
	}
	
	

}
