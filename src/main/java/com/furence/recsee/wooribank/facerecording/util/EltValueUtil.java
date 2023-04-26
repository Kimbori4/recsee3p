package com.furence.recsee.wooribank.facerecording.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.furence.recsee.wooribank.facerecording.dto.ScriptProductValueEltDto;

import microsoft.exchange.webservices.data.core.exception.misc.FormatException;

public class EltValueUtil {
	public EltValueUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static void setEltProductValueValList(ScriptProductValueEltDto dto) throws ParseException {
		if(dto.getRsProductValueValList() == null) {
			return;
		}
		String rsProductValueCode = dto.getRsProductValueCode();
		List<String> valList = dto.getRsProductValueValList();
		for(int i=0; i<valList.size(); i++) {
			String val = setValueList(valList.get(i),rsProductValueCode);
			valList.set(i,val);
		}
		dto.setRsProductValueValList(valList);
	}
	
	public static  String setValueList(String val , String rsProductValueName) throws ParseException{
		
		switch (rsProductValueName) {
		case "LZR_RPY_DT" :
		case "FST_BAS_PR_DCS_DT":
		case "RISIING_XPR_RPY_DT":
		case "XPR_ERRP_RPY_DT":
			val = formatDateToString(val);
			break;
		case "XPR_ERRP_RPY_BAR":
		case "XPR_ERRP_RPY_PTRT":
		case "XPR_MINUS_BAR":
		case "XPR_BAR":
		case "POCP_TRTPY_RT":
		case "MN_PAY_PTRT":
		case "MN_PAY_BAR":
		case "LZR_PTRT":
		case "LZR_BAR":
		case "KNOCK_IN_BAR":
		case "HRPY_AM_EVL_RT2":
		case "HRPY_AM_EVL_RT1":
		case "DFAN_TRTPY_RT":
		case "BNK_COM":
		case "XPR_PRFT_RT":
			val = formatPercentToString(val);
			break;
		}
		return val;
	}

	private static String formatPercentToString(String val) {
		DecimalFormat dcl = new DecimalFormat("#.#####"); 
		return dcl.format(Double.parseDouble(val))+"%";
	}

	private static String formatDateToString(String val) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy년 MM월 dd일");
		Date date = null;
		date = sdf.parse(val);
		return sdf2.format(date);
	}
	
	
	public static String changeEltValue(String text , List<ScriptProductValueEltDto> dtoList, Integer eltType) throws Exception {
		//선취,후취,선후취 (1,2,3)
		int trtp = 1;
		//후취보수율
		String dfanRt = null;
		//선취보수율
		String pocpRt = null;
		//리자드차수
		int lzrTp = 0;
		
		//선취후취 구분
		for(ScriptProductValueEltDto dto : dtoList) {
			
			//리자드 차수구분
			if(dto.getRsProductValueCode().equals("LZR_TP")) {
				try {
					lzrTp = Integer.parseInt(dto.getRsProductValueVal());
				}catch (Exception e) {
					throw new Exception("리자드 차수 숫자아님");
				}
			}
			
			if(dto.getRsProductValueCode().equals("TRTPY_TP")) {
				String val = dto.getRsProductValueVal();
				if(val.equals("1")) {
					dto.setRsProductValueValList(Arrays.asList("선취"));
				}else if(val.equals("2")){
					dto.setRsProductValueValList(Arrays.asList("후취"));
					trtp = 2;
				}else if(val.equals("3")) {
					dto.setRsProductValueValList(Arrays.asList("선후취"));
					trtp = 3;
				}
			}
			
			//선취보수율체크
			if(dto.getRsProductValueCode().equals("POCP_TRTPY_RT")) {
				if(dto.getRsProductValueVal() == null) {
					throw new NullPointerException("선취보수율 누락");
				}
				pocpRt = dto.getRsProductValueVal();
			}
			//후취보수율체크
			if(dto.getRsProductValueCode().equals("DFAN_TRTPY_RT")) {
				if(dto.getRsProductValueVal() == null) {
					throw new NullPointerException("후취보수율 누락");
				}
				dfanRt = dto.getRsProductValueVal();
			}
			
		}
		String trtpValue = null;
		if(trtp==3) {
			trtpValue = makeTrtpDfanValue(dfanRt,pocpRt);
		}
		
		if(dtoList.size() == 0) {
			return null;
		}
		
		
		try {
			for(ScriptProductValueEltDto dto : dtoList) {
				String valueCode = dto.getRsProductValueCode();
				//선취/후취 구분이 후취일때 기존 선취값을 후취로 바꾸어주기
				if(valueCode.equals("POCP_TRTPY_RT") && trtp==2) {
					try {
						dto.setRsProductValueValList(Arrays.asList(new DecimalFormat("#.#####").format(Double.parseDouble(dfanRt))+"%"));
					}catch (Exception e) {
						throw new Exception(dfanRt+" formating Exception");
					}
				}
				//선후취
				if(valueCode.equals("POCP_TRTPY_RT") && trtp==3) {
					dto.setRsProductValueValList(Arrays.asList(trtpValue));
				}
				
				if(valueCode.equals("YTM2")) {
					String val = dto.getRsProductValueVal();
					if(val.contains("-")) {
						dto.setRsProductValueValList(Arrays.asList(val.replaceFirst("-", "")));
					}
				}
				
				if(valueCode.equals("LZR_SQC_CN")) {
					String val = dto.getRsProductValueVal();
				}
				
				//ELF 일반형 수식적용
				if(eltType != null) {
					if(eltType == 7) {
						if(dto.getRsProductValueCode().equals("XPR_BAR")) {
							//첫번째 파싱후 넣기
							text = text.replaceFirst("\\{"+valueCode+"}",new DecimalFormat("#.#####").format(Double.parseDouble(dto.getRsProductValueVal()))+"%");
							//두번쨰 수식적용후 넣기
							double math = 100 - Double.parseDouble(dto.getRsProductValueVal());
							String reulstMath =  new DecimalFormat("#.#####").format(math)+"%";
							text = text.replaceFirst("\\{"+valueCode+"}", reulstMath);
						}
					}
				}
				
				if(text.split(valueCode).length == 1) {
					continue;
				}
				if(text.split(valueCode).length > 1) {
					if(dto.getRsProductValueValList().size() == 1) {
							text = text.replaceAll("\\{"+valueCode+"}", dto.getRsProductValueValList().get(0));
					}else if(dto.getRsProductValueValList().size() > 1) {
						//마지막 구간 배리어
						if(eltType != null) {
							if(eltType == 1) {
								if(dto.getRsProductValueCode().equals("XPR_ERRP_RPY_BAR")) {
									text = text.replaceFirst("\\{"+valueCode+"}", dto.getRsProductValueValList().get(dto.getRsProductValueValList().size()-1));
								}
								if(dto.getRsProductValueCode().equals("XPR_ERRP_RPY_PTRT")) {
									text = text.replaceFirst("\\{"+valueCode+"}", dto.getRsProductValueValList().get(dto.getRsProductValueValList().size()-1));
								}
							}
							//100 - 마지막구간 배리어
							if(eltType == 2) {
								if(dto.getRsProductValueCode().equals("XPR_ERRP_RPY_BAR")) {
									String string = dto.getRsProductValueValList().get(dto.getRsProductValueValList().size()-1);
									try {
										double bearer = (1 - new DecimalFormat("0.#%").parse(string).doubleValue());
										string = new DecimalFormat("#.##%").format(bearer);
									}catch (Exception e) {
										throw new Exception(string+" formating Exception");
									}
									text = text.replaceFirst("\\{"+valueCode+"}", string);
								}
							}
							if(eltType == 3) {
								if(dto.getRsProductValueCode().equals("XPR_ERRP_RPY_BAR")) {
									List<String> list = dto.getRsProductValueValList();
									String string = makeBearerFormatLast(list);
									text = text.replaceFirst("\\{"+valueCode+"}", string);
								}
								if(dto.getRsProductValueCode().equals("XPR_ERRP_RPY_PTRT")) {
									List<String> list = dto.getRsProductValueValList();
									String string = makeRpyAndDateFormatLast(list);
									text = text.replaceFirst("\\{"+valueCode+"}", string);
								}
							}
							if(eltType == 4) {
								if(dto.getRsProductValueCode().equals("XPR_ERRP_RPY_DT")) {
									List<String> list = dto.getRsProductValueValList();
									String string = makeRpyAndDateFormatLast(list);
									text = text.replaceFirst("\\{"+valueCode+"}", string);
								}
							}
						}
						String resultText = makeBufFromListValue(dto.getRsProductValueValList() , valueCode);
						if(resultText == null) {
							for (int i = 0; i < dto.getRsProductValueValList().size(); i++) {
								text = text.replaceFirst("\\{"+valueCode+"}", dto.getRsProductValueValList().get(i));
							}
							try {
								//리자드 구간 추가치환
								if(text.contains(valueCode)) {
									for (int i = 0; i < dto.getRsProductValueValList().size(); i++) {
										text = text.replaceFirst("\\{"+valueCode+"}", dto.getRsProductValueValList().get(i));
									}
								}
							}catch (Exception e) {
								throw new ArrayIndexOutOfBoundsException("가변값 치환 범위에러");
							}
						}else {
							text = text.replaceFirst("\\{"+valueCode+"}", resultText);
						}
					}
				}
			}
		}catch (Exception e) {
			return text;
		}
		return text;
	}
	
	public static String makeBufFromListValue(List<String> valueList,String valueCode) {
		String resultText = null;
		switch (valueCode) {
		//배리어 ( ? - ? - ? )
		case "XPR_ERRP_RPY_BAR":
			resultText = makeBearerFormat(valueList);
			break;
		//XPR_ERRP_RPY_DT : 조기상환일
		//RISIING_XPR_RPY_DT : RisingWe 만기 상환 평가일
		case "XPR_ERRP_RPY_DT":
		case "RISIING_XPR_RPY_DT" :
		//XPR_ERRP_RPY_PTRT : 조기상환 수익률 ( ? , ? , ? )
		case "XPR_ERRP_RPY_PTRT" :
			resultText =  makeRpyAndDateFormat(valueList);
			break;
		//리자드 차수 (ELF)	
		}
		return resultText;
	}
	

	public static String makeBearerFormat(List<String> valueList) {
		StringBuffer buf = new StringBuffer();
		for(int i=0; i<valueList.size(); i++) {
			String value = valueList.get(i);
			if(i == valueList.size()-1) {
				buf.append(value);
				continue;
			}
			buf.append(value+" - ");
			
		}
		return buf.toString();
	}
	public static String makeBearerFormatLast(List<String> valueList) {
		StringBuffer buf = new StringBuffer();
		for(int i=0; i<valueList.size()-1; i++) {
			String value = valueList.get(i);
			if(i == valueList.size()-2) {
				buf.append(value);
				continue;
			}
			buf.append(value+" - ");
			
		}
		return buf.toString();
	}
	public static String makeRpyAndDateFormat(List<String> valueList) {
		StringBuffer buf = new StringBuffer();
		for(int i=0; i<valueList.size(); i++) {
			String value = valueList.get(i);
			if(i == valueList.size()-1) {
				buf.append(value);
				continue;
			}
			buf.append(value+" , ");
			
		}
		return buf.toString();
	}
	public static String makeRpyAndDateFormatLast(List<String> valueList) {
		StringBuffer buf = new StringBuffer();
		for(int i=0; i<valueList.size()-1; i++) {
			String value = valueList.get(i);
			if(i == valueList.size()-2) {
				buf.append(value);
				continue;
			}
			buf.append(value+" , ");
			
		}
		return buf.toString();
	}
	//선후취 가변값 생성
	public static String makeTrtpDfanValue(String dfanRt , String pocpRt) throws Exception {
		StringBuffer buf = new StringBuffer();
		dfanRt = new DecimalFormat("#.#####").format(Double.parseDouble(dfanRt)) +"%";
		pocpRt = new DecimalFormat("#.#####").format(Double.parseDouble(pocpRt))+"%";
		if(dfanRt ==null || pocpRt == null) {
			throw new NullPointerException("선취 후취보수율 누락");
		}
		buf.append("선취"+pocpRt);
		buf.append(" 후취"+dfanRt);
		
		return buf.toString();
		
	}
}
