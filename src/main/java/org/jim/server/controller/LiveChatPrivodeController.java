package org.jim.server.controller;

import java.util.Map;

import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.domain.LiveChatPrivode;
import org.jim.server.domain.LiveChatShop;
import org.jim.server.domain.QueryBean;
import org.jim.server.domain.QueryResult;
import org.jim.server.service.LiveChatPrivodeService;
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
@RequestMapping("/livechatprivode")
@Api(value = "客服系统：客服管理",tags = {"客服系统：客服管理"})
public class LiveChatPrivodeController extends BaseController{
	@Autowired
	private LiveChatPrivodeService liveChatPrivodeService;
	/*
	 *客服登入
	 */
	@PostMapping("/login")
	@ApiOperation("客服登入")
	public Map<String, Object> privodeLogin(@RequestBody LiveChatPrivode liveChatPrivode){
		if (liveChatPrivode.getUserName() == null 
				|| liveChatPrivode.getPassword() == null
				||liveChatPrivode.getShopId() == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		String token = liveChatPrivodeService.privodeLogin(liveChatPrivode);
		if (token != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS,token);
		}
		return returnResultMap(ResultMapInfo.GETSUCCESS);
	}
	
	/*
	 * 客服退出登入
	 */
	@GetMapping("/logout")
	@ApiOperation("客服退出登入")
	public Map<String, Object> privodeLogout(@RequestParam("id")String id){
		if (id == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int  result = liveChatPrivodeService.privodeLogout(id);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.QUITSUCCESS);
		}else if (result == 2) {
			return returnResultMap(ResultMapInfo.EXITED);
		}
		return returnResultMap(ResultMapInfo.QUITFAIL);
	}
	
	
	/*
	 * 客服注册
	 */
	@PostMapping("/register")
	@ApiOperation("客服注册--生成客服")
	public Map<String, Object> privodeRegister(@RequestBody LiveChatPrivode liveChatPrivode){
		if (liveChatPrivode.getUserName() == null || liveChatPrivode.getPassword() == null || liveChatPrivode.getShopId() == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int  result = liveChatPrivodeService.privodeRegister(liveChatPrivode);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
		}
		return returnResultMap(ResultMapInfo.ADDFAIL);
	}
	
	
	/**
	 * 删除客服
	 * @param privodeId 客服id
	 * @return
	 */
	@GetMapping("/delete")
	@ApiOperation("删除客服")
	public Map<String, Object> privodeDelete(@RequestParam String privodeId){
		if (privodeId == null || privodeId == "") {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int result = liveChatPrivodeService.privodeDelete(privodeId);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.DELETESUCCESS);
		}
		return returnResultMap(ResultMapInfo.DELETEFAIL);
	}
	
	
	/**
	 * 获取客服列表
	 * @param queryBean 查询条件
	 * @return
	 */
	@PostMapping("/findprivodelist")
	@ApiOperation("获取客服列表")
	public Map<String, Object> findPrivodeList(@RequestBody QueryBean queryBean){
		if (queryBean == null || queryBean.getShopId() == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		QueryResult<LiveChatPrivode> list = liveChatPrivodeService.findPrivodeList(queryBean);
		
		return returnResultMap(ResultMapInfo.GETSUCCESS,list);
	}
	
	/**
	 * 客服修改
	 * @param liveChatPrivode 客服实体
	 * @return
	 */
	@PostMapping("/update")
	@ApiOperation("客服修改")
	public Map<String, Object> privodeUpdate(@RequestBody LiveChatPrivode liveChatPrivode){
		if (liveChatPrivode == null || liveChatPrivode.getId()==null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int  result = liveChatPrivodeService.privodeUpdate(liveChatPrivode);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.EDITSUCCESS);
		}
		return returnResultMap(ResultMapInfo.EDITFAIL);
	}
	
	/**获取单个客服信息
	 * 
	 * @param privodeId 客服id
	 * @return
	 */
	@GetMapping("/getoneprivode")
	@ApiOperation("获取单个客服信息")
	public Map<String, Object> getOnePrivode(@RequestParam String privodeId){
		if (privodeId == null || privodeId == "") {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		LiveChatPrivode result = liveChatPrivodeService.getOnePrivode(privodeId);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS,result);
		}
		return returnResultMap(ResultMapInfo.GETFAIL);
	}
	
}
