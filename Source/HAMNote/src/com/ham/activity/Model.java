package com.ham.activity;

public class Model {
	private String name;
	private String ID; 
	private boolean selected;
	
	public Model(String name, String id){
		this.name = name;
		this.ID = id;
		selected = false;
	}
	
	public String getName(){
		return name;
	}
	
	public String getid(){
		return ID;
	}
	
	public void setName(String name){
		this.name = name;
	}
	public boolean isSelected(){
		return selected;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	public void deleteModle(){
		 
	}

}
