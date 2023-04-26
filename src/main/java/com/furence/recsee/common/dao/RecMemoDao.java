
package com.furence.recsee.common.dao;

import java.util.List;

import com.furence.recsee.common.model.RecMemo;


public interface RecMemoDao {

	List<RecMemo> selectRecMemo(RecMemo recMemo);
	Integer insertRecMemo(RecMemo recMemo);
	Integer	updateRecMemo(RecMemo recMemo);
	Integer	deleteRecMemo(RecMemo recMemo);
	Integer upsertRecMemo(RecMemo recMemo);
	Integer selectTagCheck(RecMemo recMemo);

}
