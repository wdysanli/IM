package org.jim.server.dao;

import org.jim.server.domain.LiveChatShop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveChatShopRepository extends JpaRepository<LiveChatShop,String>{

	LiveChatShop findByShopNameAndPasswordAndDeleteFlag(String shopName, String password, Integer deleteFlag);

	LiveChatShop findByShopNameAndPasswordAndManagerIdAndDeleteFlag(String shopName, String password, String managerId,
			Integer deleteFlag);

	LiveChatShop findByIdAndDeleteFlag(String shopId, Integer deleteFlag);

}
