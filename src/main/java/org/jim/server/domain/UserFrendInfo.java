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
@Table(name="user_frend_info")
@Data
public class UserFrendInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * id;
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;
	
	/**
	 * 好友组
	 */
	@Column(name = "frend_group")
	private String frendGroup;
	
	
	/**
	 * 好友组
	 */
	@Column(name = "frend_user_id")
	private Long frendUserId;
	
	
	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;
	
	/**
	 * 逻辑删除
	 */
	@Column(name = "delete_flag")
	private Integer deleteFlag;
	
    @Column(name = "create_time", columnDefinition = "timestamp", insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;//创建时间

    @Column(name = "update_time", columnDefinition = "timestamp", insertable = false, updatable = false)
    @Generated(GenerationTime.ALWAYS)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;//更新时间

}
