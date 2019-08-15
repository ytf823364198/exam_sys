package com.ziyue.create;
import org.junit.Test;

import com.ziyue.autocode.main.AutoCode;
import com.ziyue.autocode.main.AutoDoc;
public class AutoCodeTest {
	
	/**
	 * 创建代码
	 * 请先确定已配置	src/main/resources/application.properties
	 * 需要配备工作流的模块,请在表中加入字段 PROCINSTID varchar2(32),--流程实例ID
	 * 注意：如果代码已经存在，则不创建代码
	 */
	@Test
	public void autoCode() {
		
		String tableName = "EX_LEAVE";//表名
		String classNameShort = "lea";//类名简写
		String htmlDir = "lea"; // HTML放置路径(首尾不能加“/”)
		
		AutoCode ac = new AutoCode(tableName, classNameShort, htmlDir);
		ac.createAll();  //创建Java代码和HTML页面代码
		
		//ac.createJava(); //只创建Java代码（包含Entity、Dao、Service、Controller）
		//ac.createJavaEntity(); //创建Entity层代码
		//ac.createJavaDao(); //创建Dao层代码
		//ac.createJavaService(); //创建Service层代码
		//ac.createJavaController(); //创建Controller层代码
		
		//ac.createHtml(); //只创建HTML代码 （包含add、modify、detail、doTask、list页面）
	}

	
	/**
	 * 创建数据库文档
	 * 请先确定已配置 	src/main/resources/application.properties
	 * 生成文件路径	src/test/db_doc.html,用IE打开后复制到word
	 */
	@Test
	public void autoDBDoc() {
		AutoDoc.createDataBaseDoc();
	}
}
