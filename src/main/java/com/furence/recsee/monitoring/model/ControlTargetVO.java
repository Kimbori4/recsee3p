package com.furence.recsee.monitoring.model;

import java.util.List;

public class ControlTargetVO {
	private String rm_target_id;
	private String rm_target_name;
	private String rm_target_type;
	private String rm_target_ip;
	private String rm_target_use;
	private String rm_target_descr;
	private String rm_target_ui_order;
	private String rm_target_ui_watch_time;
	private String rm_target_ui_position_left;
	private String rm_target_ui_position_top;
	private String rm_target_ui_object_icon;
	private String rm_icon_path;
	private String rm_icon_filename;
	private String rm_icon_descr;
	
	private List<ItemVo> itemList;
	
	private int seq;
	
	public String getRm_icon_path() {
		return rm_icon_path;
	}

	public void setRm_icon_path(String rm_icon_path) {
		this.rm_icon_path = rm_icon_path;
	}
   
	public String getRm_icon_filename() {
		return rm_icon_filename;
	}

	public void setRm_icon_filename(String rm_icon_filename) {
		this.rm_icon_filename = rm_icon_filename;
	}

	public String getRm_icon_descr() {
		return rm_icon_descr;
	}

	public void setRm_icon_descr(String rm_icon_descr) {
		this.rm_icon_descr = rm_icon_descr;
	}

	public String getRm_target_ui_order() {
		return rm_target_ui_order;
	}

	public void setRm_target_ui_order(String rm_target_ui_order) {
		this.rm_target_ui_order = rm_target_ui_order;
	}

	public String getRm_target_ui_watch_time() {
		return rm_target_ui_watch_time;
	}

	public void setRm_target_ui_watch_time(String rm_target_ui_watch_time) {
		this.rm_target_ui_watch_time = rm_target_ui_watch_time;
	}

	public String getRm_target_ui_object_icon() {
		return rm_target_ui_object_icon;
	}

	public void setRm_target_ui_object_icon(String rm_target_ui_object_icon) {
		this.rm_target_ui_object_icon = rm_target_ui_object_icon;
	}

	public String getRm_target_ui_position_left() {
		return rm_target_ui_position_left;
	}

	public void setRm_target_ui_position_left(String rm_target_ui_position_left) {
		this.rm_target_ui_position_left = rm_target_ui_position_left;
	}

	public String getRm_target_ui_position_top() {
		return rm_target_ui_position_top;
	}

	public void setRm_target_ui_position_top(String rm_target_ui_position_top) {
		this.rm_target_ui_position_top = rm_target_ui_position_top;
	}

	public String getRm_target_id() {
		return rm_target_id;
	}

	public void setRm_target_id(String rm_target_id) {
		this.rm_target_id = rm_target_id;
	}

	public String getRm_target_name() {
		return rm_target_name;
	}

	public void setRm_target_name(String rm_target_name) {
		this.rm_target_name = rm_target_name;
	}

	public String getRm_target_type() {
		return rm_target_type;
	}

	public void setRm_target_type(String rm_target_type) {
		this.rm_target_type = rm_target_type;
	}

	public String getRm_target_ip() {
		return rm_target_ip;
	}

	public void setRm_target_ip(String rm_target_ip) {
		this.rm_target_ip = rm_target_ip;
	}

	public String getRm_target_use() {
		return rm_target_use;
	}

	public void setRm_target_use(String rm_target_use) {
		this.rm_target_use = rm_target_use;
	}

	public String getRm_target_descr() {
		return rm_target_descr;
	}

	public void setRm_target_descr(String rm_target_descr) {
		this.rm_target_descr = rm_target_descr;
	}

	public List<ItemVo> getItemList() {
		return itemList;
	}

	public void setItemList(List<ItemVo> itemList) {
		this.itemList = itemList;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
	
}
