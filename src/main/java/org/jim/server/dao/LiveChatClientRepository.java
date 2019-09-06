package org.jim.server.dao;

import org.jim.server.domain.LiveChatClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveChatClientRepository extends JpaRepository<LiveChatClient, String>{

	LiveChatClient findByUidAndShopIdAndDeleteFlag(String uid, String shopid,Integer deleteFlag);

	LiveChatClient findByIdAndDeleteFlag(String userName, Integer deleteFlag);

	LiveChatClient findByTokenAndDeleteFlag(String token, Integer deleteFlag);

}
