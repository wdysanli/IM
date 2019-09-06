package org.jim.server.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.jim.server.dao.ImUserRepository;
import org.jim.server.dao.LiveChatClientRepository;
import org.jim.server.dao.LiveChatPrivodeRepository;
import org.jim.server.dao.LiveChatRecordRepository;
import org.jim.server.dao.LivechatConfigRepository;
import org.jim.server.domain.ImUser;
import org.jim.server.domain.LiveChatClient;
import org.jim.server.domain.LiveChatClientLoginInfo;
import org.jim.server.domain.LiveChatPrivode;
import org.jim.server.domain.LiveChatRecord;
import org.jim.server.domain.LivechatConfig;
import org.jim.server.helper.redis.RedisMessageHelper;
import org.jim.server.server.HelloClientAioHandler;
import org.jim.server.service.LiveChatClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tio.core.Node;

import aj.org.objectweb.asm.Type;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import org.jim.common.ImConst;
import org.jim.common.cache.redis.RedissonTemplate;
import org.jim.common.ImAio;
import org.jim.common.packets.ChatBody;
import org.jim.common.packets.Command;
import org.jim.common.packets.LoginReqBody;
import org.jim.common.packets.User;
import org.jim.common.tcp.TcpPacket;
import org.jim.common.utils.Md5;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;


@Slf4j
@Service
public class LiveChatClientServiceImpl implements LiveChatClientService{
	
	
	RedisMessageHelper redisMessageHelper = new RedisMessageHelper();
	
	
	@Autowired
	private LiveChatClientRepository liveChatClientRepository;
	
	@Autowired
	private LiveChatPrivodeRepository liveChatPrivodeRepository;
	
	@Autowired
	private LiveChatRecordRepository liveChatRecordRepository;
	
	@Autowired
	private LivechatConfigRepository livechatConfigRepository;
	
	private static Integer currentIndex = 0;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
		
	// 客服系统登入
	@Override
	@Transactional
	public User login(String token, String password) {
		User user = new User();
		// 用户端登入
		LiveChatClient liveChatClient = liveChatClientRepository.findByTokenAndDeleteFlag(token,0);
		if (liveChatClient != null) {
			user.setId("LiveChatClient"+liveChatClient.getUid()+liveChatClient.getShopId());
			user.setNick(liveChatClient.getUserName());
			//token只能使用一次，用完后置为空
			liveChatClient.setToken(null);
			liveChatClientRepository.save(liveChatClient);
			return user;
		}
		// 客服端登入
		LiveChatPrivode liveChatProvide = liveChatPrivodeRepository.findByTokenAndDeleteFlag(token,0);
		if (liveChatProvide != null) {
			user.setId(liveChatProvide.getId());
			user.setNick(liveChatProvide.getUserNick());
			//token只能使用一次，用完后置为空
			liveChatProvide.setToken(null);
			liveChatPrivodeRepository.save(liveChatProvide);
			return user;
		}
		return null;
	
	
	}
	
	
	
	@Override
	public Map<String, Object> clientConnect(String uid, String shopid, String username,Integer type) {
		
		
		HashMap<String, Object> map = new HashMap<>();
		if (type == 2) {
			//退出登入，将token置为空
			map = userClientLogout(uid,shopid);
			return map;
//		}else if (type == 3) {
//			//刷新，重新获取客服
//			boolean isOnline = redisMessageHelper.isOnline("LiveChatClient"+uid+shopid);
//			if (isOnline == false) {
//				map.put("message", "非连接状态下，无法刷新");
//				return map;
//			}
//			
//			LiveChatPrivode provideUser = findUserProvide(shopid);
//			if (provideUser == null) {
//				map.put("message", "刷新成功，暂无可用客服");
//				return map;
//			}
//			LiveChatClient clientUser = liveChatClientRepository.findByUidAndShopIdAndDeleteFlag(uid,shopid,0);
//			LiveChatRecord liveChatRecord = saveRecord(clientUser,provideUser);
//			map.put("liveChatRecord", liveChatRecord);
//			map.put("message", "刷新成功，获取可用客服");
//			return map;
		}else {
			// 在数据库存入用户,并且登入
			LiveChatClient clientUser = liveChatClientRepository.findByUidAndShopIdAndDeleteFlag(uid,shopid,0);
			if (clientUser == null) {
				//将客户存入数据库
				clientUser = userSave(uid,shopid,username);
			}else {
				// 更新token
				long currentTimeMillis = System.currentTimeMillis();
				String token ="LiveChatClient"+uid+shopid+currentTimeMillis;
				String md5 = Md5.getMD5(token);
				clientUser.setToken(md5);
				liveChatClientRepository.save(clientUser);				
			}
			
			// 获取客服实体
			LiveChatPrivode provideUser = findUserProvide(shopid);
			if (provideUser == null) {
				map.put("message","暂无可用客服");
				return map;
			}
			// 客户登入成功，并且获取到可以客服，记录本次会话
			LiveChatRecord liveChatRecord = saveRecord(clientUser,provideUser);
			map.put("clientUserToken", clientUser.getToken());
			map.put("clientUserId", "LiveChatClient"+clientUser.getUid()+clientUser.getShopId());
			map.put("provideUserId", provideUser.getId());
			map.put("provideUserNick",provideUser.getUserNick());
			map.put("message", "获取登入token，并且获取可用客服id");
			return map;
		}
			
		
	}
	
	//记录本次会话
	@Transactional
	private LiveChatRecord saveRecord(LiveChatClient clientUser, LiveChatPrivode provideUser) {
		LiveChatRecord liveChatRecord = new LiveChatRecord();
		liveChatRecord.setClientId(clientUser.getId());
		liveChatRecord.setClientimId(clientUser.getToken());
		liveChatRecord.setUid(clientUser.getUid());
		liveChatRecord.setShopId(clientUser.getShopId());
		liveChatRecord.setClientName(clientUser.getUserName());
		liveChatRecord.setPrivodeId(provideUser.getId());
		liveChatRecord.setPrivideimId(provideUser.getId());
		liveChatRecord.setPrivodeName(provideUser.getUserNick());
		liveChatRecord.setType(0);
		liveChatRecordRepository.save(liveChatRecord);
		return liveChatRecord;
	}



	// 获取可用客服
	@Transactional
	private LiveChatPrivode findUserProvide(String shopid) {
		List<LiveChatPrivode> list = liveChatPrivodeRepository.findByShopIdAndDeleteFlag(shopid,0);
		if (list == null || list.size() == 0) {
			return null;
		}
		// 获取在线客服
		List<LiveChatPrivode> onlineList = new ArrayList<>();
		for (LiveChatPrivode liveChatPrivode : list) {
			String id = liveChatPrivode.getId();
			boolean isOnline = redisMessageHelper.isOnline(id);
			if (isOnline == true) {
				onlineList.add(liveChatPrivode);
			}
		}
		if (onlineList == null || onlineList.size() == 0) {
			return null;
		}
		//获取客服轮询方式
		LivechatConfig  livechatConfig = livechatConfigRepository.findByShopIdAndDeleteFlag(shopid,0);
		Integer pollingType = null;
		if (livechatConfig != null) {
			pollingType = livechatConfig.getPollingType();
		}
		
		
		if (pollingType != null && pollingType == 1) {
			// 加权平滑轮询
			Integer weightIndex = getWeightIndex(onlineList,shopid);
			return onlineList.get(weightIndex);
		}else {
			// 顺序轮询获取客服
			int size = onlineList.size();
			// 从redis中取轮询索引
			Integer index = (Integer) redisTemplate.opsForValue().get("Polling"+shopid);
			// redis中索引为空和为最后一个索引时，取第一个索引
			if (index == null || index >= (size-1)) {
				index=0;
			}else {
				index=index+1;
			}
			// 将索引存入redis
			redisTemplate.opsForValue().set("Polling"+shopid, index);
			return onlineList.get(index);
		}
		
	}
	
	// 加权平滑轮询
	
	private Integer getWeightIndex(List<LiveChatPrivode> onlineList,String shopid) {
		// 获取总权重数
		Integer totalWeight = 0;
		for (LiveChatPrivode liveChatPrivode : onlineList) {
			totalWeight += liveChatPrivode.getPrivodeWeight();
		}
		//获取redis中的总权重数
		Map<String , Integer> map = (Map<String, Integer>) redisTemplate.opsForValue().get("PollingWeight"+shopid);
		
		// redis中集合为空 或者 总权重数与redis中不同，则重置权重值
		
		
		if (map == null || map.get(10000+"") == null || map.get(10000+"") != totalWeight ) {
			// 获取最大的权重值的索引
			map = new HashMap<>();
			Integer maxWeightIndex = -1;
			Integer maxWeight = -10000;
			for(Integer i= 0 ;i < onlineList.size();i++) {
				Integer privodeWeight = onlineList.get(i).getPrivodeWeight();
				if (privodeWeight > maxWeight) {
					maxWeightIndex =i;
					maxWeight = privodeWeight;
				}
			}
			
			
			// 重新计算权重值，并且存入redis中；
			for(Integer i= 0 ;i < onlineList.size();i++) {
				Integer privodeWeight = onlineList.get(i).getPrivodeWeight();
				// 被选中的索引
				if (i == maxWeightIndex ) {
					Integer finalWeight = privodeWeight - totalWeight + privodeWeight;
					map.put(i+"", finalWeight);
					continue;
				}
				// 其他索引
				Integer finalWeight = privodeWeight + privodeWeight;
				map.put(i+"", finalWeight);
			}
			
			map.put(10000+"", totalWeight);
			redisTemplate.opsForValue().set("PollingWeight"+shopid, map);
			return maxWeightIndex;
		}
		
		
		// 获取比重值最大的索引
		Integer finalKey = -1;
		Integer finalValue = -10000;
		Set<Entry<String , Integer>> entrySet = map.entrySet();
		for (Entry<String , Integer> entry : entrySet) {
			String key = entry.getKey();
			if (key.equals(10000+"")) {
				continue;
			}
			Integer value = entry.getValue();
			if (value > finalValue) {
				finalKey = Integer.valueOf(key);
				finalValue= value;
			}
		}
		
		// 重新计算权重值，并且存入redis中；
		for(Integer i= 0 ;i < onlineList.size();i++) {
			LiveChatPrivode liveChatPrivode = onlineList.get(i);
			
			// 被选中的索引
			if (i == finalKey) {
				Integer value = map.get(i+"") - totalWeight + liveChatPrivode.getPrivodeWeight();
				map.put(i+"", value);
				continue;
			}
			// 其他索引
			Integer value = map.get(i+"")+liveChatPrivode.getPrivodeWeight();
			map.put(i+"", value);
		}
		
		redisTemplate.opsForValue().set("PollingWeight"+shopid, map);
		
		return finalKey;
	}



	@Transactional
	private LiveChatClient userSave(String uid, String shopid, String username) {
		LiveChatClient liveChatClient = new LiveChatClient();
		liveChatClient.setUid(uid);
		liveChatClient.setShopId(shopid);
		liveChatClient.setUserName(username);
		liveChatClient.setPassword("12345678");
		long currentTimeMillis = System.currentTimeMillis();
		String token ="LiveChatClient"+uid+shopid+currentTimeMillis;
		String md5 = Md5.getMD5(token);
		liveChatClient.setToken(md5);
		liveChatClientRepository.save(liveChatClient);
		return liveChatClient;
	}
	@Transactional
	private HashMap<String, Object> userClientLogout(String uid, String shopid) {
		HashMap<String, Object> map = new HashMap<>();
		// 判断是否已关闭连接
		boolean isOnline = redisMessageHelper.isOnline("LiveChatClient"+uid+shopid);
		if (isOnline) {
			//未关闭，则关闭连接
			ImAio.remove("LiveChatClient"+uid+shopid, "收到关闭请求!");
			map.put("message", "关闭连接成功");
			return map;
		}else {
			map.put("message", "已关闭连接，请勿重复操作");
			return map;
		}
	}
	
}

	

