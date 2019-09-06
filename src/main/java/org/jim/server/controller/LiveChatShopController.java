package org.jim.server.controller;

import java.util.Map;

import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.domain.ImUser;
import org.jim.server.domain.LiveChatPrivode;
import org.jim.server.domain.LiveChatShop;
import org.jim.server.domain.QueryBean;
import org.jim.server.domain.QueryResult;
import org.jim.server.service.LiveChatShopService;
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
@RequestMapping("/livechatshop")
@Api(value = "客服系统：商户管理",tags = {"客服系统：商户管理"})
public class LiveChatShopController extends BaseController{
	
	@Autowired
	private LiveChatShopService liveChatShopService;
	
	
	/*
	 * 商户注册
	 */
	@PostMapping("/register")
	@ApiOperation("商户注册")
	public Map<String, Object> shopRegister(@RequestBody LiveChatShop liveChatShop){
		if (liveChatShop == null || liveChatShop.getShopName()==null 
				|| liveChatShop.getPassword()==null || liveChatShop.getShopNick()==null || liveChatShop.getManagerId()==null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int  result = liveChatShopService.shopRegister(liveChatShop);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
		}
		return returnResultMap(ResultMapInfo.ADDFAIL);
	}
	
	/*
	 * 商户删除
	 */
	@GetMapping("/delete")
	@ApiOperation("商户删除")
	public Map<String, Object> shopDelete(@RequestParam String shopId){
		if (shopId == null || shopId == "") {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int result = liveChatShopService.shopDelete(shopId);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.DELETESUCCESS);
		}
		return returnResultMap(ResultMapInfo.DELETEFAIL);
	}
	
	/*
	 * 商户修改
	 */
	@PostMapping("/update")
	@ApiOperation("商户修改")
	public Map<String, Object> shopUpdate(@RequestBody LiveChatShop liveChatShop){
		if (liveChatShop == null || liveChatShop.getId()==null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int  result = liveChatShopService.shopUpdate(liveChatShop);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.EDITSUCCESS);
		}
		return returnResultMap(ResultMapInfo.EDITFAIL);
	}
	
	/*
	 * 商户列表
	 */
	@PostMapping("/findshoplist")
	@ApiOperation("商户列表")
	public Map<String, Object> findShopList(@RequestBody QueryBean queryBean){
		if (queryBean == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		QueryResult<LiveChatShop> list = liveChatShopService.findShopList(queryBean);
		
		return returnResultMap(ResultMapInfo.GETSUCCESS,list);
	}
	
	
	/*
	 * 获取单个商户
	 */
	@GetMapping("/getoneshop")
	@ApiOperation("获取单个商户信息")
	public Map<String, Object> getOneShop(@RequestParam String shopId){
		if (shopId == null || shopId == "") {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		LiveChatShop result = liveChatShopService.getOneShop(shopId);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS,result);
		}
		return returnResultMap(ResultMapInfo.GETFAIL);
	}
	
	/**
	 * 商户登入
	 * @param shopName 商户名
	 * @param password 密码
	 * @return
	 */
	@GetMapping("/login")
	@ApiOperation("商户登入")
	public Map<String, Object> login(String shopName,String password){
		if (shopName == null || password == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		LiveChatShop result = liveChatShopService.login(shopName,password);
		if (result != null) {
			return returnResultMap(ResultMapInfo.LOGINSUCCESS,result);
		}
		return returnResultMap(ResultMapInfo.LOGINFAIL);
	}
	
}
