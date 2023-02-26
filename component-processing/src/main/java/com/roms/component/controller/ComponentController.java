package com.roms.component.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.roms.component.dto.CompleteProcessDTO;
import com.roms.component.dto.ProcessRequestDTO;
import com.roms.component.entity.CompleteProcess;
import com.roms.component.entity.ProcessRequest;
import com.roms.component.entity.ProcessResponse;
import com.roms.component.service.ComponentService;

@RestController
public class ComponentController {
	@Autowired
	Environment env;
	Logger logger = LoggerFactory.getLogger(ComponentController.class);

	@Autowired
	private ComponentService componentService;

	@PostMapping("/process/processdetail")
	public ProcessResponse processDetail(@RequestBody ProcessRequestDTO processRequestDTO,
			@RequestHeader("Authorization") String token) {
		logger.info("Inside processDetail() controller");
		logger.info(token);
		if (componentService.validateToken(token)) {
			ProcessRequest processRequest = new ProcessRequest(processRequestDTO);
			String userid = componentService.decodeToken(token);
			processRequest.setUserid(userid);
			return componentService.processReturnRequest(processRequest, token);
		}
		return new ProcessResponse();
	}

	@PostMapping("/process/CompleteProcessing")
	public String completeProcessing(@RequestBody CompleteProcessDTO completeProcessDTO,
			@RequestHeader("Authorization") String token) {
		CompleteProcess completeProcess = null;
		try {
			completeProcess= new CompleteProcess(completeProcessDTO);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "Please enter valid details";
		}
		try {
			if (componentService.validateToken(token))
				return componentService.completeProcessing(completeProcess);
			else
				return "Token expired.Please Login again";
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "Oops!There was an issue with the payment";
		}
	}

	@GetMapping(value = "/process/processlist")
	public List<CompleteProcess> processedList(@RequestHeader("Authorization") String token) {
		logger.info("Inside processedList method");
		logger.info(token);
		if (componentService.validateToken(token)) {
			logger.info("Valid Token");
			String userid = componentService.decodeToken(token);
			return componentService.completeReturnRequestList(userid);
		}
		return Collections.emptyList();
	}
}