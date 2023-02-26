package com.roms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.roms.packagingdelivery.controller.PackageDeliveryController;
import com.roms.packagingdelivery.service.PackingDeliveryService;

@SpringBootTest
class PackageDeliveryApplicationTests {

	
	   @Autowired
	   private PackingDeliveryService service;
	   public static final String TOKEN="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmMiLCJpc1VzZXIiOnRydWUsImlhdCI6MTY1MjcxOTk4OX0.SzYaEAeBYdgMNyHrSaxqpdMpSNlw9QB89SOtK__-XueWTotSJBbxHZ6tbBN04PuL663_cZMQ1lgcCg2ZNgUkOA";
	   @Autowired
	   PackageDeliveryController deliveryController;
	   @Test
	   void whenComponentTypeIsProvidedThenRetrievedPackageAndDeliveryIsCorrect() {
	     assertEquals(700.0,service.getCostForPackageAndDelivery("Integral",2));
	     assertEquals(0.0,service.getCostForPackageAndDelivery("Integral",0));	
	     assertEquals(400.0,service.getCostForPackageAndDelivery("Accessory",2));	
	     assertEquals(0.0,service.getCostForPackageAndDelivery("Accessory",0));	
	   }
	   
	   @Test
	   void whenInvalidQuantityisProvidedThenRetrievedPackageAndDeliveryIsCorrectlyComputed() {
	     assertEquals(0.0,service.getCostForPackageAndDelivery("Integral",-2));		
	     assertEquals(0.0,service.getCostForPackageAndDelivery("Accessory",-5));	
	     
	   }
	   
	   @Test
	   void checkTheComponent() {		   
		   
	     assertEquals("0.0",deliveryController.getPackagingDeliveryCost(TOKEN,"Integral",-2));		
	     assertEquals("700.0",deliveryController.getPackagingDeliveryCost(TOKEN,"Integral",2));	
	     assertEquals("Invalid token",deliveryController.getPackagingDeliveryCost("","Integral",2));	
	   }

}
