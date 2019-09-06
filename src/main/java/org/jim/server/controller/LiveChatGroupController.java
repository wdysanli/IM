package org.jim.server.controller;

import java.util.List;
import java.util.Map;

import org.jim.server.base.bo.ResultContent;
import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.dao.LiveChatClientRepository;
import org.jim.server.domain.ChatGroup;
import org.jim.server.domain.ImUser;
import org.jim.server.domain.LiveChatClient;
import org.jim.server.domain.LiveChatGroup;
import org.jim.server.service.IGroupService;
import org.jim.server.service.ILivechatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/im/liveChatGroup")
@Api(value = "群组聊天：群组管理",tags = {"群组聊天：群组管理"})
public class LiveChatGroupController extends BaseController {
	
	@Autowired
	ILivechatGroupService livechatGroupService;
	
	
	@Autowired
	private LiveChatClientRepository liveChatClientRepository;
	
	@GetMapping("/connect")
	@ApiOperation("获取在线客服")
	public Map<String, Object> clientConnect(@RequestParam("uid")String uid,
											@RequestParam("shopid")String shopid,
											@RequestParam("username")String username,
											@RequestParam("type")Integer type,
											@RequestParam("sign")String sign){
		Map<String, Object> map = livechatGroupService.clientConnect(uid,shopid,username,type,sign);
		if (map != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS,map);
		}
		return returnResultMap(ResultMapInfo.GETSUCCESS);
	}
	
	
	
	@ApiOperation("获取群组列表")
	@RequestMapping(value = "/groupList", method = RequestMethod.GET)
	public ResultContent groupList(String uid, String shopid) {
		
		
		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
		
		LiveChatClient clientUser = liveChatClientRepository.findByUidAndShopIdAndDeleteFlag(uid,shopid,0);
		List<LiveChatGroup> result = livechatGroupService.getAllLiveChatGroup(clientUser);
		return new ResultContent(ResultMapInfo.GETSUCCESS, result);
	}
	
	/*@RequestMapping(value = "/getGroupAllNumber", method = RequestMethod.GET)
	public ResultContent groupList() {
		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
		List<LiveChatGroup> result = livechatGroupService.getAllLiveChatGroup(imUser);
		return new ResultContent(ResultMapInfo.GETSUCCESS);
	}*/
	@ApiOperation("创建群组")
	@RequestMapping(value = "/createGroup", method = RequestMethod.GET)
	public ResultContent createGroup(LiveChatGroup group,String uid, String shopid) {
		if (group == null) return new ResultContent(ResultMapInfo.NOTPARAM);
		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
		LiveChatClient clientUser = liveChatClientRepository.findByUidAndShopIdAndDeleteFlag(uid,shopid,0);
		ResultMapInfo result = livechatGroupService.createLiveChatGroup(group, clientUser);
		return new ResultContent(result);
	}
	@ApiOperation("加入群组")
	@RequestMapping(value = "/jionGroup", method = RequestMethod.GET)
	public ResultContent jionGroup(Long groupId,String uid, String shopid) {
		if (groupId == null) return new ResultContent(ResultMapInfo.NOTPARAM);
		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
		LiveChatClient clientUser = liveChatClientRepository.findByUidAndShopIdAndDeleteFlag(uid,shopid,0);
		ResultMapInfo result = livechatGroupService.jionLiveChatGroup(groupId, clientUser);
		return new ResultContent(result);
	}
	@ApiOperation("退出群组")
	@RequestMapping(value = "/quitGroup", method = RequestMethod.GET)
	public ResultContent quitGroup(Long groupId,String uid, String shopid) {
		if (groupId == null) return new ResultContent(ResultMapInfo.NOTPARAM);
		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
		LiveChatClient clientUser = liveChatClientRepository.findByUidAndShopIdAndDeleteFlag(uid,shopid,0);
		ResultMapInfo result = livechatGroupService.quitLiveChatGroup(groupId, clientUser);
		return new ResultContent(result);
	}
	@ApiOperation("检查是否在群组")
	@RequestMapping(value = "/examineGroupMember", method = RequestMethod.GET)
	public ResultContent examineGroupMember(Long groupId, Long userId,String uid, String shopid) {
		if (groupId == null || userId == null) return new ResultContent(ResultMapInfo.NOTPARAM);
		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
		LiveChatClient clientUser = liveChatClientRepository.findByUidAndShopIdAndDeleteFlag(uid,shopid,0);
		ResultMapInfo result = livechatGroupService.examineLiveChatGroupMember(groupId, userId, clientUser);
		return new ResultContent(result);
	}
}
