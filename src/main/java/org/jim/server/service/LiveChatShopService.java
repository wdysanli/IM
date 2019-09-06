package org.jim.server.service;

import org.jim.server.domain.LiveChatShop;
import org.jim.server.domain.QueryBean;
import org.jim.server.domain.QueryResult;

public interface LiveChatShopService {

	int shopRegister(LiveChatShop liveChatShop);

	int shopDelete(String shopId);

	int shopUpdate(LiveChatShop liveChatShop);

	QueryResult<LiveChatShop> findShopList(QueryBean queryBean);

	LiveChatShop getOneShop(String shopId);

	LiveChatShop login(String shopName, String password);

}
