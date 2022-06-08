package com.switchkit.incidentreport.report.model;

import javax.validation.constraints.NotBlank;

public record CreateReportModel(@NotBlank String title, @NotBlank String assignee) {

}
