package com.furence.recsee.wooribank.facerecording.model;

public interface ParamKeyDefine {
	
	 public static  String opr_no =""; 
	 public static  String rcd_key =""; 
	 public static  String opr_nm ="";
	 public static  String advep_no="";
	 public static  String advpe_nm="";
	 public static  String csno="";
	 public static  String cus_nm="";
	 public static  String bz_brcd="";
	 public static  String bzbr_nm="";
	 public static  String biz_dis="";
	 public static  String sys_dis="";
	 
	 public static  String full_65_tax_abv_yn=""; 
	 public static  String agnpe_nm="";
	 
	 public static  String ssblt_ivpe_yn=""; 
	 public static  String csinc_gd="";
	 public static  String prd_nm="";
	 public static  String psn_yn="";
	 public static  String reg_am="";
	 public static  String tr_ac_yn="";
	
	class Fund implements ParamKeyDefine{
		public static final int type = 2;
		
		public static  String full_65_tax_abv_yn; 
		public static  String agnpe_nm;
	}
	class Isa  implements ParamKeyDefine{
		public static  String full_65_tax_abv_yn;
		public static  String agnpe_nm;
		public static  String isa_pfe_rt;
		public static  String isa_op_type;
	}
	
	class sht  implements ParamKeyDefine{
		public static final int type = 5;
		
	}
	class Bk implements ParamKeyDefine{
		public static final int type = 4;
		
	}
	class shintak implements ParamKeyDefine{
		
		public static final int type = 1;
		
		public static  String cust_type;
		public static  String com_rept_type;
		public static  String com_rept_type_etc;
	}

}
