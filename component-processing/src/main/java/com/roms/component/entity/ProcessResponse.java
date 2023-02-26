package com.roms.component.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "processresponse")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessResponse {
	
	@Id
	@Column(name="processid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	String processid;
	@Column(name="processingcharge")
	double processingCharge;
	@Column(name="packaginganddeliverycharge")
	double packagingAndDeliveryCharge;
	@Column(name="dateofdelivery")
	String dateOfDelivery;
	@Column(name="userid")
	String userid;
	@Column(name="requestid")
	String requestId;

}
