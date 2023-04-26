package com.furence.recsee.scriptRegistration.model;

public class ScriptStepVO {

	private String fk;
	private String parent;
	private String name;
	private String type;

	public String getFk() {
		return fk;
	}

	public void setFk(String fk) {
		this.fk = fk;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ScriptStepVO [fk=" + fk + ", parent=" + parent + ", name=" + name + ", type=" + type + "]";
	}

	
}
