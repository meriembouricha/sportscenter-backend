package com.ecomerce.sportscenter.repository;


import com.ecomerce.sportscenter.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Complaint findByIdComplaint(long complaintId);
    List<Complaint> findComplaintByUser_Id(long idUser);
    List<Complaint> findComplaintsByStatus(boolean status);
}