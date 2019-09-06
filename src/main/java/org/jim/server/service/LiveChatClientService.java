package org.jim.server.service;

import java.util.Map;

import org.jim.common.packets.User;

public interface LiveChatClientService {

	Map<String, Object> clientConnect(String uid, String shopid, String username,Integer type);

	User login(String userName, String password);

}
