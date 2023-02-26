package com.roms.component.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.roms.component.entity.CompleteProcess;
import com.roms.component.entity.ProcessRequest;
import com.roms.component.entity.ProcessResponse;
import com.roms.component.repository.CompleteProcessRepository;
import com.roms.component.repository.ProcessRequestRepository;
import com.roms.component.repository.ProcessResponseRepository;
import com.roms.component.wf.RepairIntegralWF;
import com.roms.component.wf.ReplacementAccessoryWF;

@Service
public class ComponentService implements RepairIntegralWF, ReplacementAccessoryWF {

	public static final String AUTH="Authorization";
	public static final String PARAM="parameters";
	public static final String URL="url.common";
	@Autowired
	Environment env;

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();

	}

	@Autowired
	EntityManager em;

	@Autowired
	private ProcessRequestRepository processRequestRepository;
	@Autowired
	private ProcessResponseRepository processResponseRepository;
	@Autowired
	private CompleteProcessRepository completeProcessRepository;
	Logger logger = LoggerFactory.getLogger(ComponentService.class);

	public boolean validateToken(String token) {
		HttpHeaders headers = new HttpHeaders();
	    headers.add(AUTH, token);
	    HttpEntity<String> entity = new HttpEntity<>(PARAM, headers);
		String result=restTemplate().exchange(env.getProperty(URL)+"auth/validate",
				HttpMethod.POST,entity,
				String.class).getBody();
		return result!=null&&result.equals("Valid");
	}

	public String decodeToken(String token) {
		HttpHeaders headers = new HttpHeaders();
	    headers.add(AUTH, token);
	    HttpEntity<String> entity = new HttpEntity<>(PARAM, headers);
		return restTemplate().exchange(env.getProperty(URL)+"auth/decode",
				HttpMethod.POST,entity,
				String.class).getBody();

	}

	public ProcessResponse processReturnRequest(ProcessRequest processRequestInput, String token) {
		int processingDuration = 5;
		int quantity = Integer.parseInt(processRequestInput.getQuantity());
		LocalDate date;

		ProcessResponse processResponse = new ProcessResponse();

		String componentType = processRequestInput.getComponentType();

		if (componentType.equals("Integral")) {
			processingDuration = RepairIntegralWF.PROCESSINGDURATION;
			processResponse.setProcessingCharge(RepairIntegralWF.PROCESSINGCHARGE * quantity);
			if (processRequestInput.isPriority()) {
				processingDuration = 2;
				processResponse.setProcessingCharge(processResponse.getProcessingCharge() + 200.0 * quantity);
			}
		}
		if (componentType.equals("Accessory")) {
			processingDuration = ReplacementAccessoryWF.PROCESSINGDURATION;
			processResponse.setProcessingCharge(ReplacementAccessoryWF.PROCESSINGCHARGE * quantity);

		}
		double packagingAndDeliveryCharge = getPackagingAndDelivery(processRequestInput.getComponentType(),
				Integer.parseInt(processRequestInput.getQuantity()), token);

		date = LocalDate.now().plusDays(processingDuration);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		processResponse.setDateOfDelivery(formatter.format(date));
		processResponse.setRequestId(UUID.randomUUID().toString());
		processResponse.setUserid(processRequestInput.getUserid());
		processResponse.setProcessid(processRequestInput.getProcessid());
		processResponse.setPackagingAndDeliveryCharge(packagingAndDeliveryCharge);
		processRequestInput = processRequestRepository.save(processRequestInput);
		processResponse.setProcessid(processRequestInput.getProcessid());
		processResponseRepository.save(processResponse);
		return processResponse;
	}

	public double getPackagingAndDelivery(String componentType, int quantity, String token) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add(AUTH, token);
			HttpEntity<String> entity = new HttpEntity<>(PARAM, headers);
			String result = restTemplate()
					.exchange(env.getProperty(URL) + "compute/packagingdeliverycost?componentType="
							+ componentType + "&quantity=" + quantity, HttpMethod.GET, entity, String.class)
					.getBody();
			if (result!=null&&!result.equals("Invalid token"))
				return Double.parseDouble(result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return 0;
	}

	public String completeProcessing(CompleteProcess completeProcess) {
		Query q = em.createNativeQuery("SELECT C.REQUESTID FROM COMPLETEPROCESS  C WHERE C.REQUESTID=:requestid");
		q.setParameter("requestid", completeProcess.getRequestid());
		if (!q.getResultList().isEmpty()) {
			return "Transaction has already completed!";
		}
		if (completeProcess.getCreditLimit() >= completeProcess.getProcessingCharge()) {
			completeProcessRepository.save(completeProcess);
			return "Payment Completed Successfully";
		} else
			return "Oops! You have exceeded the limit of your card";
	}

	public List<CompleteProcess> completeReturnRequestList(String userid) {

		Query q = em.createNativeQuery(
				"SELECT C.* FROM COMPLETEPROCESS  C INNER JOIN PROCESSRESPONSE P ON C.REQUESTID=P.REQUESTID WHERE P.USERID=:userid",
				CompleteProcess.class);
		q.setParameter("userid", userid);
		@SuppressWarnings("unchecked")
		List<CompleteProcess> result = q.getResultList();

		for (CompleteProcess c : result) {
			String cardno = c.getCardnumber();
			c.setCardnumber("XXXX-XXXX-" + cardno.split("-")[2]);
		}

		return result;
	}

}
