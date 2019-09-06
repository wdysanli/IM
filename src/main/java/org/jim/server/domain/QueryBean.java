package org.jim.server.domain;

import java.util.Date;

import lombok.Data;

@Data
public class QueryBean {
	
	private int page; // 当前页数
	
	private int size; //每页记录数
    
	private String mobile;//用户手机号
    
	private String user_name; //用户名
	
	private String shopId;// 商户id
	
	private String shopName; //商户名
	
	private String shopNick; //商户昵称
	
	private String userName; // 客服名称
	
	private String userNick;// 客服昵称
	
	
	private String managerId;// 管理员id
	
	private String managerName;// 管理员名称
	
	private String managerNick;// 管理员昵称
	
}
