package com.zhanghui.entity;

import java.io.Serializable;

import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Table(name = "shiro_menu_permission_role")
@Data
@EqualsAndHashCode(callSuper = true)
public class ShiroMenuPermissionRole extends BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3337875417290551490L;
	
	private Long roleId;
	private Long menuId;

}
