package org.jim.server.dao;

import java.util.List;

import org.jim.server.domain.ImUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImUserRepository extends JpaRepository<ImUser, Long>{

	ImUser findByUserNameAndPassword(String userName, String password);

	ImUser findByUserNameAndUserNickAndDeleteFlag(String userName, String userNick, int i);

	

}
