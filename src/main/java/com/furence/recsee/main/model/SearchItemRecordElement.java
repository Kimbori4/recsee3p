package com.furence.recsee.main.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchItemRecordElement {

	@XmlAttribute(name="calendarPosition")
	private String calendarPosition;

	@XmlAttribute(name="className")
	private String className;

	@XmlAttribute(name="connector")
	private String connector;

	@XmlAttribute(name="comboType")
	private String comboType;

	@XmlAttribute(name="dateFormat")
	private String dateFormat;

	@XmlAttribute(name="disabled")
	private boolean disabled;

	@XmlAttribute(name="enableTime")
	private boolean enableTime;

	@XmlAttribute(name="hidden")
	private boolean hidden;

	@XmlAttribute(name="info")
	private boolean info;

	@XmlAttribute(name="inputAlign")
	private String inputAlign;

	@XmlAttribute(name="inputHeight")
	private Integer inputHeight;

	@XmlAttribute(name="inputLeft")
	private Integer inputLeft;

	@XmlAttribute(name="inputTop")
	private String inputTop;

	@XmlAttribute(name="inputWidth")
	private Integer inputWidth;

	@XmlAttribute(name="filtering")
	private String filtering;

	@XmlAttribute(name="label")
	private String label;

	@XmlAttribute(name="labelAlign")
	private String labelAlign;

	@XmlAttribute(name="labelHeight")
	private Integer labelHeight;

	@XmlAttribute(name="labelLeft")
	private Integer labelLeft;

	@XmlAttribute(name="labelTop")
	private Integer labelTop;

	@XmlAttribute(name="labelWidth")
	private Integer labelWidth;

	@XmlAttribute(name="maxLength")
	private Integer maxLength;

	@XmlAttribute(name="minutesInterval")
	private Integer minutesInterval;

	@XmlAttribute(name="name")
	private String name;

	@XmlAttribute(name="note")
	private String note;

	@XmlAttribute(name="numberFormat")
	private String numberFormat;

	@XmlAttribute(name="offsetLeft")
	private Integer offsetLeft;

	@XmlAttribute(name="offsetTop")
	private Integer offsetTop;

	@XmlAttribute(name="options")
	private String options;

	@XmlAttribute(name="position")
	private String position;

	@XmlAttribute(name="readonly")
	private boolean readonly;
	
	@XmlAttribute(name="required")
	private boolean required;
	
	@XmlAttribute(name="rows")
	private Integer rows;
	
	@XmlAttribute(name="serverDateFormat")
	private String serverDateFormat;
	
	@XmlAttribute(name="showWeekNumbers")
	private boolean showWeekNumbers;

	@XmlAttribute(name="style")
	private boolean style;
	
	@XmlAttribute(name="tooltip")
	private boolean tooltip;

	@XmlAttribute(name="type")
	private String type;
	
	@XmlAttribute(name="validate")
	private String validate;

	@XmlAttribute(name="value")
	private String value;

	@XmlAttribute(name="weekStart")
	private Integer weekStart;
	
	@XmlAttribute(name="userdata")
	private String userdata;

	public String getCalendarPosition() {
		return calendarPosition;
	}

	public void setCalendarPosition(String calendarPosition) {
		this.calendarPosition = calendarPosition;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getConnector() {
		return connector;
	}

	public void setConnector(String connector) {
		this.connector = connector;
	}

	public String getComboType() {
		return comboType;
	}

	public void setComboType(String comboType) {
		this.comboType = comboType;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isEnableTime() {
		return enableTime;
	}

	public void setEnableTime(boolean enableTime) {
		this.enableTime = enableTime;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isInfo() {
		return info;
	}

	public void setInfo(boolean info) {
		this.info = info;
	}

	public String getInputAlign() {
		return inputAlign;
	}

	public void setInputAlign(String inputAlign) {
		this.inputAlign = inputAlign;
	}

	public Integer getInputHeight() {
		return inputHeight;
	}

	public void setInputHeight(Integer inputHeight) {
		this.inputHeight = inputHeight;
	}

	public Integer getInputLeft() {
		return inputLeft;
	}

	public void setInputLeft(Integer inputLeft) {
		this.inputLeft = inputLeft;
	}

	public String getInputTop() {
		return inputTop;
	}

	public void setInputTop(String inputTop) {
		this.inputTop = inputTop;
	}

	public Integer getInputWidth() {
		return inputWidth;
	}

	public void setInputWidth(Integer inputWidth) {
		this.inputWidth = inputWidth;
	}

	public String getFiltering() {
		return filtering;
	}

	public void setFiltering(String filtering) {
		this.filtering = filtering;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabelAlign() {
		return labelAlign;
	}

	public void setLabelAlign(String labelAlign) {
		this.labelAlign = labelAlign;
	}

	public Integer getLabelHeight() {
		return labelHeight;
	}

	public void setLabelHeight(Integer labelHeight) {
		this.labelHeight = labelHeight;
	}

	public Integer getLabelLeft() {
		return labelLeft;
	}

	public void setLabelLeft(Integer labelLeft) {
		this.labelLeft = labelLeft;
	}

	public Integer getLabelTop() {
		return labelTop;
	}

	public void setLabelTop(Integer labelTop) {
		this.labelTop = labelTop;
	}

	public Integer getLabelWidth() {
		return labelWidth;
	}

	public void setLabelWidth(Integer labelWidth) {
		this.labelWidth = labelWidth;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getMinutesInterval() {
		return minutesInterval;
	}

	public void setMinutesInterval(Integer minutesInterval) {
		this.minutesInterval = minutesInterval;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

	public Integer getOffsetLeft() {
		return offsetLeft;
	}

	public void setOffsetLeft(Integer offsetLeft) {
		this.offsetLeft = offsetLeft;
	}

	public Integer getOffsetTop() {
		return offsetTop;
	}

	public void setOffsetTop(Integer offsetTop) {
		this.offsetTop = offsetTop;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getServerDateFormat() {
		return serverDateFormat;
	}

	public void setServerDateFormat(String serverDateFormat) {
		this.serverDateFormat = serverDateFormat;
	}

	public boolean isShowWeekNumbers() {
		return showWeekNumbers;
	}

	public void setShowWeekNumbers(boolean showWeekNumbers) {
		this.showWeekNumbers = showWeekNumbers;
	}

	public boolean isStyle() {
		return style;
	}

	public void setStyle(boolean style) {
		this.style = style;
	}

	public boolean isTooltip() {
		return tooltip;
	}

	public void setTooltip(boolean tooltip) {
		this.tooltip = tooltip;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getWeekStart() {
		return weekStart;
	}

	public void setWeekStart(Integer weekStart) {
		this.weekStart = weekStart;
	}

	public String getUserdata() {
		return userdata;
	}

	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}
}
