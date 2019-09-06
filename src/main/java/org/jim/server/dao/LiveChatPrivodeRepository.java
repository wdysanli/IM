package org.jim.server.dao;

import java.util.List;

import org.jim.server.domain.LiveChatPrivode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveChatPrivodeRepository extends JpaRepository<LiveChatPrivode, String>{

	LiveChatPrivode findByUserNameAndPasswordAndDeleteFlag(String userName, String password,Integer deleteFlag);

	List<LiveChatPrivode> findByShopIdAndDeleteFlag(String shopId,Integer deleteFlag);

	LiveChatPrivode findByIdAndDeleteFlag(String id, Integer deleteFlag);

	LiveChatPrivode findByUserNameAndPasswordAndShopIdAndDeleteFlag(String userName, String password, String shopId,
			Integer deleteFlag);

	LiveChatPrivode findByTokenAndDeleteFlag(String token, Integer deleteFlag);

}
