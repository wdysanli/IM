package org.jim.server.dao;

import org.jim.server.domain.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface GroupMemberDao extends JpaRepository<GroupMember, Long> {

	GroupMember findByGroupIdAndUserId(@Param("groupId")Long groupId, @Param("userId")Long userId);

}
