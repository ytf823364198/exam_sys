package com.ziyue.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ziyue.entity.BaseInspect;
import com.ziyue.entity.BaseUser;
import com.ziyue.util.BaseDao;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public  class ActivitiDao extends BaseDao  {
	/**
	 * 查每个版本部署的最新流程
	 * @return
	 */
    public List findLastVersionProcdefs(){
    	try{
	    	String sql = 
	    			"select def.key_,def.version_,def.dgrm_resource_name_ ,def.deployment_id_,dep.deploy_time_,dep.name_ "+
	    			"from act_re_procdef def left join act_re_deployment dep on dep.id_=def.deployment_id_ "+
	    			"where def.id_ in (select max(id_) from act_re_procdef  group by key_)";
			return jdbcTemplate.queryForList(sql);
    	}catch(Exception e){
    		return null;
    	}
    }
    
    /**
     * 查流程实例中，任务节点的待办人
     * @param procInstId  流程实例
     * @param taskDefKey 任务节点
     * @return 待办人
     */
	public List<BaseUser> findAssigneeByDefKeyProcInstId(String procInstId,String taskDefKey) {
		try{
			String sql = "select distinct  u.realname,ASSIGNEE as username from v_act_task t  left join base_user u on u.id = t.assignee  " +
						 "where PROC_INST_ID_ = ? and TASK_DEF_KEY_ =?";
			return jdbcTemplate.query(sql,new Object[]{procInstId,taskDefKey},new BeanPropertyRowMapper(BaseUser.class));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 通过流程实例，待办任务信息
	 * @param procInstId 流程实例
	 * @return 当前的待办任务信息
	 */
	public List<BaseInspect> findNextInspectByProcInstId(String procInstId){
		try{
			String sql ="select t.NAME_ as taskname,  t.TASK_DEF_KEY_ as flag, t.assignee as userid, u.realname  " +
						"from v_act_task t " +
						"left join base_user u on u.id = t.assignee  " +
						"where t.PROC_INST_ID_=? ";
			return jdbcTemplate.query(sql,new Object[]{procInstId},new BeanPropertyRowMapper(BaseInspect.class) );
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	

}
