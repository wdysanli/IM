package org.jim.server.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Table(name="livechat_record")
@Data
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class LiveChatRecord implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7055111808247144065L;

	/**
	 * 记录id;
	 */
	@Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
	private String id;
	
	
	@Column(name = "client_id")
	private String clientId;
	
	@Column(name = "clientim_id")
	private String clientimId;
	
	@Column(name = "uid")
	private String uid;
	
	@Column(name = "shop_id")
	private String shopId;
	
	@Column(name = "client_name")
	private String clientName;
	
	@Column(name = "privode_id")
	private String privodeId;
	
	@Column(name = "privideim_id")
	private String privideimId;
	
	@Column(name = "privode_name")
	private String privodeName;
	
	@Column(name = "type")
    private Integer type;
	
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
}
