package org.jim.server.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.jim.server.dao.LiveChatShopRepository;
import org.jim.server.domain.LiveChatShop;
import org.jim.server.domain.QueryBean;
import org.jim.server.domain.QueryResult;
import org.jim.server.service.LiveChatShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LiveChatShopServiceImpl implements LiveChatShopService {
	@Autowired
	private LiveChatShopRepository liveChatShopRepository;
	
	
	@Override
	public int shopRegister(LiveChatShop liveChatShop) {
		String shopName = liveChatShop.getShopName();
		String password = liveChatShop.getPassword();
		String managerId = liveChatShop.getManagerId();
		LiveChatShop findliveChatShop = liveChatShopRepository.findByShopNameAndPasswordAndManagerIdAndDeleteFlag(shopName,password,managerId,0);
		if (findliveChatShop != null) {
			return 2;
		}
		liveChatShopRepository.save(liveChatShop);
		return 1;
	}


	@Override
	public int shopDelete(String shopId) {
		Optional<LiveChatShop> optional = liveChatShopRepository.findById(shopId);
		if (!optional.isPresent()) {
			return 2;
		}
		LiveChatShop liveChatShop = optional.get();
		liveChatShop.setDeleteFlag(1);
		liveChatShopRepository.save(liveChatShop);
		return 1;
	}


	@Override
	public int shopUpdate(LiveChatShop liveChatShop) {
		Optional<LiveChatShop> optional = liveChatShopRepository.findById(liveChatShop.getId());
		if (!optional.isPresent()) {
			return 2;
		}
		LiveChatShop findLiveChatShop = optional.get();
		if (liveChatShop.getShopName() != null) {
			findLiveChatShop.setShopName(liveChatShop.getShopName());
		}
		if (liveChatShop.getPassword() != null) {
			findLiveChatShop.setPassword(liveChatShop.getPassword());
		}
		if (liveChatShop.getShopNick() != null) {
			findLiveChatShop.setShopNick(liveChatShop.getShopNick());
		}
		liveChatShopRepository.save(findLiveChatShop);
		return 1;
	}


	@Override
	public QueryResult<LiveChatShop> findShopList(QueryBean queryBean) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("shopName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("shopNick", ExampleMatcher.GenericPropertyMatchers.contains());
		LiveChatShop liveChatShop = new LiveChatShop();
		if (queryBean.getShopName() != null) {
			liveChatShop.setShopName(queryBean.getShopName());
		}
		if (queryBean.getShopNick() != null) {
			liveChatShop.setShopNick(queryBean.getShopNick());
		}
		liveChatShop.setDeleteFlag(0);
		 //查询条件
        Example<LiveChatShop> example = Example.of(liveChatShop, exampleMatcher);
		
        int page = 0;
		if(queryBean.getPage() != 0) {
			page= queryBean.getPage()-1;
		}
		int size = 10;
		if (queryBean.getSize() != 0) {
			size = queryBean.getSize();
		}
        Pageable pageable = PageRequest.of(page,size);
		Page<LiveChatShop> findAll = liveChatShopRepository.findAll(example, pageable);
		QueryResult<LiveChatShop> queryResult = new QueryResult<>();
		queryResult.setTotal(findAll.getTotalElements());
		queryResult.setList(findAll.getContent());
		return queryResult; 
	}


	@Override
	public LiveChatShop getOneShop(String shopId) {
		LiveChatShop liveChatShop = liveChatShopRepository.findByIdAndDeleteFlag(shopId,0);
		
		return liveChatShop;
	}


	@Override
	public LiveChatShop login(String shopName, String password) {
		LiveChatShop result = liveChatShopRepository.findByShopNameAndPasswordAndDeleteFlag(shopName, password, 0);
		return result;
	}

}
