package com.ziyue.entity;

import com.ziyue.util.DateUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * 系统要发送的邮件
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseMailMsg implements java.io.Serializable {
	private String  id;
	private String  toemail; //接受地址
	private String  touser; //收件人姓名
	private String  subject; //邮件标题
	private String  msg; //正文内容
	private String  addtime; //添加时间
	private String  lasttime; //最后一次发送时间
	private Integer trytimes; //尝试次数
	private String  status; //正在发送   (n未发送  e发送失败 y已发送)
	
	public BaseMailMsg(String toemail,String touser, String subject , String msg) {
		super();
		this.toemail = toemail;
		this.touser = touser;
		this.subject = subject;
		this.msg = msg;
		this.addtime = DateUtil.fullTime();
		this.trytimes = 0;
		this.status = "n";
	}
	
	public BaseMailMsg(String toemail,String touser, String msg) {
		super();
		this.toemail = toemail;
		this.touser = touser;
		this.msg = msg;
		this.addtime = DateUtil.fullTime();
		this.trytimes = 0;
		this.status = "n";
	}
	
	public BaseMailMsg(String toemail, String msg) {
		super();
		this.toemail = toemail;
		this.msg = msg;
		this.addtime = DateUtil.fullTime();
		this.trytimes = 0;
		this.status = "n";
	}
	
	
}