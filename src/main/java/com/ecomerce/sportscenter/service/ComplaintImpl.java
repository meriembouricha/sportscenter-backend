package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.Complaint;
import com.ecomerce.sportscenter.entity.Response;
import com.ecomerce.sportscenter.repository.ComplaintRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ComplaintImpl implements IComplaint{
    private final ComplaintRepository complaintRepo ;


    @Override
    public Complaint addComplaint(Complaint complaint) {

        return complaintRepo.save(complaint);
    }

    @Override
    public List<Complaint> getAllComplaints() {
        List<Complaint> complaints = complaintRepo.findAll();
        complaints.forEach(complaint -> {
            Response response = complaint.getResponse();
            complaint.setStatus(response != null);
        });
        return complaints;
    }


    @Override
    public void deleteComplaint(Long id) {
        complaintRepo.deleteById(id);
    }

    @Override
    public List<Complaint> retrieveComplaints(Long idUser) {
        return complaintRepo.findComplaintByUser_Id(idUser);
    }

    @Override
    public List<Complaint> findComplaintsByStatus(Boolean status) {
        return complaintRepo.findComplaintsByStatus(status);
    }





}