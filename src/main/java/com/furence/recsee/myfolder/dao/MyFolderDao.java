package com.furence.recsee.myfolder.dao;

import java.util.List;

import com.furence.recsee.myfolder.model.MyFolderInfo;
import com.furence.recsee.myfolder.model.MyFolderListinfo;

public interface MyFolderDao {
	 List <MyFolderInfo> selectMyFolderInfo(MyFolderInfo myFolderInfo);
	 Integer insertMyFolderItem(MyFolderListinfo myFolderListInfo);
	List<MyFolderListinfo> selectMyfolderListInfo(MyFolderListinfo myfolderListinfo);
	Integer deleteMyFolder(MyFolderInfo myFolderInfo);
	Integer createMyFolder(MyFolderInfo myFolderInfo);
	Integer modifyMyFolder(MyFolderInfo myFolderInfo);
	Integer deleteMyFolderItem(MyFolderListinfo myFolderListInfo);
	Integer moveMyFolderItem(MyFolderListinfo myFolderListInfo);
}
