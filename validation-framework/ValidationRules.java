package com.trade.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ValidationRules {
	EXISTS{
		@Override
		public <T> Validation<T> execute(Map<String,Object> inputs){
			ValidationMapper mapper = (ValidationMapper) inputs.get("mapper");
			return Validation.valueExistsCheck(mapper.getValue(),mapper.getErrorMsg());
		}
	},
	DUPLICATE{
		@Override
		public <T> Validation<T> execute(Map<String,Object> inputs){
			ValidationMapper mapper = (ValidationMapper) inputs.get("mapper");
			Map<String,List<Object>> colValues = (Map<String,List<Object>>) inputs.get("colValues");
			return Validation.duplicateCheck(colValues.get(mapper.getValue().get(0)),mapper.getErrorMsg());
		}
	},
	MAX_LENGTH{
		@Override
		public <T> Validation<T> execute(Map<String,Object> inputs){
			ValidationMapper mapper = (ValidationMapper) inputs.get("mapper");
			return Validation.lengthCheck(mapper.getErrorMsg());
		}
	},
	BLANK{
		@Override
		public <T> Validation<T> execute(Map<String,Object> inputs){
			ValidationMapper mapper = (ValidationMapper) inputs.get("mapper");
			return Validation.blankCheck(mapper.getErrorMsg());
		}
	},
	DEPENDENCIES{
		@Override
		public <T> Validation<T> execute(Map<String,Object> inputs){
			ValidationMapper mapper = (ValidationMapper) inputs.get("mapper");
			Map<String,String> rowValues = (Map<String,String>) inputs.get("rowValues");
			return Validation.holds(s -> mapper.getValue().stream().anyMatch(rowValues::containsKey),mapper.getErrorMsg());
		}
	},
	NOT_SUPPORTED{
		@Override
		public <T> Validation<T> execute(Map<String,Object> inputs){
			ValidationMapper mapper = (ValidationMapper) inputs.get("mapper");
			return Validation.holds(s -> false,mapper.getErrorMsg());
		}
	},
	TYPE_CHECK{
		@Override
		public <T> Validation<T> execute(Map<String,Object> inputs){
			ValidationMapper mapper = (ValidationMapper) inputs.get("mapper");
			//fetch type of column and match
			return Validation.holds(s -> true,mapper.getErrorMsg());
		}
	},
	FIELD_CHECK{
		@Override
		public <T> Validation<T> execute(Map<String,Object> inputs){
			ValidationMapper mapper = (ValidationMapper) inputs.get("mapper");
			Map<String,String> rowValues = (Map<String,String>) inputs.get("rowValues");
			Validation validation = ValidationRules.valueOf(mapper.getConstraint()).execute(inputs);
			ValidationResult res = (ValidationResult)validation.apply(rowValues.get(mapper.getField()));
			return Validation.holds(s -> res.getReason().get().length() != 0,mapper.getErrorMsg());
		}
	},
	LOOK_UP{
		@Override
		public <T> Validation<T> execute(Map<String,Object> inputs){
			ValidationMapper mapper = (ValidationMapper) inputs.get("mapper");
			//also fetch a lookupservice
			List<Object> values = mapper.getValue();
			Map<String,String> lookup = new HashMap<String,String>();
			for(int i=0;i<values.size();i++) {
				String value = (String)values.get(i);
				String [] entry = value.split(":");
				if(entry.length > 1) {
					lookup.put(entry[0], entry[1]);
				}
			}
			// load a class using lookupservice
			//then call a mehtod of that class passing lookup to get a map
			/* lookup.keySet().stream().anyMatch(s -> s.contains("FILTER_FIELD"))
			 * if above true then get FILTER_FIELD_1, FILTER_FIELD_2 etc and FILTER_VALUE_1 , FILTER_VALUE_2 
			 * from lookup to apply filter
			 * and return Map<String,List<String>> with key as primary id and data in list
			 */
			Map<String,List<String>> data = new HashMap<String,List<String>>();
			return Validation.valueExistsCheck(data.get(lookup.get("primaryId")),mapper.getErrorMsg());
		}
	},
	NULL{
		@Override
		public <T> Validation<T> execute(Map<String,Object> inputs){
			ValidationMapper mapper = (ValidationMapper) inputs.get("mapper");
			return Validation.nullCheck(mapper.getErrorMsg());
		}
	};
	
	
	public abstract <T> Validation<T> execute(Map<String,Object> inputs);
	

}
