package com.furence.recsee.wooribank.script.batch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.furence.recsee.wooribank.script.repository.dao.ScriptEditApproveDao;
import com.furence.recsee.wooribank.script.repository.entity.Product;
import com.furence.recsee.wooribank.script.repository.entity.ScriptApprove;

@EnableScheduling
@Component
public class BatchTaskExecutor {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchTaskExecutor.class);
	
	private BatchTaskDao taskDao;
	
	private ScriptEditApproveDao approveDao;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	public BatchTaskExecutor(BatchTaskDao taskDao, ScriptEditApproveDao approveDao) {
		this.taskDao = taskDao;
		this.approveDao = approveDao;				
	}
	
	/**
	 * 일별 스크립트 수정 적용 예약건 적용 task
	 */
	/* @Scheduled(cron = "0 5 0 * * *") */
	public void executeDailyScriptSnapshot() {
		
		logger.info("=================== Task Executor Start ===================");
		try {
			
			Map<String, Object> param = new HashMap<String, Object>();		
			
			String today = sdf.format(new Date());
			
			List<ScriptApprove> approveLsit = this.approveDao.selectReservedApproveList(today);
			approveLsit.forEach(approve -> {
				
				List<Product> productList= this.approveDao.selectProductListForSnapshot(approve.getScriptEditId());
				
				productList.forEach( product -> {
					param.put("code", 0);
					param.put("message", "");
					param.put("productPk", Integer.parseInt(product.getProductPk()));
					param.put("scriptEditId", approve.getScriptEditId());
					this.approveDao.insertProductScriptSnapshot2(param);
					logger.info("insertProductScriptSnapshot2: {}", param);
				});
			});
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		logger.info("=================== Task Executor End ===================");
	}
}
