package org.jim.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jim.common.packets.UserMessageData;
import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.domain.ImUser;
import org.jim.server.domain.LiveChatManager;
import org.jim.server.domain.LiveChatPrivode;
import org.jim.server.domain.LiveChatShop;
import org.jim.server.domain.QueryResult;
import org.springframework.data.domain.Page;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class BaseController {
	
	public static HttpSession getSession() {
		return getRequest().getSession();
	}
	
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
		.getRequestAttributes();
		return attrs.getRequest();
	}
	
	public static HttpServletResponse getResponse() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
		.getRequestAttributes();
		return attrs.getResponse();
	}
	
	  /**
     * 获取登陆IP
     * @param request
     * @param response
     * @return 
     */
    public static String getIpAddr() {
    	HttpServletRequest request = getRequest();
    	//处理代理访问获取不到真正的ip问题的        
    	String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	//获取代理中中的ip
            ip = request.getHeader("PRoxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	//获取代理中中的ip

            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	//非代理的情况获取ip
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
        	ip = "127.0.0.1";
        }
        return ip;
    }
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		resultMap.put("data", null);
		return resultMap;
	}
	

	
	
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, Page<?> data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	

protected Map<String, Object> returnResultMap(ResultMapInfo info, UserMessageData userMessageData) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", userMessageData);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}

protected Map<String, Object> returnResultMap(ResultMapInfo info, LiveChatManager liveChatManager) {
	Map<String, Object> resultMap = new HashMap<String, Object>();
	resultMap.put("code", info.getCode());
	resultMap.put("data", liveChatManager);
	resultMap.put("message", info.getMessage());
	if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
	else resultMap.put("status", "FALT");
	return resultMap;
}

protected Map<String, Object> returnResultMap(ResultMapInfo info, LiveChatShop liveChatShop) {
	Map<String, Object> resultMap = new HashMap<String, Object>();
	resultMap.put("code", info.getCode());
	resultMap.put("data", liveChatShop);
	resultMap.put("message", info.getMessage());
	if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
	else resultMap.put("status", "FALT");
	return resultMap;
}

protected Map<String, Object> returnResultMap(ResultMapInfo info, LiveChatPrivode liveChatPrivode) {
	Map<String, Object> resultMap = new HashMap<String, Object>();
	resultMap.put("code", info.getCode());
	resultMap.put("data", liveChatPrivode);
	resultMap.put("message", info.getMessage());
	if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
	else resultMap.put("status", "FALT");
	return resultMap;
}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, List<?> data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	protected Map<String, Object> returnResultMap(ResultMapInfo info, QueryResult<?> data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, Map<String, Object> data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	
	protected Map<String, Object> returnResultMap(ResultMapInfo info, String data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", info.getCode());
		resultMap.put("data", data);
		resultMap.put("message", info.getMessage());
		if (info.getCode() == 1) resultMap.put("status", "SUCCESS");
		else resultMap.put("status", "FALT");
		return resultMap;
	}
	
	
	 
	
	

	
	
	
	

}

