package com.ziyue.test;
import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailApplicationTests {
	@Autowired
	JavaMailSenderImpl mailSender;
	
	/*
	 * 发普通邮件
	 */
	@Test
	public void contextLoads() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject("邮件标题");
		message.setText("邮件正文");
		message.setFrom("312384364@qq.com");
		message.setTo("huyq_cq@163.com");
		mailSender.send(message);
	}
	/**
	 * 发HTML格式的内容，带附件发送
	 */
	@Test
	public void mimeMail() {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage,true);
			helper.setSubject("subject");
			helper.setText("<b style='color:red'>text<b>",true);
			helper.setFrom("312384364@qq.com");
			helper.setTo("huyq_cq@163.com");
			helper.addAttachment("ziyue.jpg", new File("D:\\ziyue.jpg"));
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
}
