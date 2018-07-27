package com.trade.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface Validation<T> extends Function<T,ValidationResult> {

	static <T> Validation<T> nullCheck(String message){
		return holds(input -> input != null, message);
	}
	
	static <T> Validation<T> blankCheck(String message){
		return holds(input -> input != "", message);
	}
	
	static <T> Validation<T> duplicateCheck(List<?> masterList,String message){
		return holds(input -> Collections.frequency(masterList,input) == 1, message);
	}
	
	static <T> Validation<T> lengthCheck(String message){
		return holds(input -> input.toString().length() <= 50, message);
	}
	
	static <T> Validation<T> valueExistsCheck(List<?> masterList,String message){
		return holds(input -> !input.equals("") ? masterList.contains(input) : true, message);
	}
	
	static <T> Validation<T> holds(Predicate<T> p,String message){
		return input -> p.test(input) ? ValidationResult.valid() : ValidationResult.invalid(message);
	}
	
	@SafeVarargs
	static <T> Validation<T> all(Validation<T>... validations){
		return input -> {
			String reasons = Arrays.stream(validations)
									.map(v-> v.apply(input))
									.filter(r -> !r.isValid())
									.map(r -> r.getReason().get())
									.collect(Collectors.joining("\n"));
			return reasons.isEmpty() ? ValidationResult.valid() : ValidationResult.invalid(reasons);
		};
	}
	
	
}
