package org.jim.server.dao;

import org.jim.server.domain.LivechatConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivechatConfigRepository extends JpaRepository<LivechatConfig,String>{

	LivechatConfig findByShopIdAndDeleteFlag(String shopid, Integer deleteFlag);

}
