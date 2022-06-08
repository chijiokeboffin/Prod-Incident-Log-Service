package com.switchkit.incidentreport.report.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @SequenceGenerator(name = "report_id_sequence",
            sequenceName = "report_id_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_id_sequence")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private ReportStatus status;
    private String assignee;
    @Column(nullable = false)
    private String assignedBy;
    private LocalDateTime assignedDate;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;

   // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
}
