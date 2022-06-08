package com.switchkit.incidentreport.report.model;


import javax.validation.constraints.NotBlank;

public record EditReportStatusModel(@NotBlank(message = "Status is required") String status) {

}
