package com.ziyue.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.ziyue.dao.BaseMailDao;
import com.ziyue.util.BaseDao;

import com.ziyue.util.DBProductUtil;
import com.ziyue.util.DateUtil;
import com.ziyue.util.StringUtil;
import com.ziyue.entity.BaseMailMsg;
import com.ziyue.entity.BaseMailSendError;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
public class BaseMailDao extends BaseDao  {
	
	public void addMailMsg(BaseMailMsg msg) {
			String sql = "insert into base_mail_msg( id,toemail,touser,subject,msg,addtime,trytimes,status)";
			Object[]args = {StringUtil.UUID(),msg.getToemail(),msg.getTouser(),msg.getSubject(),msg.getMsg(),msg.getAddtime(),0,"n"};
			sql = sql + StringUtil.sqlField(args.length);
			jdbcTemplate.update(sql, args);		
	}

	public void delMailMsgById(String id) {
		String sql = "delete from base_mail_msg where id = ? ";
	    jdbcTemplate.update(sql,new Object[]{id});

	}

	public void delMailSendErrorByMsgId(String msgid) {
		String sql = "delete from base_mail_Send_Error where msgid=? ";
	    jdbcTemplate.update(sql,new Object[]{msgid});
	}

	public void modifyMailMsg(String id,String status) {
		String sql = "update base_mail_msg set trytimes = trytimes + 1 ,lasttime =?, status =?   where id = ? ";
        jdbcTemplate.update(sql,new Object[]{DateUtil.fullTime(),status,id});

	}
	
	public void addMailSendError(BaseMailSendError error) {
		String sql = "insert into base_mail_send_error( msgid , error,sendtime) values(?,?,?)";
		jdbcTemplate.update(sql, new Object[] {error.getMsgid(),error.getError(),error.getSendtime()});		
	}
	
	public void addMailSendError(String msgid,String error,String sendtime) {
		String sql = "insert into base_mail_send_error( msgid , error,sendtime) values(?,?,?)";
		jdbcTemplate.update(sql, new Object[] {msgid,error,sendtime});		
	}

	/**
	 * 查待发送的邮件
	 * @param maxtry 最大尝试次数
	 * @param delayMinute 错误后延迟多少（分）再发
	 * @param limit  选择待发送邮件数
	 * @return
	 */
	public List<BaseMailMsg> findMailMsg(Integer maxtry, Integer delayMinute , Integer limit) {
		try{
			String dbProductName = DBProductUtil.getInstance(jdbcTemplate).getProductName();
			String sql = "";
			if("Oracle".equals(dbProductName) ){
				sql="select * from  base_mail_msg  where  ( trytimes = 0  or   ( trytimes > 0 and  trytimes < ? and  to_char(  to_date(lasttime ,'yyyy-MM-dd HH24:mi:ss') + ?/(24*60) ,'yyyy-MM-dd HH24:mi:ss'  )  <= ?   )  ) and status <> 'y' order by trytimes ";
				sql = "select * from ( " + sql + ") where rownum <= ?";
			}else if("MySQL".equals(dbProductName)){
				sql="select * from  base_mail_msg  where  ( trytimes = 0  or   ( trytimes > 0 and  trytimes < ? and  DATE_ADD( STR_TO_DATE (lasttime, '%Y-%m-%d %H:%i:%s') , INTERVAL ? MINUTE) <= ?   )  ) and status != 'y' order by trytimes ";
				sql = sql +" limit 0,?" ;
			}
			return (List<BaseMailMsg>) jdbcTemplate.query(sql,new Object[]{maxtry,delayMinute,DateUtil.fullTime(),limit}, new BeanPropertyRowMapper(BaseMailMsg.class));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
