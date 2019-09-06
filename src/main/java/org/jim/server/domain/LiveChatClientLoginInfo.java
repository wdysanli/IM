package org.jim.server.domain;

import java.util.Date;

import lombok.Data;

@Data
public class LiveChatClientLoginInfo {
	private String uid;
	private String shopid;
	private String username;
	private Integer type;
	private String sign;
	
}
