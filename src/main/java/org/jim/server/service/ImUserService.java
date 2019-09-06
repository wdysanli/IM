package org.jim.server.service;

import org.jim.server.base.bo.ResultContent;
import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.dao.ImUserRepository;
import org.jim.server.domain.ImUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImUserService {
	
	@Autowired
	ImUserRepository imUserRepository;
	
	public ImUser getImUser(String userName, String password) {
		return imUserRepository.findByUserNameAndPassword(userName, password);
	}

	public ImUser login(String userName, String password) {
		return imUserRepository.findByUserNameAndPassword(userName, password);
	}
	
}
