package org.jim.server.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name="group_config")
@Data
public class GroupConfig implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 组id;
	 */
	@Id
	@Column(name = "group_id")
	private Long groupId;
	
	/**
	 * 组规模
	 */
	@Column(name = "scale")
	private Integer scale;
	
	/**
	 * 组类型
	 */
	@Column(name = "group_type")
	private String groupType;
	
	/**
	 * 组管理规模
	 */
	@Column(name = "privilege")
	private Integer privilege;
	
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

	public static GroupConfig getInitGroupConfig(ChatGroup group) {
		GroupConfig result = new GroupConfig();
		result.setGroupId(group.getId());
		result.setGroupType("普通群组");
		result.setPrivilege(5);
		result.setScale(100);
		return result;
	}
    
}
