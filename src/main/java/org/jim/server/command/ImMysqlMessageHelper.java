package org.jim.server.command;

import java.util.List;

import org.jim.common.listener.ImBindListener;
import org.jim.common.message.AbstractMessageHelper;
import org.jim.common.packets.ChatBody;
import org.jim.common.packets.Group;
import org.jim.common.packets.User;
import org.jim.common.packets.UserMessageData;



public class ImMysqlMessageHelper extends AbstractMessageHelper{

	@Override
	public ImBindListener getBindListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOnline(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Group getGroupUsers(String group_id, Integer type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> getAllGroupUsers(String user_id, Integer type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group getFriendUsers(String user_id, String friend_group_id, Integer type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> getAllFriendUsers(String user_id, Integer type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByType(String userid, Integer type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addGroupUser(String userid, String group_id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getGroupUsers(String group_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getGroups(String user_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeMessage(String timelineTable, String timelineId, ChatBody chatBody) {
		// TODO Auto-generated method stub
		System.out.println(timelineTable);
		System.out.println(timelineId);
		System.out.println(chatBody.getContent());
		System.out.println(chatBody.getFrom());
		System.out.println(chatBody.getTo());
		System.out.println(chatBody.getCreateTime());
	}

	@Override
	public void removeGroupUser(String userId, String group_id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserMessageData getFriendsOfflineMessage(String userId, String fromUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserMessageData getFriendsOfflineMessage(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserMessageData getGroupOfflineMessage(String userId, String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserMessageData getFriendHistoryMessage(String userId, String fromUserId, Double beginTime, Double endTime,
			Integer offset, Integer count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserMessageData getGroupHistoryMessage(String userId, String groupid, Double beginTime, Double endTime,
			Integer offset, Integer count) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
}
