package com.ziyue.entity;

import java.io.Serializable;
import java.util.Date;
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
public class ExampleEnter implements Serializable {
	private String id;
	private String code;
	private String applyuser;
	private Date   applydate;
	private String model;
	private String organname;
	private String supplier;
	private String invoice;
	private String buyer;
	private String useer;
	private String leader;
	private Double total;
	
	private List<ExampleGoods> goodses;

}
