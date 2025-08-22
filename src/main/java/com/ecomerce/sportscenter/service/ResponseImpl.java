package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.Complaint;
import com.ecomerce.sportscenter.entity.Response;
import com.ecomerce.sportscenter.repository.ComplaintRepository;
import com.ecomerce.sportscenter.repository.ResponseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ResponseImpl implements IResponse {


    private final ResponseRepository responseRepo;
    private final ComplaintRepository complaintRepo;

    @Override
    public Response respondToComplaint(long complaintId, String responseText) {
        Complaint complaint = complaintRepo.findByIdComplaint(complaintId);

        // Créer une nouvelle réponse
        Response response = new Response();
        response.setComplaint(complaint);
        response.setDescResponse(responseText);
        complaint.setStatus(true);
        complaintRepo.save(complaint);
        // Enregistrer la réponse dans la base de données
        return responseRepo.save(response);
    }

    @Override
    public Response getResponsesForComplaint(long id) {
        return responseRepo.findByComplaintId(id);
    }

    @Override
    public List<Response> getMyresponses(long id) {
        return responseRepo.findAllByComplaint_UserId(id);
    }


}