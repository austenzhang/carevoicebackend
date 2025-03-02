package com.team7.carevoice.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for patient creation
 */
public class CreatePatientRequest {

	@NotBlank(message = "Ahs number is required.")
	@JsonProperty("AHN")
	private String ahsNumber;

	@NotBlank(message = "Name is required.")
    private String name;

	@NotNull(message = "Date of birth is required.")
	@JsonProperty("DOB")
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;


	public String getAhsNumber() {
		return ahsNumber;
	}

	public void setAhsNumber(String ahsNumber) {
		this.ahsNumber = ahsNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}
