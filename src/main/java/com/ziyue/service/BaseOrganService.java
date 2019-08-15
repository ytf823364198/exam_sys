package com.ziyue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ziyue.dao.BaseOrganDao;
import com.ziyue.entity.BaseOrgan;
import com.ziyue.util.BaseDao;

import lombok.extern.slf4j.Slf4j;

@CacheConfig(cacheNames="BaseOrgan")
@Service
@Slf4j
public class  BaseOrganService extends BaseDao{
	@Autowired
	private BaseOrganDao  baseOrganDao;
	
	@CacheEvict(allEntries = true)
	public void addOrgan(BaseOrgan organ){
		log.debug("addOrgan");
		baseOrganDao.addOrgan(organ);
	}
	
	@CacheEvict(allEntries = true)
	public String delOrganById(String id){
		log.debug("delOrganById");
		//先查是否包含小部门
		if(baseOrganDao.countOrgansByPid(id) > 0 ){
			return "isPorgan";
		}	
		baseOrganDao.delOrganById(id);
		return "ok";
	}
	
	@CacheEvict(allEntries = true)
	public void modifyOrgan(BaseOrgan organ){
		log.debug("modifyOrgan");
		baseOrganDao.modifyOrgan(organ);
	}
	
	@Cacheable(key="#root.methodName+'['+ #id +']'")
	public BaseOrgan findOrganById(String id){
		log.debug("findOrganById");
		return baseOrganDao.findOrganById(id);
	}
	
	@Cacheable(key="#root.methodName+'['+ #id +']'")
	public String findOrganNameById(String id){
		log.debug("findOrganNameById");
		return baseOrganDao.findOrganNameById(id);
	}
	
	@Cacheable(key="#root.methodName")
	public List<BaseOrgan> findOrgans(){
		log.debug("findOrgans");
		return baseOrganDao.findOrgans();
	}
	
	@Cacheable(key="#root.methodName")
	public List<BaseOrgan> findPOrgans(){
		log.debug("findPOrgans");
		return baseOrganDao.findPOrgans();
	}
	
	@Cacheable(key="#root.methodName+'['+ #pid +']'")
	public List<BaseOrgan> findOrgansByPid(String pid){
		return baseOrganDao.findOrgansByPid(pid);
	}
	
	public int countOrgansByPid(String pid){//查下属部门数量
		return baseOrganDao.countOrgansByPid(pid);
	}
	
	public int findCodeIsExit(String code,String excludeId){
		return baseOrganDao.findCodeIsExit(code, excludeId);
	}

}
