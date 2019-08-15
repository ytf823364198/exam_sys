package com.ziyue.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统邮件发送错误记录
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseMailSendError implements java.io.Serializable {
	private String msgid;
	private String error;//发送错误原因
	private String sendtime;//发送时间
}