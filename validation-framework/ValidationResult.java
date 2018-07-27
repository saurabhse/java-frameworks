package com.trade.controller;

import java.util.Optional;

public interface ValidationResult {

	static ValidationResult valid() {
		return new ValidationResult() {
			public boolean isValid() {
				return true;
			}
			
			public Optional<String> getReason(){
				return Optional.of("");
			}
		};
	}
	
	static ValidationResult invalid(String reason) {
		return new ValidationResult() {
			public boolean isValid() {
				return false;
			}
			
			public Optional<String> getReason(){
				return Optional.of(reason);
			}
		};
	}
	
	boolean isValid();
	Optional<String> getReason();
}
