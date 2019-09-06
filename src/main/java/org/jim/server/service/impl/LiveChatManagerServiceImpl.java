package org.jim.server.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.jim.server.dao.LiveChatManagerRepository;
import org.jim.server.domain.LiveChatManager;
import org.jim.server.domain.LiveChatShop;
import org.jim.server.domain.QueryBean;
import org.jim.server.domain.QueryResult;
import org.jim.server.service.LiveChatManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LiveChatManagerServiceImpl implements LiveChatManagerService{
	
	@Autowired
	private LiveChatManagerRepository liveChatManagerRepository;

	@Override
	public int managerRegister(LiveChatManager liveChatManager) {
		String managerName = liveChatManager.getManagerName();
		String password = liveChatManager.getPassword();
		LiveChatManager findLiveChatManager=liveChatManagerRepository.findByManagerNameAndPasswordAndDeleteFlag(managerName,password,0);
		if (findLiveChatManager != null) {
			return 2;
		}
		liveChatManagerRepository.save(liveChatManager);
		return 1;
	}

	@Override
	public int managerDelete(String managerId) {
		Optional<LiveChatManager> optional = liveChatManagerRepository.findById(managerId);
		if (!optional.isPresent()) {
			return 2;
		}
		LiveChatManager liveChatManager = optional.get();
		liveChatManager.setDeleteFlag(1);
		liveChatManagerRepository.save(liveChatManager);
		return 1;
	}

	@Override
	public int managerUpdate(LiveChatManager liveChatManager) {
		String id = liveChatManager.getId();
		Optional<LiveChatManager> optional = liveChatManagerRepository.findById(id);
		if (!optional.isPresent()) {
			return 2;
		}
		LiveChatManager findLiveChatManager = optional.get();
		if (liveChatManager.getManagerName() != null) {
			findLiveChatManager.setManagerName(liveChatManager.getManagerName());
		}
		if (liveChatManager.getPassword() != null) {
			findLiveChatManager.setPassword(liveChatManager.getPassword());
		}
		if (liveChatManager.getManagerNick() != null) {
			findLiveChatManager.setManagerNick(liveChatManager.getManagerNick());
		}
		liveChatManagerRepository.save(findLiveChatManager);
		return 1;
	}

	@Override
	public QueryResult<LiveChatManager> findManagerList(QueryBean queryBean) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("managerName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("managerNick", ExampleMatcher.GenericPropertyMatchers.contains());
		LiveChatManager liveChatManager = new LiveChatManager();
		if (queryBean.getManagerName() != null) {
			liveChatManager.setManagerName(queryBean.getManagerName());
		}
		if (queryBean.getManagerNick() != null) {
			liveChatManager.setManagerNick(queryBean.getManagerNick());
		}
		liveChatManager.setDeleteFlag(0);
		 //查询条件
        Example<LiveChatManager> example = Example.of(liveChatManager, exampleMatcher);
        int page = 0;
		if(queryBean.getPage() != 0) {
			page= queryBean.getPage()-1;
		}
		int size = 10;
		if (queryBean.getSize() != 0) {
			size = queryBean.getSize();
		}
        Pageable pageable = PageRequest.of(page,size);
		Page<LiveChatManager> findAll = liveChatManagerRepository.findAll(example, pageable);
		QueryResult<LiveChatManager> queryResult = new QueryResult<>();
		queryResult.setTotal(findAll.getTotalElements());
		queryResult.setList(findAll.getContent());
		return queryResult; 
	}

	@Override
	public LiveChatManager getOneManager(String managerId) {
		LiveChatManager liveChatManager=liveChatManagerRepository.findByIdAndDeleteFlag(managerId,0);
		
		return liveChatManager;
	}

	@Override
	public LiveChatManager login(String managerName, String password) {
		LiveChatManager result = liveChatManagerRepository.findByManagerNameAndPasswordAndDeleteFlag(managerName, password, 0);
		return result;
	}
	
	
	

}
