package com.switchkit.incidentreport.report.repository;


import com.switchkit.incidentreport.report.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT COUNT(r) FROM Report r WHERE r.assignee =:assignee AND r.status = Assigned")
    int countAssignedTasks(@Param("assignee") String assignee);
}
