package org.jim.server.base.bo;

import org.jim.server.common.enums.ResultMapInfo;

import lombok.Data;

@Data
public class ResultContent {
	
	private int code;
	
	private Object data;
	
	private String messasge;
	
	public ResultContent(ResultMapInfo info) {
		this.code = info.getCode();
		this.messasge = info.getMessage();
		this.data = "";
	}
	
	public ResultContent(ResultMapInfo info, Object data) {
		this.code = info.getCode();
		this.messasge = info.getMessage();
		this.data = data;
	}
}
