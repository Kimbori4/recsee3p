
package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.RecMemoDao;
import com.furence.recsee.common.model.RecMemo;
import com.furence.recsee.common.service.RecMemoService;


@Service("recMemoService")
public class RecMemoServiceImpl implements RecMemoService {

	@Autowired
	RecMemoDao recMemoDaoMapper;

	@Override
	public List<RecMemo> selectRecMemo(RecMemo recMemo){
		return recMemoDaoMapper.selectRecMemo(recMemo);
	}
	@Override
	public Integer insertRecMemo(RecMemo recMemo){
		return recMemoDaoMapper.insertRecMemo(recMemo);
	}
	@Override
	public Integer	updateRecMemo(RecMemo recMemo){
		return recMemoDaoMapper.updateRecMemo(recMemo);
	}
	@Override
	public Integer	deleteRecMemo(RecMemo recMemo){
		return recMemoDaoMapper.deleteRecMemo(recMemo);
	}
	@Override
	public Integer upsertRecMemo(RecMemo recMemo) {
		return recMemoDaoMapper.upsertRecMemo(recMemo);
	}
	
	@Override
	public Integer selectTagCheck(RecMemo recMemo) {
		return recMemoDaoMapper.selectTagCheck(recMemo);
	}
}