package com.furence.recsee.wooribank.script.controller.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.EmptyFileException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.furence.recsee.common.model.AJaxResVO;
import com.furence.recsee.common.util.XssFilterUtil;
import com.furence.recsee.scriptRegistration.dao.AdminProductInsertDAO;
import com.furence.recsee.wooribank.facerecording.model.BkProductAndScriptStepDetail;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepDetailVo;
import com.furence.recsee.wooribank.facerecording.model.ScriptStepVo;
import com.furence.recsee.wooribank.script.controller.file.FileDownloadController;
import com.furence.recsee.wooribank.script.repository.dao.ExcelUploadDao;
import com.furence.recsee.wooribank.script.repository.entity.ExcelDataInfo;
import com.furence.recsee.wooribank.script.repository.entity.InsuranceAndProductList;
import com.furence.recsee.wooribank.script.repository.entity.ProductCharacterDetail;
import com.furence.recsee.wooribank.script.repository.entity.ProductFundDetail;
import com.furence.recsee.wooribank.script.repository.entity.ScriptStep;
import com.furence.recsee.wooribank.script.service.BkExcelService;
import com.furence.recsee.wooribank.script.util.file.excel.ExcelUtil;

//import jdk.internal.jline.internal.Log;

@RestController
@RequestMapping("/script/excel")
public class ProductBkExcelUpload {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductBkExcelUpload.class);
	
	@Autowired
	private BkExcelService bkExcelService;
	
	@Autowired
	private ExcelUploadDao uploadMapper;
	
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping("/upload")
	public AJaxResVO excelUpload(MultipartHttpServletRequest request) {
		AJaxResVO jRes = new AJaxResVO();
		MultipartFile file = null;
		
		Iterator<String> iterator = request.getFileNames();
		if(iterator.hasNext()) {
			file = request.getFile(iterator.next());
		}
		
		String[] headerInfo = {"number","text","date"};
		
		ExcelDataInfo readExcel = null;
		try {
			readExcel = ExcelUtil.readExcel(file, headerInfo);
		} catch (EmptyFileException e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "액셀파일을 선택하여 주십시오.");
			return jRes;
			
		} catch (Exception e) {
		}
		
		if(readExcel == null) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "액셀 규격이 맞지 않습니다.");
			return jRes;
		}
		
		InsuranceAndProductList readSheetOne = null;
		List<ProductListVo> readSheetOneProList = null;
		List<BkProductAndScriptStepDetail> readSheetTwo = null;
		List<ProductCharacterDetail> readSheetThree = null;
		List<ProductFundDetail> readSheetFour = null;
		try {
//			bkExcelService.readSheet(readExcel);
			List<XSSFSheet> sheetList = readExcel.getSheetList();
			
			for (int i = 0; i < sheetList.size(); i++) {
				XSSFSheet xssfSheet = sheetList.get(i);
				String sheetName = xssfSheet.getSheetName();
				int t =0;
				if(sheetName.contains("T0")) {
					t = 0; 
				}
				
				if(sheetName.contains("T1")) {
					t = 1; 
				}
				
				if(sheetName.contains("T2")) {
					t = 2; 
				}
				
				if(sheetName.contains("T3")) {
					t = 3; 
				}
				switch (t) {
				case 0:
					readSheetOne = ExcelUtil.ReadSheetOne(sheetList.get(i));
					readSheetOneProList = readSheetOne.getPList();
					break;
				case 1:
					readSheetTwo = ExcelUtil.ReadSheetTwo(sheetList.get(i));
//					readSheetTwo.forEach(System.out::println);
					break;
				case 2:
					readSheetThree = ExcelUtil.ReadSheetThree(sheetList.get(i));
					readSheetThree.forEach(System.out::println);
					break;
				case 3:
					readSheetFour = ExcelUtil.ReadSheetFour(sheetList.get(i));
					break;
				}
			}
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		readSheetOneProList = Optional.ofNullable(readSheetOneProList).orElse(Collections.emptyList());
		readSheetTwo = Optional.ofNullable(readSheetTwo).orElse(Collections.emptyList());
		readSheetThree = Optional.ofNullable(readSheetThree).orElse(Collections.emptyList());
		readSheetFour = Optional.ofNullable(readSheetFour).orElse(Collections.emptyList());
		
		
		if(!readSheetOneProList.isEmpty()) {
			//보험사먼저 넣기
			List<HashMap<String,String>> insureList = readSheetOne.getInsureList();
			try {
				HashMap<String, List<HashMap<String,String>>> hash = new HashMap<String, List<HashMap<String,String>>>();
				hash.put("insureList", insureList);
				uploadMapper.upsertInsureList(hash);
			}catch (Exception e) {
				jRes.setSuccess(AJaxResVO.SUCCESS_N);
				jRes.addAttribute("msg", "T_0 시트에서 에러가 발생하였습니다.");
				return jRes;
			}
		}
		try {
			if(!readSheetOneProList.isEmpty()) {
				int result = uploadMapper.conflicProductList(readSheetOneProList);
				AdminProductInsertDAO listInsert=sqlSession.getMapper(AdminProductInsertDAO.class);
				listInsert.executeProcdure();
				//스탭할준비
				if(result > 0) {
					List<ProductListVo> insertProductList = readSheetOneProList.stream().filter(i->{
						if(i.getrUseYn().equals("Y")) {
							return true;
						}
						return false;
					}).collect(Collectors.toList());
					
				
					
					List<ProductListVo> realProduct = new ArrayList<ProductListVo>(); 
					for (ProductListVo productListVo : insertProductList) {
						int stepCheckCnt = uploadMapper.stepCheck(productListVo);
						if(stepCheckCnt == 0) {
							realProduct.add(productListVo);
							logger.info("{} step count > 0",productListVo.getrProductCode());
						}
					}
					List<List<ScriptStepVo>> stepList = new ArrayList<List<ScriptStepVo>>();
					
					for (ProductListVo list : realProduct) {
						List<ScriptStepVo> voList = new ArrayList<ScriptStepVo>();
						for(int i=1; i<=5; i++) {
							ScriptStepVo vo = new ScriptStepVo();
							vo.setrScriptStepType(list.getrProductCode());
							switch (i) {
							case 1:
								vo.setrScriptStepOrder(1); vo.setrUseYn("Y"); vo.setrScriptStepParent(0); vo.setrScriptStepName("1. 녹취/본인확인");
								break;
							case 2:
								vo.setrScriptStepOrder(2); vo.setrUseYn("Y"); vo.setrScriptStepParent(0); vo.setrScriptStepName("2. 상품 설명 및 펀드 안내");
								break;
							case 3:
								vo.setrScriptStepOrder(3); vo.setrUseYn("Y"); vo.setrScriptStepParent(0); vo.setrScriptStepName("3. 해약환급금 및 사업비 등 안내");
								break;
							case 4:
								vo.setrScriptStepOrder(4); vo.setrUseYn("Y"); vo.setrScriptStepParent(0); vo.setrScriptStepName("4. 기타 사항 안내");
								break;
							case 5:
								vo.setrScriptStepOrder(5); vo.setrUseYn("Y"); vo.setrScriptStepParent(0); vo.setrScriptStepName("5. 맺음말");
								break;
							}
							voList.add(vo);
						}
						stepList.add(voList);
	//					break;
					}
					List<List<ScriptStepVo>> stepUnderList = new ArrayList<List<ScriptStepVo>>();
					for (ProductListVo list : realProduct) {
						List<ScriptStepVo> voList = new ArrayList<ScriptStepVo>();
						for(int i=1; i<=5; i++) {
							ScriptStepVo vo = new ScriptStepVo();
							vo.setrScriptStepType(list.getrProductCode());
							switch (i) {
							case 1:
								vo.setrScriptStepOrder(1); vo.setrUseYn("Y");vo.setrScriptStepName("2-1.고객이 선택한 상품설명"); vo.setrScriptStepParent(2);
								break;
							case 2:
								vo.setrScriptStepOrder(2); vo.setrUseYn("Y");vo.setrScriptStepName("2-2.펀드권유 및 안내"); vo.setrScriptStepParent(2);
								break;
							case 3:
								vo.setrScriptStepOrder(1); vo.setrUseYn("Y");vo.setrScriptStepName("3-1.가입내역 및 해약환급금 등 설명"); vo.setrScriptStepParent(3);
								break;
							case 4:
								vo.setrScriptStepOrder(2); vo.setrUseYn("Y");vo.setrScriptStepName("3-2.사업비 안내"); vo.setrScriptStepParent(3);
								break;
							case 5:
								vo.setrScriptStepOrder(3); vo.setrUseYn("Y");vo.setrScriptStepName("3-3.펀드관리 등 안내"); vo.setrScriptStepParent(3);
								break;
							}
							voList.add(vo);
						}
						stepUnderList.add(voList);
	//					break;
					}
	//				
					for (List<ScriptStepVo> list : stepList) {
						int a = uploadMapper.insertScriptStep(list);
						logger.info("insertScriptStep: "+a);
						
					}
					// 선릉금융 장예원 대리 재녹취건 안보임
					for (List<ScriptStepVo> list : stepUnderList) {
						int a = uploadMapper.insertScriptStepUnder(list);
						logger.info("insertScriptStep: "+a);
					}
					// 스탭먼져 넣고 T1시트있으면 디테일넣기
					if( !readSheetTwo.isEmpty() ) {
						
						for(BkProductAndScriptStepDetail vo : readSheetTwo) {
							String productCode = vo.getProductCode();
							boolean flag = false;
							for (ProductListVo realPro : realProduct) {
								if(realPro.getrProductCode().equals(productCode)) {
									flag = true;
								}
							}
							if(flag) {
								this.setSwitchDetailOrderFk(vo);
								logger.info("detail Success");
								
								int deResult = uploadMapper.insertScriptStepDetail(vo);
								logger.info(""+deResult);
							}
						}
					}
					
				}
			}
		}catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "T_0 , T_1 시트에서 에러가 발생하였습니다.");
			return jRes;
		}

			
		try {
			if(!readSheetThree.isEmpty()) {
				HashMap<String, List<ProductCharacterDetail>> resultList = new HashMap<String, List<ProductCharacterDetail>>();
				resultList.put("threeSheet", readSheetThree);
				int resultCnt = uploadMapper.insertUpdateProductCharacterDetail(resultList);
			}
		}catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "T_3 시트에서 에러가 발생하였습니다.");
			return jRes;
		}
		try {
			if( !readSheetFour.isEmpty() ) {
					HashMap<String, List<ProductFundDetail>> resultList = new HashMap<String, List<ProductFundDetail>>();
					resultList.put("foursheet", readSheetFour);
					int resultCnt = uploadMapper.insertUpdateProductFundDetail(resultList);
				}
		}catch (Exception e) {
			jRes.setSuccess(AJaxResVO.SUCCESS_N);
			jRes.addAttribute("msg", "T_4 시트에서 에러가 발생하였습니다.");
			return jRes;
		}
		
		jRes.setSuccess(AJaxResVO.SUCCESS_Y);
		jRes.addAttribute("msg", "업로드 완료하였습니다.");
		
		return jRes;
	}

	private void setSwitchDetailOrderFk(BkProductAndScriptStepDetail vo) {
		List<ScriptStepDetailVo> detailList = vo.getDetailList();
		for (int i = 0; i < detailList.size(); i++) {
			int c = i+1;
			HashMap<String, String> hash = new HashMap<>();
			hash.put("code", vo.getProductCode());
			hash.put("flag", null);
			ScriptStepDetailVo detailVo = detailList.get(i);
			//1구간
			if( c <= 6  ) {
				hash.put("order", "1");
				String selectDetailFk = uploadMapper.selectDetailFk(hash);
				detailVo.setrScriptStepFk(selectDetailFk);
				logger.info(detailVo.toString());
			}
			//2-1구간
			if( c > 6 && c <= 11 ) {
				hash.replace("flag", "Y");
				hash.put("order", "1");
				hash.put("Uorder", "2");
				detailVo.setrScriptStepFk(uploadMapper.selectDetailFk(hash));
			}
			//2-2
			if( c > 11 && c <= 17 ) {
				hash.replace("flag", "Y");
				hash.put("order", "2");
				hash.put("Uorder", "2");
				detailVo.setrScriptStepFk(uploadMapper.selectDetailFk(hash));
			}
			//3-1
			if( c > 17 && c <= 22) {
				hash.replace("flag", "Y");
				hash.put("order", "1");
				hash.put("Uorder", "3");
				detailVo.setrScriptStepFk(uploadMapper.selectDetailFk(hash));
			}
			//3-2
			if( c == 23 ) {
				hash.replace("flag", "Y");
				hash.put("order", "2");
				hash.put("Uorder", "3");
				detailVo.setrScriptStepFk(uploadMapper.selectDetailFk(hash));
			}
			//3-3
			if( c > 23 && c <= 29 ) {
				hash.replace("flag", "Y");
				hash.put("order", "3");
				hash.put("Uorder", "3");
				detailVo.setrScriptStepFk(uploadMapper.selectDetailFk(hash));
			}
			//4
			if( c > 29 && c <= 34 ) {
				hash.put("order", "4");
				detailVo.setrScriptStepFk(uploadMapper.selectDetailFk(hash));
			}
			//5
			if( c > 34 && c <= 38 ) {
				hash.put("order", "5");
				detailVo.setrScriptStepFk(uploadMapper.selectDetailFk(hash));
			}
			logger.info(detailVo.toString());
		}
		
		
	}
	
	

}
