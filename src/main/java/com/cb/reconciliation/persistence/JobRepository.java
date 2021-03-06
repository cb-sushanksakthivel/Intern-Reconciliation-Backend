package com.cb.reconciliation.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository
        extends JpaRepository<Job, String> {
    Optional<Job> findJobByJobId(String jobId);

    @Query("SELECT j.jobId,j.startTime,j.endTime,j.createdAt FROM Job j WHERE j.chargebeeSiteUrl = ?1")
    List<String> findJobIdByChargebeeSiteUrl(String siteUrl);


}
