package com.furence.recsee.wooribank.facerecording.util;

public class IsaOpTypeMaker {
	
	private static IsaOpTypeMaker instance = new IsaOpTypeMaker();
	
	private IsaOpTypeMaker() {
	}
	
	public static IsaOpTypeMaker getInstance() {
		return instance;
	}
	
	public String IsaOpTypeMaker(int isaOpType , String text) {
		switch (isaOpType) {
		case 2:
			text = this.IsaInputText();
			break;
		case 3:
			text = this.IsaMaedoText();
			break;

		default:
			break;
		}
		
		return text;
	}

	private String IsaMaedoText() {
		return "고객님께서 가입하신 상품은 {PRD_NM} 로서, 매도금액의 {ISA_OP_RT}% 입니다. 맞습니까?";
	}

	private String IsaInputText() {
		return "고객님께서 가입하신 상품은 {PRD_NM} 로서, 향후 입금금액의 {ISA_OP_RT}% 입니다. 맞습니까?";
	}
}
