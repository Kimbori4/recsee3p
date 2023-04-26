package com.furence.recsee.myfolder.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.myfolder.dao.MyFolderDao;
import com.furence.recsee.myfolder.model.MyFolderInfo;
import com.furence.recsee.myfolder.model.MyFolderListinfo;
import com.furence.recsee.myfolder.service.MyFolderService;

@Service("myFolderService")
public class MyFolderServiceImpl implements MyFolderService {
	@Autowired
	MyFolderDao myFolderDaoMapper;
	
	@Override
	public List<MyFolderInfo> selectMyFolderInfo(MyFolderInfo myFolderInfo){
		return myFolderDaoMapper.selectMyFolderInfo(myFolderInfo);
	}

	@Override
	public Integer insertMyFolderItem(MyFolderListinfo myFolderListInfo) {
		return myFolderDaoMapper.insertMyFolderItem(myFolderListInfo);
	}

	@Override
	public List<MyFolderListinfo> selectMyfolderListInfo(MyFolderListinfo myfolderListinfo) {
		return myFolderDaoMapper.selectMyfolderListInfo(myfolderListinfo);
	}

	@Override
	public Integer deleteMyFolder(MyFolderInfo myFolderInfo) {
		return myFolderDaoMapper.deleteMyFolder(myFolderInfo);
	}

	@Override
	public Integer createMyFolder(MyFolderInfo myFolderInfo) {
		return myFolderDaoMapper.createMyFolder(myFolderInfo);
	}

	@Override
	public Integer modifyMyFolder(MyFolderInfo myFolderInfo) {
		return myFolderDaoMapper.modifyMyFolder(myFolderInfo);
	}

	@Override
	public Integer deleteMyFolderItem(MyFolderListinfo myFolderListInfo) {
		return myFolderDaoMapper.deleteMyFolderItem(myFolderListInfo);
	}

	@Override
	public Integer moveMyFolderItem(MyFolderListinfo myFolderListInfo) {
		return myFolderDaoMapper.moveMyFolderItem(myFolderListInfo);
	}
}
