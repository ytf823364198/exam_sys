package com.ziyue.entity;

import com.ziyue.util.DateUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统附件
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseAttach implements java.io.Serializable{
	private String  id;
	private String  path;//路径
	private String  name;//文件名
	private String  format;//后缀名（小写）
	private Long    length;//大小
	private String  filesize;//如3.3M,30KB
	private String  flag;//业务分类标志
	private String  objectid;//关联业务id
	private String  md5;//md5编码
	private String  uploadtime;//文件上传时间
	

	public BaseAttach(String path, String name, String format, Long length) {
		super();
		this.path = path;
		this.name = name;
		this.format = format;
		setLength(length);
		this.uploadtime = DateUtil.fullTime();
	}

	
}
