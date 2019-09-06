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
@Table(name="livechat_config")
@Data
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class LivechatConfig implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5379144505587472581L;

	@Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
	private String id;
	
	@Column(name = "shop_id")
	private String shopId;  // 商户id
	
	@Column(name = "polling_type")
	private Integer pollingType; //轮询类型 0 顺序轮询   1加权轮询  
	
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
