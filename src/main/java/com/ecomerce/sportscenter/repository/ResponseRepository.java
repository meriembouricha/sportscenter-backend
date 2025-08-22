package com.ecomerce.sportscenter.repository;


import com.ecomerce.sportscenter.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ResponseRepository extends JpaRepository<Response, Long> {
    @Query("SELECT r FROM Response r WHERE r.complaint.idComplaint = :complaintId")
    Response findByComplaintId(Long complaintId);

    List<Response> findAllByComplaint_UserId(long idUser);
}