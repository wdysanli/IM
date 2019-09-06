package org.jim.server.service;

import java.util.List;

import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.domain.ChatGroup;
import org.jim.server.domain.ImUser;

public interface IGroupService {

	ResultMapInfo createGroup(ChatGroup group, ImUser imUser);

	ResultMapInfo jionGroup(Long groupId, ImUser imUser);

	ResultMapInfo quitGroup(Long groupId, ImUser imUser);

	ResultMapInfo examineGroupMember(Long groupId, Long userId, ImUser imUser);

	List<ChatGroup> getAllGroup(ImUser imUser);

}
