package com.ziyue.entity;

import java.beans.Transient;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MenuTree {
	private String id;
	private String name;
	private Date createtime;
	private String remark;
	private String type;
	private String pid;
    private List<MenuTree> children = new ArrayList<>();
    public MenuTree(String id, String pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

}
