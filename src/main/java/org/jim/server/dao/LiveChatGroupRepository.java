package org.jim.server.dao;

import java.util.List;

import org.jim.server.domain.LiveChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LiveChatGroupRepository extends JpaRepository<LiveChatGroup,Long>{
	
	@Query(value="select c.* from Livechat_group c right join livechat_group_member g on c.id = g.group_id where g.user_id = :userId", nativeQuery=true)
	List<LiveChatGroup> getImUserAllGroup(@Param("userId")String userId);
	

}
