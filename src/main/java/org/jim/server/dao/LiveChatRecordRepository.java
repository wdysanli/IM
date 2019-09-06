package org.jim.server.dao;

import org.jim.server.domain.LiveChatRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveChatRecordRepository extends JpaRepository<LiveChatRecord, String>{

}
