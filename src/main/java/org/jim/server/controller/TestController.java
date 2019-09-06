package org.jim.server.controller;

import org.jim.server.base.bo.ResultContent;
import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {
	
	@Autowired
	private ITestService testService;
	
	@RequestMapping(value = "/addImUserByCount", method = RequestMethod.GET)
	public ResultContent addImUserByCount(Integer count) {
		if (count == null || count <= 0) {
			count = 10;
		}
		ResultMapInfo result = testService.addImUserByCount(count);
		return new ResultContent(result);
	}

}
