package com.trade.controller;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FileData {
	
	Map<String,List<Object>> columns = new LinkedHashMap<>();
	List<Map<String,String>> rows = new LinkedList<Map<String,String>>();
	
	
	public Map<String, List<Object>> getColumns() {
		return columns;
	}
	public void setColumns(Map<String, List<Object>> columns) {
		this.columns = columns;
	}
	public List<Map<String, String>> getRows() {
		return rows;
	}
	public void setRows(List<Map<String, String>> rows) {
		this.rows = rows;
	}
	

}
