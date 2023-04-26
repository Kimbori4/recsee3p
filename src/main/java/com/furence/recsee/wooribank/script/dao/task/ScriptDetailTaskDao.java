package com.furence.recsee.wooribank.script.dao.task;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.furence.recsee.wooribank.script.param.request.ScriptEditParam;
import com.furence.recsee.wooribank.script.param.request.ScriptEditParam.Detail;
import com.furence.recsee.wooribank.script.repository.entity.ScriptDetail;


@Repository
@Qualifier("scriptDetailTaskDao")
public class ScriptDetailTaskDao implements ScriptTaskDao<ScriptEditParam.Detail, ScriptDetail> {

	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Override
	public List<ScriptDetail> selectScriptEntity(Detail searchDto) {
		return this.sqlSession.getMapper(this.getClass())
				.selectScriptEntity(searchDto);
	}
	
	@Override
	public List<Integer> selectCountScriptEntity(Detail editDto) {
		return this.sqlSession.getMapper(this.getClass())
				.selectCountScriptEntity(editDto);
	}
	
	@Override
	public int insertScriptEditData(Detail editDto) {
		return this.sqlSession.getMapper(this.getClass())
				.insertScriptEditData(editDto);
	}
	
	@Override
	public int updateScriptEditData(Detail editDto) {
		return this.sqlSession.getMapper(this.getClass())
				.updateScriptEditData(editDto);
	}
	
	@Override
	public int deleteScriptEditData(Detail editDto) {
		return this.sqlSession.getMapper(this.getClass())
				.deleteScriptEditData(editDto);
	}
	
}
