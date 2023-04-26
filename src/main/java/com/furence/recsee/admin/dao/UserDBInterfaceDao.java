package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.UserDBInterface;

public interface UserDBInterfaceDao {
	List<UserDBInterface> selectUserDBInterface(UserDBInterface userDBInterface);
	UserDBInterface selectOneUserDBInterface(UserDBInterface userDBInterface);
	Integer insertUserDBInterface(UserDBInterface userDBInterface);
	Integer updateUserDBInterface(UserDBInterface userDBInterface);
	Integer deleteUserDBInterface(UserDBInterface userDBInterface);
}
