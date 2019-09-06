package org.jim.server.controller;

import java.util.Map;

import org.jim.common.utils.Md5;
import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.domain.LiveChatClientLoginInfo;
import org.jim.server.service.LiveChatClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/livechatclient")
@Api(value = "客服系统：用户登入获取在线客服",tags = {"客服系统：用户登入获取在线客服"})
public class LiveChatClientController extends BaseController{
	
	@Autowired
	private LiveChatClientService liveChatClientService;
	
	@Value("${spring.clientchatkey}")
	private String key;
	/*
	 * 客户登入，获取在线客服
	 */
	@PostMapping("/connect")
	@ApiOperation("获取在线客服")
	public Map<String, Object> clientConnect(@RequestBody LiveChatClientLoginInfo loginInfo){
		if (loginInfo == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		String uid = loginInfo.getUid();
		String shopid = loginInfo.getShopid();
		Integer type = loginInfo.getType();
		String username = loginInfo.getUsername();
		String sign = loginInfo.getSign();
		if (uid == null || shopid == null|| username == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		//做MD5校验
		String md5 = Md5.getMD5(uid+shopid+username+type+key);
		System.out.println(md5);
//		if (!sign.equals(md5)) {
//			return returnResultMap(ResultMapInfo.FAILESIGN);
//		}
		Map<String, Object> map = liveChatClientService.clientConnect( uid, shopid, username,type);
		if (map != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS,map);
		}
		return returnResultMap(ResultMapInfo.GETSUCCESS);
	}
	
}
