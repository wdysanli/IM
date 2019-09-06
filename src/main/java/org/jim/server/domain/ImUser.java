package org.jim.server.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.jim.common.packets.User;

import lombok.Data;

/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年7月26日 下午3:13:47
 */
@Entity
@Table(name="im_user")
@Data
public class ImUser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 用户id;
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * imID
	 */
	@Column(name = "im_id")
	private String imId;
	
	/**
	 * 用户名
	 */
	@Column(name = "user_name")
	private String userName;
	
	
	/**
	 * 用户昵称
	 */
	
	@Column(name = "user_nick")
	private String userNick;
	
	/**
	 * 用户密码
	 */
	@Column(name = "password")
	private String password;
	
	/**
	 * 用户手机号
	 */
	@Column(name = "mobile")
	private String mobile;
	
	/**
	 * 用户邮箱
	 */
	@Column(name = "email")
	private String email;
	
	/**
	 * 用户性别
	 */
	@Column(name = "gender")
	private Integer gender;
	
	/**
	 * 用户年龄
	 */
	@Column(name = "age")
	private Integer age;
	
	/**
	 * 个性签名;
	 */
	@Column(name = "sign")
	private String sign;
	
	
	
	
    @Column(name = "delete_flag")
    private Integer deleteFlag = 0;//删除标记，0，不删，1已删

    @Column(name = "create_time", columnDefinition = "timestamp", insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;//创建时间

    @Column(name = "update_time", columnDefinition = "timestamp", insertable = false, updatable = false)
    @Generated(GenerationTime.ALWAYS)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;//更新时间
	
	public User getUser() {
		User result = new User();
		result.setId(imId);
		result.setNick(userNick);
		result.setSign(sign);
		return result;
	}

	public GroupMember getGroupMember(ChatGroup group) {
		GroupMember result = new GroupMember();
		result.setGroupId(group.getId());
		result.setUserId(id);
		return result;
	}
	
	
	public LiveChatGroupMember getLiveChatGroupMember(LiveChatGroup group) {
		LiveChatGroupMember result = new LiveChatGroupMember();
		result.setGroupId(group.getId());
		result.setUserId(id);
		return result;
	}
}
