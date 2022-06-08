package com.switchkit.incidentreport.report.service;

import com.switchkit.incidentreport.common.ApiResponse;
import com.switchkit.incidentreport.exception.PendingAssignedTaskException;
import com.switchkit.incidentreport.exception.RecordNotFoundException;
import com.switchkit.incidentreport.report.entities.Report;
import com.switchkit.incidentreport.report.entities.ReportStatus;
import com.switchkit.incidentreport.report.model.CreateReportModel;
import com.switchkit.incidentreport.report.model.EditReportDetailModel;
import com.switchkit.incidentreport.report.model.EditReportStatusModel;
import com.switchkit.incidentreport.report.repository.ReportRepository;
import com.switchkit.incidentreport.user.utilities.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;


@Transactional
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

    private final ReportRepository  reportRepository;

    @Override
    public ResponseEntity<ApiResponse> createIncidentReport(CreateReportModel model) {
        String username = AuthUtil.getUserName();

        if(reportRepository.countAssignedTasks(model.assignee()) > 0){
            throw new PendingAssignedTaskException("Selected Assignee %s has pending task".formatted(model.assignee()));
        }

        ReportStatus status = Objects.isNull(model.assignee())? ReportStatus.New: ReportStatus.Assigned;
        Report report = new Report(null,
                model.title(),
                status,
                model.assignee(),
                username,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null);
        var res = reportRepository.save(report);

        ApiResponse response = ApiResponse
                .builder()
                .code(HttpStatus.CREATED.value())
                .message("created successfully")
                .build();
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<ApiResponse> listIncidentReports(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        var page = reportRepository.findAll(pageable);
        var reports = page.get().collect(Collectors.toList());
        if(reports == null){
            throw new RecordNotFoundException("No record found in the database");
        }
        ApiResponse response = ApiResponse
                .builder()
                .data(reports)
                .code(HttpStatus.OK.value())
                .message("success")
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponse> editIncidentReportDetail(long reportId,EditReportDetailModel model) {
        String lastModifiedBy = AuthUtil.getUserName();

        var report = reportRepository.findById(reportId)
               .orElseThrow(()->
                       new RecordNotFoundException("No record with Id %s found in the database".formatted(reportId))
               );

      if(Objects.nonNull(model.assignee())){
          report.setAssignee(model.assignee());
      }
        if(Objects.nonNull(model.title())){
            report.setAssignee(model.title());
        }
        report.setLastModifiedBy(lastModifiedBy);
        report.setLastModifiedDate(LocalDateTime.now());

        ApiResponse response = ApiResponse
                .builder()
                .code(HttpStatus.OK.value())
                .message("updated detail successfully")
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponse> editIncidentReportStatus(long reportId, EditReportStatusModel model) {
        var report = reportRepository.findById(reportId)
                .orElseThrow(()->
                        new RecordNotFoundException("No record with Id %s found in the database".formatted(reportId))
                );

       try{
           String pizzaEnumValue = model.status();
           ReportStatus reportStatus  = ReportStatus.valueOf(pizzaEnumValue);

           if(report.getStatus().equals(reportStatus)){
             throw new IllegalArgumentException("Current status is %s".formatted(model.status()));
           }
           report.setStatus(reportStatus);

       }catch (IllegalArgumentException ex){
           throw new IllegalArgumentException("Status must be New or Assigned or Closed");
       }
        ApiResponse response = ApiResponse
                .builder()
                .code(HttpStatus.OK.value())
                .message("updated detail successfully")
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteReport(long reportId) {
        var report = reportRepository.findById(reportId)
                .orElseThrow(()->
                        new RecordNotFoundException("No record with Id %s found in the database".formatted(reportId))
                );
        reportRepository.deleteById(reportId);
        ApiResponse response = ApiResponse
                .builder()
                .code(HttpStatus.OK.value())
                .message("deleted successfully")
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
