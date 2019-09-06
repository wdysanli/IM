package org.jim.server.dao;

import org.jim.server.domain.GroupConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupConfigDao extends JpaRepository<GroupConfig, Long> {

}
