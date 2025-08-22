package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.Complaint;

import java.util.List;

public interface IComplaint {
    Complaint addComplaint (Complaint complaint);
    List<Complaint> getAllComplaints();
    void deleteComplaint(Long id);
    List<Complaint> retrieveComplaints(Long idUser);
    List<Complaint> findComplaintsByStatus(Boolean status);
}