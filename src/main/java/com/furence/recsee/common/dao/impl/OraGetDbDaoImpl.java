package com.furence.recsee.common.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.OraGetDbDao;
import com.furence.recsee.common.model.ChannInfo;
import com.furence.recsee.common.model.CodeInfoVO;
import com.furence.recsee.common.model.CustomerInfoVO;

@Repository("oraGetDbDao")
public class OraGetDbDaoImpl implements OraGetDbDao {

	@Autowired
	@Qualifier("oracleSqlSession")
	private SqlSession sqlSession;

	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public List<CustomerInfoVO> selectsCustomerList(){
		//List<CustomerInfoVO> result =  sqlSession.selects("com.furence.nts.mapper.oraGetDb.firstHourCallList",nd);
		//List<CustomerInfoVO> result =  sqlSession.selectList("com.furence.recsee.common.mapper.oraGetDb.selectsCustomerList");
		//return result;
		OraGetDbDao selectCustInfoMapper = sqlSession.getMapper(OraGetDbDao.class);
		return selectCustInfoMapper.selectsCustomerList();
	}

	public List<CodeInfoVO> selectsMgCodeList(){
		//List<CustomerInfoVO> result =  sqlSession.selects("com.furence.nts.mapper.oraGetDb.firstHourCallList",nd);
		//List<CodeInfoVO> result =  sqlSession.selectList("com.furence.recsee.common.mapper.oraGetDb.selectsMgCodeList");
		//return result;
		OraGetDbDao selectCustInfoMapper = sqlSession.getMapper(OraGetDbDao.class);
		return selectCustInfoMapper.selectsMgCodeList();
	}

	public List<CodeInfoVO> selectsSgCodeList(){
		//List<CustomerInfoVO> result =  sqlSession.selects("com.furence.nts.mapper.oraGetDb.firstHourCallList",nd);
		//List<CodeInfoVO> result =  sqlSession.selectList("com.furence.recsee.common.mapper.oraGetDb.selectsSgCodeList");
		OraGetDbDao selectCustInfoMapper = sqlSession.getMapper(OraGetDbDao.class);
		return selectCustInfoMapper.selectsSgCodeList();
	}

	@Override
	public void selectCustInfo(HashMap<String, Object> map){
		OraGetDbDao selectCustInfoMapper = sqlSession.getMapper(OraGetDbDao.class);

		selectCustInfoMapper.selectCustInfo(map);
	}


	@Override
	public 	List<ChannInfo> selectChannelInfo(){
		OraGetDbDao selectChannelInfoMapper = sqlSession.getMapper(OraGetDbDao.class);
		return selectChannelInfoMapper.selectChannelInfo();
	}

	@Override
	public 	Integer selectMChannCnt(ChannInfo channelInfo){
		OraGetDbDao selectChannelInfoMapper = sqlSession.getMapper(OraGetDbDao.class);
		return selectChannelInfoMapper.selectMChannCnt(channelInfo);
	}

	@Override
	public void encodingCHAR(){
		OraGetDbDao selectCustInfoMapper  = sqlSession.getMapper(OraGetDbDao.class);
		selectCustInfoMapper.encodingCHAR();
	}

}
