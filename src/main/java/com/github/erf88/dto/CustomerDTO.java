package com.github.erf88.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {

	private String name;
	private String email;
	private String processedAt;
	private String status;

}
