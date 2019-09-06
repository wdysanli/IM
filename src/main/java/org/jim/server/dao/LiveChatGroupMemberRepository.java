package org.jim.server.dao;

import org.jim.server.domain.LiveChatGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface LiveChatGroupMemberRepository extends JpaRepository<LiveChatGroupMember, Long>{
	LiveChatGroupMember findByGroupIdAndUserId(@Param("groupId")Long groupId, @Param("userId")String userId);
}
