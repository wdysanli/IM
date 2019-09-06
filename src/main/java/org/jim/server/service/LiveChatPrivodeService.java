package org.jim.server.service;

import java.util.Map;

import org.jim.server.domain.LiveChatPrivode;
import org.jim.server.domain.QueryBean;
import org.jim.server.domain.QueryResult;



public interface LiveChatPrivodeService {

	String privodeLogin( LiveChatPrivode liveChatPrivode);

	int privodeLogout(String id);

	int privodeRegister(LiveChatPrivode liveChatPrivode);

	int privodeDelete(String privodeId);

	QueryResult<LiveChatPrivode> findPrivodeList(QueryBean queryBean);

	int privodeUpdate(LiveChatPrivode liveChatPrivode);

	LiveChatPrivode getOnePrivode(String privodeId);

	

}
