package com.zhanghui.entity;

import java.io.Serializable;

import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Table(name = "shiro_user_role")
@Data
@EqualsAndHashCode(callSuper = true)
public class ShiroUserRole extends BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7794842404231947790L;
	

	private Long userId;
	private Long roleId;
}
