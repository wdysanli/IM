package org.jim.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jim.common.packets.UserMessageData;
import org.jim.server.dao.ImUserRepository;
import org.jim.server.dao.UserFriendInfoRepository;
import org.jim.server.domain.ImUser;
import org.jim.server.domain.QueryBean;
import org.jim.server.domain.QueryResult;
import org.jim.server.domain.UserFrendInfo;
import org.jim.server.helper.redis.RedisMessageHelper;
import org.jim.server.service.impl.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PersonalService {
	@Autowired
	ImUserRepository imUserRepository;
	
	@Autowired
	UserFriendInfoRepository userFriendInfoRepository;
	
	
	public int registerImUser(ImUser imUser) {
		String userName = imUser.getUserName();
		String userNick = imUser.getUserNick();
		ImUser findImUser = imUserRepository.findByUserNameAndUserNickAndDeleteFlag(userName, userNick,0);
		if (findImUser != null) {
			return 2;
		}
		imUserRepository.save(imUser);
		return 1;
	}
	
	public QueryResult<ImUser> findUserList(QueryBean queryBean) {
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("userName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("mobile", ExampleMatcher.GenericPropertyMatchers.contains());
		ImUser imUser = new ImUser();
		if (queryBean.getUser_name()!=null) {
			imUser.setUserName(queryBean.getUser_name());
		}
		if (queryBean.getMobile()!=null) {
			imUser.setMobile(queryBean.getMobile());
		}
		 //查询条件
        Example<ImUser> example = Example.of(imUser, exampleMatcher);
		
        int page = queryBean.getPage() -1;
        int size = queryBean.getSize();
        Pageable pageable = PageRequest.of(page,size);
		Page<ImUser> findAll = imUserRepository.findAll(example, pageable);
		QueryResult<ImUser> queryResult = new QueryResult<>();
		queryResult.setTotal(findAll.getTotalElements());
		queryResult.setList(findAll.getContent());
		return queryResult; 
	}
	
	public int addFriend(UserFrendInfo userFrendInfo) {
		
		UserFrendInfo userFrendInfo1 =userFriendInfoRepository.findByUserIdAndFrendUserIdAndStatus(userFrendInfo.getUserId(),userFrendInfo.getFrendUserId(),0);
		if (userFrendInfo1 != null) {
			// 对方已是您的好友
			return 2;
		}
		UserFrendInfo userFrendInfo2 =userFriendInfoRepository.findByUserIdAndFrendUserIdAndStatus(userFrendInfo.getUserId(),userFrendInfo.getFrendUserId(),1);
		if (userFrendInfo2 != null) {
			// 请勿重复添加
			return 3;
		}
		if (userFrendInfo.getFrendGroup() ==null) {
			userFrendInfo.setFrendGroup("我的好友");
		}
		//设为等待审核状态
		userFrendInfo.setStatus(1);
		userFriendInfoRepository.save(userFrendInfo);
		return 1;
	}

	public List<ImUser> getNewFriend(Long id) {
		// 根据friendid从好友列表中获取对方添加我的好友列表
		List<UserFrendInfo> list =userFriendInfoRepository.findByFrendUserIdAndStatus(id, 1);
		if (list == null || list.size() == 0) {
			return null;
		}
		ArrayList<ImUser> arrayList = new ArrayList<>();
		// 取出userid 对应的好友列表返回
		for (UserFrendInfo userFrendInfo : list) {
			Long userId = userFrendInfo.getUserId();
			Optional<ImUser> optional = imUserRepository.findById(userId);
			if (optional.isPresent()) {
				arrayList.add(optional.get());
			}
		}
		return arrayList;
	}

	public int orAddFriend(Integer flag, UserFrendInfo userFrendInfo) {
		Long userId = userFrendInfo.getUserId();
		Long frendUserId = userFrendInfo.getFrendUserId();
		if (flag == 2) {
			// 不同意添加好友,修改对方的好友添加状态
			UserFrendInfo userFrendInfo1 = userFriendInfoRepository.findByUserIdAndFrendUserIdAndStatus(frendUserId, userId, 1);
			if (userFrendInfo1 ==null) {
				// 操作失败
				return 5;
			}
			userFrendInfo1.setStatus(2);
			userFriendInfoRepository.save(userFrendInfo1);
			// 已拒绝
			return 2;
		}else {
			// 同意添加好友
			// 在自己的好友列表中添加
			UserFrendInfo findByUserIdAndFrendUserIdAndStatus = userFriendInfoRepository.findByUserIdAndFrendUserIdAndStatus(userId, frendUserId, 0);
			// 对方已经是你的好友无需再添加
			if (findByUserIdAndFrendUserIdAndStatus == null) {
				if (userFrendInfo.getFrendGroup() == null) {
					userFrendInfo.setFrendGroup("我的好友");
				}
				// 已添加状态
				userFrendInfo.setStatus(0);
				userFriendInfoRepository.save(userFrendInfo);
			}
			
			
			// 同时修改对方好友的状态
			UserFrendInfo userFrendInfo2 = userFriendInfoRepository.findByUserIdAndFrendUserIdAndStatus(frendUserId, userId, 1);
			userFrendInfo2.setStatus(0);
			userFriendInfoRepository.save(userFrendInfo2);
			// 已添加
			return 1;
		}
	}

	public int deleteFriend(Long userid, Long frendUserId) {
		UserFrendInfo userFrendInfo = userFriendInfoRepository.findByUserIdAndFrendUserIdAndStatus(userid, frendUserId, 0);
		if (userFrendInfo == null) {
			//删除失败
			return 2;
		}
		userFrendInfo.setDeleteFlag(1);
		userFriendInfoRepository.save(userFrendInfo);
		log.info("deleteFriend success");
		return 1;
	}

	public int addBlackList(Long userid, Long frendUserId) {
		UserFrendInfo userFrendInfo = userFriendInfoRepository.findByUserIdAndFrendUserIdAndStatus(userid, frendUserId, 0);
		if (userFrendInfo == null) {
			//加入黑名单失败
			return 2;
		}
		userFrendInfo.setFrendGroup("黑名单");
		userFrendInfo.setStatus(3);
		userFriendInfoRepository.save(userFrendInfo);
		log.info("addBlackList success");
		return 1;
	}

	public int moveOutBlackList(Long userid, Long frendUserId) {
		UserFrendInfo userFrendInfo = userFriendInfoRepository.findByUserIdAndFrendUserIdAndStatus(userid, frendUserId, 3);
		if (userFrendInfo == null) {
			//移出黑名单失败
			return 2;
		}
		userFrendInfo.setFrendGroup("我的好友");
		userFrendInfo.setStatus(0);
		userFriendInfoRepository.save(userFrendInfo);
		log.info("moveOutBlackList success");
		return 1;
	}
	
	// 获取与指定用户离线消息
	public UserMessageData getFriendsOfflineMessage(String userid, String fromUserId) {
		RedisMessageHelper redisMessageHelper = new RedisMessageHelper();
		UserMessageData friendsOfflineMessage = redisMessageHelper.getFriendsOfflineMessage(userid, fromUserId);
		return friendsOfflineMessage;
	}
	
	//获取与所有用户离线消息;
	public UserMessageData getFriendsOfflineMessageAll(String userid) {
		RedisMessageHelper redisMessageHelper = new RedisMessageHelper();
		UserMessageData friendsOfflineMessage = redisMessageHelper.getFriendsOfflineMessage(userid);
		return friendsOfflineMessage;
	}

	

}
