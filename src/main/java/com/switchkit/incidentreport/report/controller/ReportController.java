package com.switchkit.incidentreport.report.controller;

import com.switchkit.incidentreport.common.ApiResponse;
import com.switchkit.incidentreport.report.model.CreateReportModel;
import com.switchkit.incidentreport.report.model.EditReportDetailModel;
import com.switchkit.incidentreport.report.model.EditReportStatusModel;
import com.switchkit.incidentreport.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/create-report")
    public ResponseEntity<ApiResponse> createIncidentReport(@Valid @RequestBody CreateReportModel model){
        return reportService.createIncidentReport(model);
    }

    @GetMapping(path = "/incident-reports")
    public ResponseEntity<ApiResponse> listIncidentReports(@RequestParam("pageNo") int pageNo
            ,@RequestParam("pageSize") int pageSize){
        return reportService.listIncidentReports(pageNo, pageSize);
    }

    @PostMapping(path = "/update-report-detail/{reportId}")
    public ResponseEntity<ApiResponse> editIncidentReportDetail(@PathVariable("reportId") long reportId,
                                                                @RequestBody EditReportDetailModel model){
        return reportService.editIncidentReportDetail(reportId, model);
    }
    @PostMapping(path = "/update-report-status/{reportId}")
    public ResponseEntity<ApiResponse> editIncidentReportStatus(@PathVariable("reportId") long reportId,
                                                                @RequestBody EditReportStatusModel model){
        return reportService.editIncidentReportStatus(reportId, model);
    }
}
