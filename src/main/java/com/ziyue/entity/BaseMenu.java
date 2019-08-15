package com.ziyue.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统菜单  
 * @author 胡永强
 *
 */
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class BaseMenu implements java.io.Serializable {
	
	private String id;
	private String pid;//上级菜单ID
	private String name;//菜单名称
	private String url;//访问路径
	private String murl;//手机端访问路径
	private String type;//类别{ menu菜单, button按钮,res其他资源}  
	private String permcode;//权限代码
	private String status;//是否可用 y可用，n禁用
	private Integer sortno;//排序号
	private String ico;//菜单图标
	private String hasnode;//是否有子菜单 {y：有,n否}
	private String checked;//是否选中 {y：是,n否}

	public String getHasnode() {
		return hasnode == null ? "n": hasnode;
	}

	public BaseMenu(String id) {
		super();
		this.id = id;
	}
	

}