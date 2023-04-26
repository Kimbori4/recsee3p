package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.UserDBInterfaceDao;
import com.furence.recsee.admin.model.UserDBInterface;
import com.furence.recsee.admin.service.UserDBInterfaceService;

@Service("userDBInterfaceService")
public class UserDBInterfaceServiceImpl implements UserDBInterfaceService {
	
	@Autowired
	UserDBInterfaceDao userDBInterfaceMapper;

	@Override
	public List<UserDBInterface> selectUserDBInterface(UserDBInterface userDBInterface) {
		return userDBInterfaceMapper.selectUserDBInterface(userDBInterface);
	}
	
	@Override
	public UserDBInterface selectOneUserDBInterface(UserDBInterface userDBInterface) {
		return userDBInterfaceMapper.selectOneUserDBInterface(userDBInterface);
	}

	@Override
	public Integer insertUserDBInterface(UserDBInterface userDBInterface) {
		return userDBInterfaceMapper.insertUserDBInterface(userDBInterface);
	}

	@Override
	public Integer updateUserDBInterface(UserDBInterface userDBInterface) {
		return userDBInterfaceMapper.updateUserDBInterface(userDBInterface);
	}

	@Override
	public Integer deleteUserDBInterface(UserDBInterface userDBInterface) {
		return userDBInterfaceMapper.deleteUserDBInterface(userDBInterface);
	}
	
}
