package com.zhanghui.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/*当我们进行开发项目时，我们经常会用到实体映射到数据库表的操作，此时我们经常会发现在我们需要隐射的几个实体类中，有几个共同的属性，例如编号ID，创建者，创建时间，修改者，修改时间，备注等。遇到这种情况，我们可能会想到把这些属性抽象出来当成一个父类，然后再以不同的实体类来继承这个父类。

那么，我们便可以使用@MappedSuperclass注解，通过这个注解，我们可以将该实体类当成基类实体，它不会隐射到数据库表，但继承它的子类实体在隐射时会自动扫描该基类实体的隐射属性，添加到子类实体的对应数据库表中。

使用环境：
1.@MappedSuperclass注解使用在父类上面，是用来标识父类的

2.@MappedSuperclass标识的类表示其不能映射到数据库表，因为其不是一个完整的实体类，但是它所拥有的属性能够隐射在其子类对用的数据库表中

3.@MappedSuperclass标识得嘞不能再有@Entity或@Table注解

 * */
@MappedSuperclass //这个注解 表示这个类为其他所有实体类的基类 
@Data
public class BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3599764667156102501L;
	
	/**状态有效*/
	public static final int FLAG_VALID = 1;
	/**状态无效*/
	public static final int FLAG_INVALID = 2;
	
	/**未删除*/
	public static final int DEL_N = 1;
	/**已删除*/
	public static final int DEL_Y = 2;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	protected Integer flag;
	protected Integer del;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	protected Date operTime;
	protected Long opertorId;
	protected String opertorName;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	protected Date createTime;
	protected Long creatorId;
	protected String creatorName;
	@Transient
    private Integer page = 1;

    @Transient
    private Integer rows = 10;
}
