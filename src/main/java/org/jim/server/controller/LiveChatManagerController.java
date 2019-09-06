package org.jim.server.controller;

import java.util.Map;

import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.domain.LiveChatManager;
import org.jim.server.domain.LiveChatShop;
import org.jim.server.domain.QueryBean;
import org.jim.server.domain.QueryResult;
import org.jim.server.service.LiveChatManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/livechatmanager")
@Api(value = "客服系统：管理员",tags = {"客服系统：管理员"})
public class LiveChatManagerController extends BaseController{

	
	@Autowired
	private LiveChatManagerService liveChatManagerService;
	
	/*
	 * 管理员注册
	 */
	@PostMapping("/register")
	@ApiOperation("管理员注册")
	public Map<String, Object> managerRegister(@RequestBody LiveChatManager liveChatManager){
		if (liveChatManager == null 
				|| liveChatManager.getManagerName() == null
				|| liveChatManager.getPassword() == null
				|| liveChatManager.getManagerNick() == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int  result = liveChatManagerService.managerRegister(liveChatManager);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
		}
		return returnResultMap(ResultMapInfo.ADDFAIL);
	}
	
	/*
	 * 管理员删除
	 */
	@GetMapping("/delete")
	@ApiOperation("删除管理员")
	public Map<String, Object> managerDelete(@RequestParam String managerId){
		if (managerId == null || managerId == "") {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int result = liveChatManagerService.managerDelete(managerId);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.DELETESUCCESS);
		}
		return returnResultMap(ResultMapInfo.DELETEFAIL);
	}
	
	/*
	 * 管理员修改
	 */
	@PostMapping("/update")
	@ApiOperation("修改管理员")
	public Map<String, Object> managerUpdate(@RequestBody LiveChatManager liveChatManager){
		if (liveChatManager == null || liveChatManager.getId()==null ) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int  result = liveChatManagerService.managerUpdate(liveChatManager);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.EDITSUCCESS);
		}
		return returnResultMap(ResultMapInfo.EDITFAIL);
	}
	
	/*
	 * 管理员列表
	 */
	@PostMapping("/findmanagerlist")
	@ApiOperation("获取管理员列表")
	public Map<String, Object> findManagerList(@RequestBody QueryBean queryBean){
		if (queryBean == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		QueryResult<LiveChatManager> list = liveChatManagerService.findManagerList(queryBean);
		
		return returnResultMap(ResultMapInfo.GETSUCCESS,list);
	}
	
	/*
	 * 获取管理员信息
	 */
	@GetMapping("/getonemanager")
	@ApiOperation("获取单个管理员信息")
	public Map<String, Object> getOneManager(@RequestParam String managerId){
		if (managerId == null || managerId == "") {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		LiveChatManager result = liveChatManagerService.getOneManager(managerId);
		if (result!=null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS,result);
		}
		return returnResultMap(ResultMapInfo.GETFAIL);
	}
	
	
	/*
	 * 管理员登入
	 */
	@GetMapping("/login")
	@ApiOperation("管理员登入")
	public Map<String, Object> login(String managerName,String password){
		if (managerName == null || password == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		LiveChatManager result = liveChatManagerService.login(managerName,password);
		if (result!=null) {
			return returnResultMap(ResultMapInfo.LOGINSUCCESS,result);
		}
		return returnResultMap(ResultMapInfo.LOGINFAIL);
	}
	
}
