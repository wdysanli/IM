package org.jim.server.dao;

import org.jim.server.domain.LiveChatManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveChatManagerRepository extends JpaRepository<LiveChatManager,String>{

	LiveChatManager findByManagerNameAndPasswordAndDeleteFlag(String managerName, String password, Integer deleteFlag);

	LiveChatManager findByIdAndDeleteFlag(String managerId, Integer deleteFlag);

}
