package com.ziyue.entity;

import java.io.Serializable;
import java.util.Date;

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
public class ExampleGoods implements Serializable {
	private String	id;
	private String	code;
	private String	name;
	private String	brand;
	private String	model;
	private String	spec;
	private String	unit;
	private Integer	quantity;
	private Double	price;
	private Double	total;
	private Date	productdate;
	private String	norm;
	private String range;
	
	
}
