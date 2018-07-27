package com.trade.controller;

public enum ValidationConditions {

	EQUALS{
		public boolean execute(String input,String value) {
			return input.equalsIgnoreCase(value);
		}
	},
	BLANK{
		public boolean execute(String input,String value) {
			return input.equalsIgnoreCase("");
		}
	};
	
	ValidationConditions(){
		
	}
	
	public abstract boolean execute(String input,String value);
}
