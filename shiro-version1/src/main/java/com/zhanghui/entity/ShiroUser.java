package com.zhanghui.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
/*
 * 当使用@Data注解的实体有父类（非Object）时，eclipse会有警告提示 
 
大概意思时自动生成的equals和hashCode方法没有调用父类，如果是故意不掉用父类的话，可以加上@EqualsAndHashCode(callSuper=false)注解。 
因为这个是实体，继承父类主要也是需要使用父类的元素，所以生成的equals和hashCode方法应该包含父类在内，因此可以把callSuper设为true。 
*/
@Table(name = "shiro_user")
@Data//另：@Data相当于@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode这5个注解的合集。
@EqualsAndHashCode(callSuper=true)
public class ShiroUser extends BaseEntity implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6893706057622586080L;
	private String userName;
	private String loginName;
	
	private String password;
	private String tel;
	private String address;
	
	private String idCard;
	private Integer sex;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	private Date birthday;
	private Integer age;
	private String eMail;
	@Transient
	private List<ShiroUserRole> list;
	
	private String salt;
	  /**
     * 密码盐.
     * @return
     */
    public String getCredentialsSalt(){
        return this.loginName+this.salt;
    }
    //重新对盐重新进行了定义，用户名+salt，这样就更加不容易被破解
}
