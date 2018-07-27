package com.trade.controller;

import java.util.List;

public class ValidationMapper {
	String name;
	String enable;
	String errorMsg;
	Object condition;
	String constraint;
	String field;
	List<Object> value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Object getCondition() {
		return condition;
	}
	public void setCondition(Object condition) {
		this.condition = condition;
	}
	public String getConstraint() {
		return constraint;
	}
	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public List<Object> getValue() {
		return value;
	}
	public void setValue(List<Object> value) {
		this.value = value;
	}
}


/*
 * {
  "validations":[{
	"name":"NULL",
	"value":["N"],
	"enable": "Y",
	"errorMsg":"#{field} cannot be null"
  },{
	"name":"DEPENDENCIES",
	"value":["primary_id"],
	"enable": "Y",
	"errorMsg":"#{field} cannot be updated without mandatory key"
  }, {
	"name":"MAX_LENGTH",
	"value":["50"],
	"enable": "Y",
	"errorMsg":"#{field} length cannot be more than 50 characters"
  },{
	"name":"BLANK",
	"value":["N"],
	"enable": "Y",
	"errorMsg":"#{field} cannot be blank"
  },{
	"name":"TYPE_CHECK",
	"value":["STRING"],
	"enable": "Y",
	"errorMsg":"#{field} value does not correspond to its type"
  },{
	"name":"LOOK_UP",
	"value":["Class:MyClass","PrimaryField":"primary_id","FILTER_FIELD_1":"field_1","FILTER_VALUE_1":"field_1_value","FILTER_FIELD_2":"field_2","FILTER_VALUE_2":"field_2_value"],
	"enable": "Y",
	"errorMsg":"#{field} cannot be null"
  },{
	"name":"FIELD_CHECK",
	"field":"field_on_which_this_field_depends", # so if this is Blank then actual field can have value thats exists in true or y. So if this condition does not satisfy we dont have to evaluate this validation
	"condition":{
	  "BLANK":"Y"
  	},
	"constraint":"EXISTS"
	"value":["true","Y"],
	"enable": "Y",
	"errorMsg":"#{field} cannot be blank if <field_on_which_this_field_depends> is"
  },{
	"name":"DUPLICATE",
	"value":["field_name"],
	"enable": "Y",
	"errorMsg":"#{field} value already present. #{field} should be unique."
  },{
	"name":"NOT_SUPPORTED",
	"value":["Y"],
	"enable": "Y",
	"errorMsg":"#{field} not supported."
  }
}
 * 
 */
