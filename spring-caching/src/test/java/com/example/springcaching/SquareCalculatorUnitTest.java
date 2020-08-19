package com.example.springcaching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Assertions;

public class SquareCalculatorUnitTest {
	
	private SquaredCalculator squaredCalculator = new SquaredCalculator();
	private CacheHelper cacheHelper = new CacheHelper();
	
	@BeforeEach 
	public void setup() {
		squaredCalculator.setCache(cacheHelper);
	}
	
	@Test
	public void whenCalculatingSquareValueAgain_thenCacheHasAllValues() {
		for(int i = 10; i < 15; i++) {
			Assertions.assertFalse(cacheHelper.getSquareNumberCache().containsKey(i));
			System.out.println("Square value of " + i + " is: " + squaredCalculator.getSquareValueOfNumber(i) + "\n");
		}
		
		for(int i = 10; i < 15; i++) {
			Assertions.assertTrue(cacheHelper.getSquareNumberCache().containsKey(i));
			System.out.println("Square value of " + i + " is: "
			          + squaredCalculator.getSquareValueOfNumber(i) + "\n");
		}
	}

}
