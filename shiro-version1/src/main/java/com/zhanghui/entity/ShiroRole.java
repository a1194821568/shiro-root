package com.zhanghui.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Table;



import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Table(name = "shiro_role")
@Data
@EqualsAndHashCode(callSuper = true)
public class ShiroRole extends BaseEntity implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1811833383304726552L;
	private String roleName;
	private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:
	@Transient
	private List<ShiroMenuPermissionRole> list;

}
