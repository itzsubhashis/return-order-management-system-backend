package com.roms.component.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.roms.component.dto.CompleteProcessDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name="completeprocess")
public class CompleteProcess {
	public CompleteProcess(CompleteProcessDTO completeProcessDTO) {
		this.cardnumber=completeProcessDTO.getCardnumber();
		this.creditLimit=Double.parseDouble(completeProcessDTO.getCreditLimit());
		this.processingCharge=completeProcessDTO.getProcessingCharge();
		this.requestid=completeProcessDTO.getRequestid();
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="processid")
	int processid;
	@Column(name="requestid")
	String requestid;
    @Column(name="cardnumber")
	String cardnumber;
    @Column(name="creditlimit")
	double creditLimit;
    @Column(name="processingcharge") 
	double processingCharge;

}
