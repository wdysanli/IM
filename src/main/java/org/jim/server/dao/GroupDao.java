package org.jim.server.dao;

import java.util.List;

import org.jim.server.domain.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupDao extends JpaRepository<ChatGroup, Long> {

	@Query(value="select c.* from chat_group c right join group_member g on c.id = g.group_id where g.user_id = :userId", nativeQuery=true)
	List<ChatGroup> getImUserAllGroup(@Param("userId")Long userId);

}
