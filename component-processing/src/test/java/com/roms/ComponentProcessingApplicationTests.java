package com.roms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.roms.component.controller.ComponentController;
import com.roms.component.dto.CompleteProcessDTO;
import com.roms.component.dto.ProcessRequestDTO;
import com.roms.component.entity.CompleteProcess;
import com.roms.component.entity.ProcessResponse;
import com.roms.component.service.ComponentService;

@SpringBootTest
class ComponentProcessingApplicationTests {

	@Autowired
	ComponentService componentService;
	@Autowired
	ComponentController componentController;
	
	public static final String TOKEN="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmMiLCJpc1VzZXIiOnRydWUsImlhdCI6MTY1MjcxOTk4OX0.SzYaEAeBYdgMNyHrSaxqpdMpSNlw9QB89SOtK__-XueWTotSJBbxHZ6tbBN04PuL663_cZMQ1lgcCg2ZNgUkOA";
	String reqId="";
	@Test
	void tokenFailTest() {
		assertFalse(componentService.validateToken("demo"));
		assertEquals(0.0,componentService.getPackagingAndDelivery("ABC", 0, "FALSE_KEY"));
	}
	@Test
	void decodeTokenTest() {
		assertEquals("1",componentService.decodeToken(TOKEN));
	}
	
	@Test
	void processReturnRequestTest() {
		 ProcessRequestDTO p=new ProcessRequestDTO("abc","89897767","Integral","abc","2",true);
		 
		 ProcessResponse pr=componentController.processDetail(p,TOKEN);
		 reqId=pr.getRequestId();
		 assertNotNull(pr);
	}
	@Test
	void completeProcessingTest() {
		CompleteProcess c=new CompleteProcess();
		c.setCardnumber("ABCS-DEFG-GHIJ");
		c.setRequestid(reqId);
		c.setCreditLimit(10);
		c.setProcessingCharge(500);
		assertEquals("Oops! You have exceeded the limit of your card",componentService.completeProcessing(c));
		c.setCreditLimit(100000);
		assertEquals("Payment Completed Successfully",componentService.completeProcessing(c));
		assertEquals("Transaction has already completed!",componentService.completeProcessing(c));
		CompleteProcessDTO cDto=new CompleteProcessDTO();
		cDto.setCardnumber("ABCS-DEFG-GHIJ");
		cDto.setRequestid(reqId);
		cDto.setCreditLimit("1000");
		cDto.setProcessingCharge(500);
		assertNotNull(componentController.completeProcessing(cDto, TOKEN));
		assertEquals("Token expired.Please Login again",componentController.completeProcessing(cDto, "ABC"));
		cDto.setCreditLimit("ababa");
		assertEquals("Please enter valid details",componentController.completeProcessing(cDto, TOKEN));
		} 
	@Test
	void completeReturnRequestListTest() {
		assertNotNull(componentService.completeReturnRequestList("1"));
	}
	
	

}
