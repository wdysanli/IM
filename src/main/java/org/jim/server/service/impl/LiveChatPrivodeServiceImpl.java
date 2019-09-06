package org.jim.server.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.jim.common.ImAio;
import org.jim.common.ImConst;
import org.jim.common.packets.Command;
import org.jim.common.packets.LoginReqBody;
import org.jim.common.tcp.TcpPacket;
import org.jim.common.utils.Md5;
import org.jim.server.dao.LiveChatPrivodeRepository;
import org.jim.server.domain.LiveChatPrivode;
import org.jim.server.domain.LiveChatShop;
import org.jim.server.domain.QueryBean;
import org.jim.server.domain.QueryResult;
import org.jim.server.helper.redis.RedisMessageHelper;
import org.jim.server.server.HelloClientAioHandler;
import org.jim.server.service.LiveChatPrivodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class LiveChatPrivodeServiceImpl implements LiveChatPrivodeService{
	
	RedisMessageHelper redisMessageHelper = new RedisMessageHelper();
	
	@Autowired
	private LiveChatPrivodeRepository liveChatPrivodeRepository;
	
	@Override
	public String privodeLogin(LiveChatPrivode liveChatPrivode) {
		String userName = liveChatPrivode.getUserName();
		String password = liveChatPrivode.getPassword();
		String shopId = liveChatPrivode.getShopId();
		
		LiveChatPrivode findLiveChatPrivode = liveChatPrivodeRepository.findByUserNameAndPasswordAndShopIdAndDeleteFlag(userName, password, shopId, 0);
		if(findLiveChatPrivode != null) {
			// 生成客服登入token
			String id = findLiveChatPrivode.getId();
			long currentTimeMillis = System.currentTimeMillis();
			String token ="LiveChatPrivode"+id+shopId+currentTimeMillis;
			String md5 = Md5.getMD5(token);
			findLiveChatPrivode.setToken(md5);
			liveChatPrivodeRepository.save(findLiveChatPrivode);
			return md5;
		}
		return null;
	}

	@Override
	public int privodeLogout(String id) {
		LiveChatPrivode liveChatPrivode = liveChatPrivodeRepository.findByIdAndDeleteFlag(id,0);
		if (liveChatPrivode == null) {
			return 0;
		}
		boolean online = redisMessageHelper.isOnline(id);
		if (online == false) {
			// 已退出
			return 2;
		}
		ImAio.remove(id, "收到关闭请求!");
		return 1;
	}

	@Override
	public int privodeRegister(LiveChatPrivode liveChatPrivode) {
		String userName = liveChatPrivode.getUserName();
		String password = liveChatPrivode.getPassword();
		String shopId = liveChatPrivode.getShopId();
		LiveChatPrivode findLiveChatPrivode = liveChatPrivodeRepository.findByUserNameAndPasswordAndShopIdAndDeleteFlag(userName, password, shopId,0);
		if (findLiveChatPrivode != null) {
			// 该客服已存在
			return 2;
		}
		liveChatPrivodeRepository.save(liveChatPrivode);
		return 1;
	}

	@Override
	public int privodeDelete(String privodeId) {
		Optional<LiveChatPrivode> optional = liveChatPrivodeRepository.findById(privodeId);
		if (!optional.isPresent()) {
			return 2;
		}
		LiveChatPrivode liveChatPrivode = optional.get();
		liveChatPrivode.setDeleteFlag(1);
		liveChatPrivodeRepository.save(liveChatPrivode);
		return 1;
	}

	@Override
	public QueryResult<LiveChatPrivode> findPrivodeList(QueryBean queryBean) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("userName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("userNick", ExampleMatcher.GenericPropertyMatchers.contains());
		 LiveChatPrivode liveChatPrivode = new LiveChatPrivode();
		if (queryBean.getUserName() != null) {
			liveChatPrivode.setUserName(queryBean.getUserName());
		}
		if (queryBean.getUserNick() != null) {
			liveChatPrivode.setUserNick(queryBean.getUserNick());
		}
		//每个商户自己查询自己的客服
		liveChatPrivode.setShopId(queryBean.getShopId());
		liveChatPrivode.setDeleteFlag(0);
		 //查询条件
        Example<LiveChatPrivode> example = Example.of(liveChatPrivode, exampleMatcher);
        int page = 0;
		if(queryBean.getPage() != 0) {
			page= queryBean.getPage()-1;
		}
		int size = 10;
		if (queryBean.getSize() != 0) {
			size = queryBean.getSize();
		}
        
        Pageable pageable = PageRequest.of(page,size);
		Page<LiveChatPrivode> findAll = liveChatPrivodeRepository.findAll(example, pageable);
		QueryResult<LiveChatPrivode> queryResult = new QueryResult<>();
		queryResult.setTotal(findAll.getTotalElements());
		queryResult.setList(findAll.getContent());
		return queryResult; 
		
	}

	@Override
	public int privodeUpdate(LiveChatPrivode liveChatPrivode) {
		Optional<LiveChatPrivode> findById = liveChatPrivodeRepository.findById(liveChatPrivode.getId());
		if (!findById.isPresent()) {
			return 2;
		}
		LiveChatPrivode findLiveChatPrivode = findById.get();
		if (liveChatPrivode.getUserName() != null) {
			findLiveChatPrivode.setUserName(liveChatPrivode.getUserName());
		}
		if (liveChatPrivode.getUserNick() != null) {
			findLiveChatPrivode.setUserNick(liveChatPrivode.getUserNick());
		}
		if (liveChatPrivode.getPassword() != null) {
			findLiveChatPrivode.setPassword(liveChatPrivode.getPassword());
		}
		if (liveChatPrivode.getPrivodeWeight() != null) {
			findLiveChatPrivode.setPrivodeWeight(liveChatPrivode.getPrivodeWeight());
		}
		liveChatPrivodeRepository.save(findLiveChatPrivode);
		return 1;
	}

	@Override
	public LiveChatPrivode getOnePrivode(String privodeId) {
		LiveChatPrivode liveChatPrivode=liveChatPrivodeRepository.findByIdAndDeleteFlag(privodeId, 0);
		
		return liveChatPrivode;
	}

	

}
