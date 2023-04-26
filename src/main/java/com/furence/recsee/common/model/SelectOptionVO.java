package com.furence.recsee.common.model;

public class SelectOptionVO {
	private String selectId;
	private String optionName;
	private String optionValue;

	public String getSelectId() {
		return selectId;
	}

	public void setSelectId(String selectId) {
		this.selectId = selectId;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	@Override
	public String toString() {
		return "selectOptionVO [selectId=" + selectId + ", optionName=" + optionName + ", optionValue=" + optionValue
				+ "]";
	}

}