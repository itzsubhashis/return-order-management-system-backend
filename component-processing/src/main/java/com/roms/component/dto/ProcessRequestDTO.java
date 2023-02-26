package com.roms.component.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProcessRequestDTO {
	String name;
	String contactno;
	String componentType;
	String componentName;
	String quantity;
	boolean priority;
}
