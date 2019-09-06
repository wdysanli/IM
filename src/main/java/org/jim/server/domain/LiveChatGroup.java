package org.jim.server.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@Table(name="livechat_group")
@Data
public class LiveChatGroup implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 组id;
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	  @Column(length = 32)
	private String shopId;
	
	/**
	 * 组名称
	 */
	@Column(name = "group_name")
	private String groupName;
	
	/**
	 * 组描述
	 */
	@Column(name = "group_describe")
	private String groupDescribe;
	
	/**
	 * 组状态  0. 待审核   1. 正常   2. 禁用
	 */
	@Column(name = "state")
	private Integer state;
	
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
    
//    private List<ImUser> groupMember;

}
