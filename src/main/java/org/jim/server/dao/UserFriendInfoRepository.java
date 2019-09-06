package org.jim.server.dao;

import java.util.List;

import org.jim.server.domain.UserFrendInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFriendInfoRepository extends JpaRepository<UserFrendInfo, Long>{

	UserFrendInfo findByUserIdAndFrendUserIdAndStatus(Long userId, Long frendUserId, int status);

	List<UserFrendInfo> findByFrendUserIdAndStatus(Long frendUserId, int status);

}
