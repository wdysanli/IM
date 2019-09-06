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

import lombok.Data;

/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年7月26日 下午3:13:47
 */
@Entity
@Table(name="livechat_group_member")
@Data
public class LiveChatGroupMember implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * id;
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 组id
	 */
	@Column(name = "group_id")
	private Long groupId;
	
	/**
	 * 组成员ID
	 */
	@Column(name = "user_id")
	private Long userId;
	
	/**
	 * 成员等级
	 */
	@Column(name = "jurisdiction")
	private Integer jurisdiction;
	
	/**
	 * 成员状态
	 */
	@Column(name = "state")
	private Integer state;
	
    @Column(name = "create_time", columnDefinition = "timestamp", insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;//创建时间

    @Column(name = "update_time", columnDefinition = "timestamp", insertable = false, updatable = false)
    @Generated(GenerationTime.ALWAYS)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;//更新时间

    
}
