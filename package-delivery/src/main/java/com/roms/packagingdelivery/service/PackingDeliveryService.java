package com.roms.packagingdelivery.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PackingDeliveryService {

	@Autowired
	Environment env;
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();

	}
	
	public double getCostForPackageAndDelivery(String componentType, int quantity) {
		if(quantity<0)
			return 0.0;
		return (getDeliveryCostMap().get(componentType)+
				getPackagingCostMap().get(componentType)+
				getPackagingCostMap().get("Protective Sheath"))*quantity;
				
	}
	
	public Map<String,Double> getDeliveryCostMap()
	{
		Map<String,Double> deliveryMap=new HashMap<>();
		deliveryMap.put("Integral",Double.parseDouble(env.getProperty("deliverycost.integral")));
		deliveryMap.put("Accessory",Double.parseDouble(env.getProperty("deliverycost.accessory")));
		return deliveryMap;
	}
	public Map<String,Double> getPackagingCostMap()
	{
		Map<String,Double> packagingMap=new HashMap<>();
		packagingMap.put("Integral",Double.parseDouble(env.getProperty("packagingcost.integral")));
		packagingMap.put("Accessory",Double.parseDouble(env.getProperty("packagingcost.accessory")));
		packagingMap.put("Protective Sheath",Double.parseDouble(env.getProperty("packagingcost.protective")));
		return packagingMap;
	}

	public boolean validate(String token) {
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", token);
	    HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		String result=restTemplate().exchange(env.getProperty("api.url.common")+"auth/validate",
				HttpMethod.POST,entity,
				String.class).getBody();
		return result!=null&&result.equals("Valid");
	}

}
