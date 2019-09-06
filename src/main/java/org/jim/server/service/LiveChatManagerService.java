package org.jim.server.service;

import org.jim.server.domain.LiveChatManager;
import org.jim.server.domain.QueryBean;
import org.jim.server.domain.QueryResult;

public interface LiveChatManagerService {

	int managerRegister(LiveChatManager liveChatManager);

	int managerDelete(String managerId);

	int managerUpdate(LiveChatManager liveChatManager);

	QueryResult<LiveChatManager> findManagerList(QueryBean queryBean);

	LiveChatManager getOneManager(String managerId);

	LiveChatManager login(String managerName, String password);

}
