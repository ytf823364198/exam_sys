package com.ziyue.service;

import java.util.List;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ziyue.dao.BaseMailDao;
import com.ziyue.entity.BaseMailMsg;
import com.ziyue.util.DateUtil;
import com.ziyue.util.FileLoad;
import com.ziyue.util.FileUtil;
import com.ziyue.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScheduledService {
	@Autowired
	private BaseLogService baseLogService;
	@Autowired
	private BaseMailDao baseMailDao;
	@Autowired
	JavaMailSenderImpl mailSender;
	
	/**
	 * 每5秒执行一次 。cron在线生成网站：http://www.itlookit.com/cron.html
	 */
	//@Scheduled(cron = "0/5 * * * * ? ")
	public void scheduled() {
		log.debug("--->"  + DateUtil.getDateLong() +"  " + DateUtil.fullTime());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//每天晚上4点0分0秒执行
	@Scheduled(cron="0 0 4 * * ?") 
	public void emptyTempFile(){
		String dir = FileLoad.FILE_ROOT +"attached/temp" ;
		//删除临时文件
		FileUtil.emptyDirFile(dir);
		log.debug("清空临时文件-->" + dir );
	}
	
	//每天晚上1点0分0秒执行
	@Scheduled(cron="0 0 1 * * ?")
	public void delUserLogOverDue(){
		//删除一个月以前的日志记录
		baseLogService.delUserLogOverDue();
		log.debug("删除一个月以前的日志记录" );
	}
	
	//邮件发送，每隔5分钟发送一次
	@Scheduled(cron = "0 0/5 * * * ?")
	public void sendMail() {
		//查待发的邮件
		List<BaseMailMsg> msgs = baseMailDao.findMailMsg(10, 60, 10);
		if(null != msgs && msgs.size() > 0) {
			for(BaseMailMsg msg : msgs) {
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper helper;
				try {
					helper = new MimeMessageHelper(mimeMessage,true);
					//有收件人的姓名，在正文拼接收件人
					if(StringUtil.notEmpty(msg.getTouser())) {
						msg.setMsg( 
							 "<p>" + msg.getTouser() +" , 您好 :</p>"
						   + "<p>" + msg.getMsg() +"</p>"
						);
					}
					//没有标题，自动生成标题
					if(StringUtil.isEmpty(msg.getSubject())) {
						msg.setSubject("您有新的信息，来自 " + mailSender.getUsername() +", "  + DateUtil.fullTime() );
					}
					//设置邮件标题
					helper.setSubject(msg.getSubject());
					//支持HTML格式的文本
					helper.setText(msg.getMsg(),true);
					//邮件发送地址
					helper.setFrom(mailSender.getUsername());
					//邮件到达地址
					helper.setTo(msg.getToemail());
					//发送邮件
					mailSender.send(mimeMessage);  
					//发送成功
					baseMailDao.modifyMailMsg(msg.getId(), "y");
				} catch (Exception e) {
					//发送失败
					baseMailDao.modifyMailMsg(msg.getId(), "e");
					//记录错误信息
					baseMailDao.addMailSendError(msg.getId(), e.getMessage(), DateUtil.fullTime());
					//e.printStackTrace();
				}
			}
		}
	}
	
}
