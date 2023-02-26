package com.roms.component.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteProcessDTO {
	String requestid;
	String cardnumber;
	String creditLimit;
	double processingCharge;

}
