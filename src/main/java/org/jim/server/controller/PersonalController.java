package org.jim.server.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.jim.common.packets.UserMessageData;
import org.jim.server.common.enums.ResultMapInfo;
import org.jim.server.domain.ImUser;
import org.jim.server.domain.QueryBean;
import org.jim.server.domain.QueryResult;
import org.jim.server.domain.UserFrendInfo;
import org.jim.server.helper.redis.RedisMessageHelper;
import org.jim.server.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personal")
public class PersonalController extends BaseController{
	
	@Autowired
	PersonalService personalService;
	
	
	/*
	 * 用户注册
	 */
	@PostMapping("register")
	public Map<String, Object> registerImUser(@RequestBody ImUser imUser){
		if (imUser == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int result = personalService.registerImUser(imUser);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.CREATESUCCESS);
		}
		return returnResultMap(ResultMapInfo.CREATEFAIL);
	}
	
	/*
	 * 搜索好友列表
	 */
	@GetMapping("/list")
	public Map<String, Object> findUserList(QueryBean queryBean){
		QueryResult<ImUser> list =personalService.findUserList(queryBean);
		return returnResultMap(ResultMapInfo.GETSUCCESS,list);
	}
	
	/*
	 * 添加好友
	 * 
	 */
	@PostMapping("/addfriend")
	public Map<String, Object> addFriend(@RequestBody UserFrendInfo userFrendInfo){
		if (userFrendInfo.getUserId()==null || userFrendInfo.getFrendUserId()==null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int result = personalService.addFriend(userFrendInfo);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.AWAITAPPROVALFRIEND);
		}if (result == 2) {
			return returnResultMap(ResultMapInfo.FRIENDHAVEBEENADDED);
		} else {
			return returnResultMap(ResultMapInfo.DONOTADDAGEIN);
		}
		
	}
	
	/*
	 * 获取新好友列表
	 */
	@GetMapping("/getnewfriend")
	public Map<String, Object> getNewFriend(Long id){
		if (id==null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		List<ImUser> result = personalService.getNewFriend(id);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS,result);
		}else {
			return returnResultMap(ResultMapInfo.NONEWFRIENDSADDED);
		}
		
	}
	
	/*
	 * 是否同意添加好友
	 * 
	 */
	@GetMapping("/oraddfriend")
	public Map<String, Object> orAddFriend(@RequestParam("flag")Integer flag,UserFrendInfo userFrendInfo){
		if (userFrendInfo.getUserId()==null || userFrendInfo.getFrendUserId()==null ||
				flag == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int result = personalService.orAddFriend(flag,userFrendInfo);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
		}if (result == 2) {
			return returnResultMap(ResultMapInfo.REFUSETOSUCCEE);
		} else {
			return returnResultMap(ResultMapInfo.ADDFAIL);
		}
		
	}
	
	
	/*
	 * 删除好友
	 */
	@GetMapping("/deletefriend")
	public Map<String, Object> deleteFriend(@RequestParam("userid")Long userid,@RequestParam("frendUserId")Long frendUserId){
		if (userid==null || frendUserId==null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int result = personalService.deleteFriend(userid,frendUserId);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
		}
		return returnResultMap(ResultMapInfo.ADDFAIL);
	}
	
	/*
	 * 加入黑名单
	 */
	@GetMapping("/addblacklist")
	public Map<String, Object> addBlackList(@RequestParam("userid")Long userid,@RequestParam("frendUserId")Long frendUserId){
		if (userid==null || frendUserId==null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int result = personalService.addBlackList(userid,frendUserId);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDFAIL);
		}
		return returnResultMap(ResultMapInfo.ADDSUCCESS);
	}
	/*
	 * 移出黑名单
	 */
	@GetMapping("/moveoutblacklist")
	public Map<String, Object> moveOutBlackList(@RequestParam("userid")Long userid,@RequestParam("frendUserId")Long frendUserId){
		if (userid==null || frendUserId==null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int result = personalService.moveOutBlackList(userid,frendUserId);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDFAIL);
		}
		return returnResultMap(ResultMapInfo.ADDSUCCESS);
	}
	/*
	 * 获取与指定用户历史信息
	 * 
	 */
	@GetMapping("/getfriendhistorymessage")
	public Map<String, Object> getFriendHistoryMessage(@RequestParam("userid")String userid, 
			@RequestParam("from_userid")String from_userid,
			@RequestParam("beginTime")Double beginTime,
			@RequestParam("endTime")Double endTime,
			@RequestParam("offset")Integer offset,
			@RequestParam("count")Integer count){
		
		RedisMessageHelper redisMessageHelper = new RedisMessageHelper();
		UserMessageData userMessageData = redisMessageHelper.getFriendHistoryMessage(userid, from_userid, null, null, null, null);
		
		return returnResultMap(ResultMapInfo.ADDSUCCESS,userMessageData);
	}
	
	
	/*
	 * 获取与指定用户离线消息
	 * 
	 */
	
	@GetMapping("/getfriendsofflinemessage")
	public Map<String, Object> getFriendsOfflineMessage(@RequestParam("userid")String userid, @RequestParam("fromUserId")String fromUserId){
		if (userid==null || fromUserId==null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		UserMessageData result=personalService.getFriendsOfflineMessage(userid,fromUserId);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS,result);
		}
			return returnResultMap(ResultMapInfo.GETFAIL);
	}
	
	
	/*
	 * 获取与所有用户离线消息;
	 * 
	 */
	
	@GetMapping("/getfriendsofflinemessageall")
	public Map<String, Object> getFriendsOfflineMessageAll(@RequestParam("userid")String userid){
		if (userid==null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		UserMessageData result=personalService.getFriendsOfflineMessageAll(userid);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS,result);
		}
			return returnResultMap(ResultMapInfo.GETFAIL);
	}
}
