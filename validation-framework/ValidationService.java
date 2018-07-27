package com.trade.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;



import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ValidationService {
	
	String json = "{\r\n" + 
			"  \"validations\":[{\r\n" + 
			"	\"name\":\"NULL\",\r\n" + 
			"	\"value\":[\"N\"],\r\n" + 
			"	\"enable\": \"Y\",\r\n" + 
			"	\"errorMsg\":\"#{field} cannot be null\"\r\n" + 
			"  },{\r\n" + 
			"	\"name\":\"DEPENDENCIES\",\r\n" + 
			"	\"value\":[\"primary_id\"],\r\n" + 
			"	\"enable\": \"Y\",\r\n" + 
			"	\"errorMsg\":\"#{field} cannot be updated without mandatory key\"\r\n" + 
			"  }, {\r\n" + 
			"	\"name\":\"MAX_LENGTH\",\r\n" + 
			"	\"value\":[\"50\"],\r\n" + 
			"	\"enable\": \"Y\",\r\n" + 
			"	\"errorMsg\":\"#{field} length cannot be more than 50 characters\"\r\n" + 
			"  },{\r\n" + 
			"	\"name\":\"BLANK\",\r\n" + 
			"	\"value\":[\"N\"],\r\n" + 
			"	\"enable\": \"Y\",\r\n" + 
			"	\"errorMsg\":\"#{field} cannot be blank\"\r\n" + 
			"  },{\r\n" + 
			"	\"name\":\"TYPE_CHECK\",\r\n" + 
			"	\"value\":[\"STRING\"],\r\n" + 
			"	\"enable\": \"Y\",\r\n" + 
			"	\"errorMsg\":\"#{field} value does not correspond to its type\"\r\n" + 
			"  },{\r\n" + 
			"	\"name\":\"LOOK_UP\",\r\n" + 
			"	\"value\":[\"Class:MyClass\",\"PrimaryField\":\"primary_id\",\"FILTER_FIELD_1\":\"field_1\",\"FILTER_VALUE_1\":\"field_1_value\",\"FILTER_FIELD_2\":\"field_2\",\"FILTER_VALUE_2\":\"field_2_value\"],\r\n" + 
			"	\"enable\": \"Y\",\r\n" + 
			"	\"errorMsg\":\"#{field} cannot be null\"\r\n" + 
			"  },{\r\n" + 
			"	\"name\":\"FIELD_CHECK\",\r\n" + 
			"	\"field\":\"field_on_which_this_field_depends\", # so if this is Blank then actual field can have value thats exists in true or y. So if this condition does not satisfy we dont have to evaluate this validation\r\n" + 
			"	\"condition\":{\r\n" + 
			"	  \"BLANK\":\"Y\"\r\n" + 
			"  	},\r\n" + 
			"	\"constraint\":\"EXISTS\"\r\n" + 
			"	\"value\":[\"true\",\"Y\"],\r\n" + 
			"	\"enable\": \"Y\",\r\n" + 
			"	\"errorMsg\":\"#{field} cannot be blank if <field_on_which_this_field_depends> is\"\r\n" + 
			"  },{\r\n" + 
			"	\"name\":\"DUPLICATE\",\r\n" + 
			"	\"value\":[\"field_name\"],\r\n" + 
			"	\"enable\": \"Y\",\r\n" + 
			"	\"errorMsg\":\"#{field} value already present. #{field} should be unique.\"\r\n" + 
			"  },{\r\n" + 
			"	\"name\":\"NOT_SUPPORTED\",\r\n" + 
			"	\"value\":[\"Y\"],\r\n" + 
			"	\"enable\": \"Y\",\r\n" + 
			"	\"errorMsg\":\"#{field} not supported.\"\r\n" + 
			"  }\r\n" + 
			"}";
		
		public static void main(String[] args) {
			ValidationService service = new ValidationService();
			//populate FileData object with cols and rows using stream or csv file and pass this in below method
			service.validate(new FileData());
		}
		public <T> List<Map<String,Object>> validate(FileData data){
			List<Map<String,String>> rows = data.getRows();
			List<Map<String,Object>> records = new ArrayList<Map<String,Object>>();
			Map<String,Object> recMap = null;
			Set columns = new HashSet();
			//Map colMap = columns.stream().collect(Collectors.toMap("name", col -> col));
			Gson gson = new Gson();
			StringBuffer errMsg ;
			Map<String,Object> validationMap = new HashMap<String,Object>();
			validationMap.put("colValues", data.getColumns());
			try {
				
				for(Map<String,String> row : rows) {
					validationMap.put("rowValues", row);
					errMsg = new StringBuffer();
					recMap = new LinkedHashMap<String,Object>();
					recMap.putAll(row);
					for(Map.Entry<String,String> entry : row.entrySet()) {
						List<Validation<T>> validations = new ArrayList<Validation<T>>();
						//fetch if validation present
						String validationJsonStr = json;
						if(validationJsonStr != null) {
							JsonObject valJsonObj = gson.fromJson(validationJsonStr, JsonObject.class);
							for(Map.Entry<String, JsonElement> jsonObj : valJsonObj.entrySet()) {
								if(jsonObj.getValue() instanceof JsonArray) {
									JsonArray jsonArr = (JsonArray) jsonObj.getValue();
									Iterator<JsonElement> valItr = jsonArr.iterator();
									while(valItr.hasNext()) {
										String valName = null;
										try {
											ValidationMapper valMapper = gson.fromJson((JsonObject)valItr.next(), ValidationMapper.class);
											valName = valMapper.getName();
											if((valMapper.getCondition() == null || conditionSatisfied(valMapper,entry)) && valMapper.getEnable().equalsIgnoreCase("Y")) {
												valMapper.setErrorMsg(valMapper.getErrorMsg().replace("field", entry.getKey()));
												validationMap.put("mapper", valMapper);
												Validation<T> validation = ValidationRules.valueOf(valName).execute(validationMap);
												validations.add(validation);
											}
										}catch(Exception e) {
											
										}
									}
								}
							}
						}
						
						Validation valid = Validation.all(validations.toArray(new Validation[validations.size()]));
						ValidationResult result = (ValidationResult)valid.apply(entry.getValue());
						String msg = result.getReason().get();
						if(msg != null && !msg.isEmpty()) {
							errMsg.append(msg);
							errMsg.append(System.lineSeparator());
						}
					}
					
					if(!errMsg.toString().isEmpty()) {
						recMap.put("errMsg", errMsg);
						records.add(recMap);
					}
				}
				
			}catch(Exception e) {
				
			}
			
			return records;
		}
		
		private boolean conditionSatisfied(ValidationMapper valMapper,Map.Entry<String,String> entry) {
			Map<String,String> map = (Map<String,String>)valMapper.getCondition();
			boolean flag = false;
			if (map != null) {
				for(Map.Entry<String,String> entry1 : map.entrySet()) {
					flag = ValidationConditions.valueOf(entry1.getKey()).execute(entry.getValue(),entry1.getValue());
				}
			}
			
			return flag;
		}
}
