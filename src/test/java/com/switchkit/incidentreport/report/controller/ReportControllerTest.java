package com.switchkit.incidentreport.report.controller;

import com.switchkit.incidentreport.common.ApiResponse;
import com.switchkit.incidentreport.report.model.CreateReportModel;
import com.switchkit.incidentreport.report.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @MockBean
    private ReportService reportService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createIncidentReport() throws Exception {

        ApiResponse response = ApiResponse
                .builder()
                .code(HttpStatus.CREATED.value())
                .message("created successfully")
                .build();
        var res =  ResponseEntity.status(HttpStatus.CREATED).body(response);

        CreateReportModel model = new  CreateReportModel("Server Log","johndoe2");


        Mockito.when(reportService.createIncidentReport(model)).thenReturn(res);

        String title = "Testing incident";
        String assignee = "password";

        String body = "{\"title\":\""
                + title + "\", \"assignee\":\""
                + assignee + "\"}";

        mvc.perform(MockMvcRequestBuilders.post("/create-report")
                        .content(body))
                .andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void listIncidentReports() {
    }

    @Test
    void editIncidentReportDetail() {
    }
}