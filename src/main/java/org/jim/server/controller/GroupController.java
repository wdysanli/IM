package org.jim.server.controller;

import java.util.List;

import org.jim.server.base.bo.ResultContent;
import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.domain.ChatGroup;
import org.jim.server.domain.ImUser;
import org.jim.server.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/im/group")
public class GroupController extends BaseController {
	
	@Autowired
	IGroupService groupService;
	
//	@RequestMapping(value = "/groupList", method = RequestMethod.GET)
//	public ResultContent groupList() {
//		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
//		List<ChatGroup> result = groupService.getAllGroup(imUser);
//		return new ResultContent(ResultMapInfo.GETSUCCESS, result);
//	}
	
	@RequestMapping(value = "/getGroupAllNumber", method = RequestMethod.GET)
	public ResultContent groupList() {
		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
//		List<ChatGroup> result = groupService.getAllGroup(imUser);
		return new ResultContent(ResultMapInfo.GETSUCCESS);
	}
	
	@RequestMapping(value = "/createGroup", method = RequestMethod.GET)
	public ResultContent createGroup(ChatGroup group) {
		if (group == null) return new ResultContent(ResultMapInfo.NOTPARAM);
		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
		ResultMapInfo result = groupService.createGroup(group, imUser);
		return new ResultContent(result);
	}

	@RequestMapping(value = "/jionGroup", method = RequestMethod.GET)
	public ResultContent jionGroup(Long groupId) {
		if (groupId == null) return new ResultContent(ResultMapInfo.NOTPARAM);
		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
		ResultMapInfo result = groupService.jionGroup(groupId, imUser);
		return new ResultContent(result);
	}
	 
	@RequestMapping(value = "/quitGroup", method = RequestMethod.GET)
	public ResultContent quitGroup(Long groupId) {
		if (groupId == null) return new ResultContent(ResultMapInfo.NOTPARAM);
		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
		ResultMapInfo result = groupService.quitGroup(groupId, imUser);
		return new ResultContent(result);
	}
	
	@RequestMapping(value = "/examineGroupMember", method = RequestMethod.GET)
	public ResultContent examineGroupMember(Long groupId, Long userId) {
		if (groupId == null || userId == null) return new ResultContent(ResultMapInfo.NOTPARAM);
		ImUser imUser = (ImUser) getSession().getAttribute("imUser");
		ResultMapInfo result = groupService.examineGroupMember(groupId, userId, imUser);
		return new ResultContent(result);
	}
}
