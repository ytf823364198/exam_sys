package com.ziyue.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ziyue.service.ExampleTransaction;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TransactionApplicationTests {

	@Autowired
	private ExampleTransaction exampleTransaction;

	@Test
	public void testTx() {
		exampleTransaction.testTx();
	}
	
	@Test 
	public void testTxMultiple() {
		exampleTransaction.testTxMultiple();
	}
	
	@Test
	public void modifyUser() {
		exampleTransaction.modifyUser();
	}
	
}
