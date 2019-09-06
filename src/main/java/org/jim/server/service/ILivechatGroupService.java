package org.jim.server.service;

import java.util.List;
import java.util.Map;

import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.domain.ImUser;
import org.jim.server.domain.LiveChatClient;
import org.jim.server.domain.LiveChatGroup;

public interface ILivechatGroupService {
	
	ResultMapInfo createLiveChatGroup(LiveChatGroup group, LiveChatClient imUser);

	ResultMapInfo jionLiveChatGroup(Long groupId, LiveChatClient imUser);

	ResultMapInfo quitLiveChatGroup(Long groupId, LiveChatClient imUser);

	ResultMapInfo examineLiveChatGroupMember(Long groupId, Long userId, LiveChatClient imUser);

	List<LiveChatGroup> getAllLiveChatGroup(LiveChatClient imUser);
	
	Map<String, Object> clientConnect(String uid, String shopid, String username,Integer type, String sign);

}
