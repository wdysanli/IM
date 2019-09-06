package org.jim.server.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jim.common.ImAio;
import org.jim.common.ImConst;
import org.jim.common.packets.Command;
import org.jim.common.packets.LoginReqBody;
import org.jim.common.tcp.TcpPacket;
import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.dao.GroupConfigDao;
import org.jim.server.dao.GroupDao;
import org.jim.server.dao.GroupMemberDao;
import org.jim.server.dao.LiveChatClientRepository;
import org.jim.server.dao.LiveChatGroupConfigRepository;
import org.jim.server.dao.LiveChatGroupMemberRepository;
import org.jim.server.dao.LiveChatGroupRepository;
import org.jim.server.domain.ChatGroup;
import org.jim.server.domain.GroupConfig;
import org.jim.server.domain.GroupMember;
import org.jim.server.domain.ImUser;
import org.jim.server.domain.LiveChatClient;
import org.jim.server.domain.LiveChatGroup;
import org.jim.server.domain.LiveChatGroupConfig;
import org.jim.server.domain.LiveChatGroupMember;
import org.jim.server.domain.LiveChatPrivode;
import org.jim.server.domain.LiveChatRecord;
import org.jim.server.helper.redis.RedisMessageHelper;
import org.jim.server.server.HelloClientAioHandler;
import org.jim.server.service.ILivechatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Transactional
@Service
public class LiveChatGroupServiceImpl implements ILivechatGroupService{
	
	
	/**
	 * 服务器节点
	 */
	public static Node serverNode = new Node("localhost", ImConst.SERVER_PORT);
	/**
	 * handler, 包括编码、解码、消息处理
	 */
	public static ClientAioHandler aioClientHandler = new HelloClientAioHandler();

	/**
	 * 事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
	 */
	public static ClientAioListener aioListener = null;

	/**
	 * 断链后自动连接的，不想自动连接请设为null
	 */
	private static ReconnConf reconnConf = new ReconnConf(5000L);

	/**
	 * 一组连接共用的上下文对象
	 */
	public static ClientGroupContext clientGroupContext = new ClientGroupContext(aioClientHandler, aioListener, reconnConf);

	public static AioClient aioClient = null;
	public static ClientChannelContext clientChannelContext = null;
	
	
	RedisMessageHelper redisMessageHelper = new RedisMessageHelper();
	
	
	
	
	@Autowired
	private LiveChatClientRepository liveChatClientRepository;
	
	@Autowired
	private LiveChatGroupRepository liveChatGroupRepository;
	
	@Autowired
	private LiveChatGroupMemberRepository liveChatGroupMemberRepository;
	
	@Autowired
	private LiveChatGroupConfigRepository liveChatGroupConfigRepository;
	
	
	
	@Override
	public ResultMapInfo createLiveChatGroup(LiveChatGroup group, LiveChatClient clientUser) {
		
		if (group.getId() != null) group.setId(null);
		Gson gson = new Gson();
		System.out.println(gson.toJson(group));
		group.setState(1);
		group = liveChatGroupRepository.save(group);
		log.info("saveLiveChat  group success");
		LiveChatGroupMember groupMember = clientUser.getLiveChatGroupMember(group);
		groupMember.setJurisdiction(0);
		groupMember.setState(1);
		liveChatGroupMemberRepository.save(groupMember);
		log.info("save  LiveChat group member success");
		LiveChatGroupConfig groupConfig = LiveChatGroupConfig.getInitLiveChatGroupConfig(group);
		liveChatGroupConfigRepository.save(groupConfig);
		log.info("save LiveChat group config success");
		return ResultMapInfo.CREATESUCCESS;
		
	}

	@Override
	public ResultMapInfo jionLiveChatGroup(Long groupId, LiveChatClient clientUser) {
		LiveChatGroup group = liveChatGroupRepository.findById(groupId).orElse(null);
		if (group == null) return ResultMapInfo.JIONFAIL;
		LiveChatGroupMember groupMember = clientUser.getLiveChatGroupMember(group);
		groupMember.setJurisdiction(2);
		groupMember.setState(0);
		liveChatGroupMemberRepository.save(groupMember);
		return ResultMapInfo.JIONSUCCESS;
	}

	@Override
	public ResultMapInfo quitLiveChatGroup(Long groupId, LiveChatClient clientUser) {
		LiveChatGroupMember groupMember = liveChatGroupMemberRepository.findByGroupIdAndUserId(groupId, clientUser.getUid());
		if (groupMember == null) return ResultMapInfo.QUITFAIL;
		liveChatGroupMemberRepository.delete(groupMember);
		return ResultMapInfo.QUITSUCCESS;
	}

	@Override
	public ResultMapInfo examineLiveChatGroupMember(Long groupId, Long userId, LiveChatClient clientUser) {
		LiveChatGroupMember groupManage = liveChatGroupMemberRepository.findByGroupIdAndUserId(groupId, clientUser.getUid());
		LiveChatGroupMember groupMember = liveChatGroupMemberRepository.findByGroupIdAndUserId(groupId, clientUser.getUid());
		if (groupManage == null || groupMember == null) return ResultMapInfo.ADDFAIL;
		if (groupManage.getJurisdiction() > 1) return ResultMapInfo.jURISDICTIONERROR;
		groupMember.setState(1);
		return ResultMapInfo.ADDSUCCESS;
	}

	@Override
	public List<LiveChatGroup> getAllLiveChatGroup(LiveChatClient clientUser) {
		return liveChatGroupRepository.getImUserAllGroup(clientUser.getUid());
	}

	@Override
	public Map<String, Object> clientConnect(String uid, String shopid, String username, Integer type, String sign) {
		HashMap<String, Object> map = new HashMap<>();
		if (type == 2) {
			//退出登入
			map = userClientLogout(uid,shopid);
			return map;
		
		}else {
			// 用户登入// 判断用户是否在线
			boolean isOnline = redisMessageHelper.isOnline("LiveChatClient"+uid+shopid);
			if (isOnline) {
				// 在线，请勿重新登入
				map.put("message", "已连接，请勿重复连接");
				return map;
			}else {
				// 在数据库存入用户,并且登入
				LiveChatClient clientUser = liveChatClientRepository.findByUidAndShopIdAndDeleteFlag(uid,shopid,0);
				if (clientUser == null) {
					//将客户存入数据库
					clientUser = userSave(uid,shopid,username);
				}
				// 返回客户实体
				map.put("clientUser", clientUser);
				// 登入
				boolean flag = userClientLogin(clientUser.getId());
				if (flag == false) {
					// 登入失败直接返回
					map.put("loginState", flag);
					return map;
				}								
				map.put("loginState", true);
				return map;
			}
			
		}
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
	@Transactional
	private boolean userClientLogin(String clientId) {
		try {
			clientGroupContext.setHeartbeatTimeout(0);
			aioClient = new AioClient(clientGroupContext);
			clientChannelContext = aioClient.connect(serverNode);
			byte[] loginBody = new LoginReqBody(clientId,"12345678").toByte();
			TcpPacket loginPacket = new TcpPacket(Command.COMMAND_LOGIN_REQ,loginBody);
			//登录;
			ImAio.send(clientChannelContext, loginPacket);
			log.info("client login success");
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	@Transactional
	private LiveChatClient userSave(String uid, String shopid, String username) {
		LiveChatClient liveChatClient = new LiveChatClient();
		liveChatClient.setUid(uid);
		liveChatClient.setShopId(shopid);
		liveChatClient.setUserName(username);
		liveChatClient.setPassword("12345678");
		liveChatClientRepository.save(liveChatClient);
		return liveChatClient;
	}
}
