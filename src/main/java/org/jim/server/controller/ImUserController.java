package org.jim.server.controller;

import java.util.Optional;

import org.jim.server.base.bo.ResultContent;
import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.dao.ImUserRepository;
import org.jim.server.domain.ImUser;
import org.jim.server.service.ImUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/imuser")
public class ImUserController extends BaseController {
		
	@Autowired
	ImUserRepository imUserRepository;
	
	@Autowired
	ImUserService imUserService;
	
	@ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String imUser() {
		
			
		return null;
	}
	
	@ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResultContent login(String userName, String password) {
		ImUser imUser = imUserService.login(userName, password);
		if (imUser == null) return new ResultContent(ResultMapInfo.GETFAIL);
		getSession().setAttribute("imUser", imUser);
		return new ResultContent(ResultMapInfo.GETSUCCESS, imUser);
	}
}
