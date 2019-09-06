package org.jim.server.controller.manage;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/manage/group")
public class GroupManageController {
	
	@RequestMapping(value = "/examineGroup", method = RequestMethod.GET)
	public Map<String, Object> createGroup(Integer examine) {
		 
		return null;
	}

	@RequestMapping(value = "/prohibitGroup", method = RequestMethod.GET)
	public Map<String, Object> prohibitGroup(String groupId) {
		 
		return null;
	}
	
	@RequestMapping(value = "/removeGroup", method = RequestMethod.GET)
	public Map<String, Object> removeGroup(String groupId) {
		 
		return null;
	}
}
