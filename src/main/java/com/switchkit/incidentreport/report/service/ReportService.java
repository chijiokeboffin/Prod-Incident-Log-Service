package com.switchkit.incidentreport.report.service;

import com.switchkit.incidentreport.common.ApiResponse;
import com.switchkit.incidentreport.report.model.CreateReportModel;
import com.switchkit.incidentreport.report.model.EditReportDetailModel;
import com.switchkit.incidentreport.report.model.EditReportStatusModel;
import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity<ApiResponse> createIncidentReport(CreateReportModel model);
    ResponseEntity<ApiResponse> listIncidentReports(int pageNo, int pageSize);
    ResponseEntity<ApiResponse> editIncidentReportDetail(long reportId, EditReportDetailModel model);
    ResponseEntity<ApiResponse> editIncidentReportStatus(long reportId, EditReportStatusModel model);
    ResponseEntity<ApiResponse> deleteReport(long reportId);
}
