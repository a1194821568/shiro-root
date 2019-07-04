package com.zhanghui.entity;

import java.io.Serializable;

import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Table(name = "shiro_menu")
@Data
@EqualsAndHashCode(callSuper = true)
public class ShiroMenu extends BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8785630126510036723L;
	private String menuName;
	private String menuUrl;
	private Long parentId;
	private Integer menuSort;
	private String permission;
	private Integer menuType;
	

}
