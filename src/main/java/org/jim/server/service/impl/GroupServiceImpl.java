package org.jim.server.service.impl;

import java.util.List;

import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.dao.GroupConfigDao;
import org.jim.server.dao.GroupDao;
import org.jim.server.dao.GroupMemberDao;
import org.jim.server.domain.ChatGroup;
import org.jim.server.domain.GroupConfig;
import org.jim.server.domain.GroupMember;
import org.jim.server.domain.ImUser;
import org.jim.server.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class GroupServiceImpl implements IGroupService {
	
	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private GroupMemberDao groupMemberDao;
	
	@Autowired
	private GroupConfigDao groupConfigDao;

	@Override
	public ResultMapInfo createGroup(ChatGroup group, ImUser imUser) {
		if (group.getId() != null) group.setId(null);
		Gson gson = new Gson();
		System.out.println(gson.toJson(group));
		group.setState(1);
		group = groupDao.save(group);
		log.info("save group success");
		GroupMember groupMember = imUser.getGroupMember(group);
		groupMember.setJurisdiction(0);
		groupMember.setState(1);
		groupMemberDao.save(groupMember);
		log.info("save group member success");
		GroupConfig groupConfig = GroupConfig.getInitGroupConfig(group);
		groupConfigDao.save(groupConfig);
		log.info("save group config success");
		return ResultMapInfo.CREATESUCCESS;
	}

	@Override
	public ResultMapInfo jionGroup(Long groupId, ImUser imUser) {
		ChatGroup group = groupDao.findById(groupId).orElse(null);
		if (group == null) return ResultMapInfo.JIONFAIL;
		GroupMember groupMember = imUser.getGroupMember(group);
		groupMember.setJurisdiction(2);
		groupMember.setState(0);
		groupMemberDao.save(groupMember);
		return ResultMapInfo.JIONSUCCESS;
	}

	@Override
	public ResultMapInfo quitGroup(Long groupId, ImUser imUser) {
		GroupMember groupMember = groupMemberDao.findByGroupIdAndUserId(groupId, imUser.getId());
		if (groupMember == null) return ResultMapInfo.QUITFAIL;
		groupMemberDao.delete(groupMember);
		return ResultMapInfo.QUITSUCCESS;
	}

	@Override
	public ResultMapInfo examineGroupMember(Long groupId, Long userId, ImUser imUser) {
		GroupMember groupManage = groupMemberDao.findByGroupIdAndUserId(groupId, imUser.getId());
		GroupMember groupMember = groupMemberDao.findByGroupIdAndUserId(groupId, userId);
		if (groupManage == null || groupMember == null) return ResultMapInfo.ADDFAIL;
		if (groupManage.getJurisdiction() > 1) return ResultMapInfo.jURISDICTIONERROR;
		groupMember.setState(1);
		return ResultMapInfo.ADDSUCCESS;
	}

	@Override
	public List<ChatGroup> getAllGroup(ImUser imUser) {
		return groupDao.getImUserAllGroup(imUser.getId());
	}

}
