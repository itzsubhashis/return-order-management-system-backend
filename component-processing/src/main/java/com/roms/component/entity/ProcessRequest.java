package com.roms.component.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import com.roms.component.dto.ProcessRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "processrequest")
@SecondaryTable(name = "defectdetail", pkJoinColumns = @PrimaryKeyJoinColumn(name = "processid"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessRequest {
	
	public ProcessRequest(ProcessRequestDTO processRequestDTO) {
		setComponentName(processRequestDTO.getComponentName());
		setComponentType(processRequestDTO.getComponentType());
		setContactno(processRequestDTO.getContactno());
		setName(processRequestDTO.getName());
		setQuantity(processRequestDTO.getQuantity());
		setPriority(processRequestDTO.isPriority());
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "processid", unique = true, nullable = false)
	String processid;
	@Column(name="name")
	String name;
	@Column(name="userid")
	String userid;
	@Column(name="contactno")
	String contactno;
	@Column(name="componenttype",table="defectdetail")
	String componentType;
	@Column(name="componentname",table="defectdetail")
	String componentName;
	@Column(name="quantity",table="defectdetail")
	String quantity;
	@Column(name="priority")
	boolean priority;
}
