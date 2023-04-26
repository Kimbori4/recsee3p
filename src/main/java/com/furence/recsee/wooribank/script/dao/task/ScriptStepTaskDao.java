package com.furence.recsee.wooribank.script.dao.task;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.wooribank.script.param.request.base.EditDtoType;
import com.furence.recsee.wooribank.script.repository.entity.ScriptEntity;


@Repository("scriptStepTaskDao")
public class ScriptStepTaskDao<E extends EditDtoType,V extends ScriptEntity> implements ScriptTaskDao<E, V> {

	private SqlSessionTemplate sqlSession;

	@Autowired
	public ScriptStepTaskDao(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;	
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<V> selectScriptEntity(E e) {
		return (List<V>)this.sqlSession.getMapper( ScriptTaskDao.class )
				.selectScriptEntity(e);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> selectCountScriptEntity(E e) {
		return (List<Integer>)this.sqlSession.getMapper( ScriptTaskDao.class )
				.selectCountScriptEntity(e);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int insertScriptEditData(E e) {
		return this.sqlSession.getMapper( ScriptTaskDao.class )
				.insertScriptEditData(e);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int updateScriptEditData(E e) {
		return this.sqlSession.getMapper( ScriptTaskDao.class )
				.updateScriptEditData(e);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int deleteScriptEditData(E e) {
		return this.sqlSession.getMapper( this.getClass() )
				.deleteScriptEditData(e);
	}
	
}
