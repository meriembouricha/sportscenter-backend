package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.entity.Complaint;
import com.ecomerce.sportscenter.entity.Feedback;
import com.ecomerce.sportscenter.entity.FeedbackStatistics;
import com.ecomerce.sportscenter.entity.Response;
import com.ecomerce.sportscenter.service.IComplaint;
import com.ecomerce.sportscenter.service.IFeedback;
import com.ecomerce.sportscenter.service.IResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ComplaintController {
    private final IComplaint complaintService;
    private final IFeedback feedbackService ;
    private final IResponse responseService;


    @PostMapping("/addComplaint")
    public Complaint ajouterUser(@RequestBody Complaint complaint){
        return complaintService.addComplaint(complaint);
    }


    @PostMapping("/addFeedback")
    public Feedback ajouteFeedback(@RequestBody Feedback complaint){
        return feedbackService.addFeedback(complaint);
    }

    @PostMapping("/response/{complaintId}")
    public ResponseEntity<Response> respondToComplaint(@PathVariable long complaintId, @RequestBody String responseText) {
        Response response = responseService.respondToComplaint(complaintId, responseText);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/responses/{complaintId}")
    public ResponseEntity<List<Response>> getResponsesForComplaint(@PathVariable long complaintId) {
        Response responses = responseService.getResponsesForComplaint(complaintId);
        return new ResponseEntity(responses, HttpStatus.OK);
    }


    @GetMapping("/myresponses/{userId}")
    public List<Response> getMyResponses(@PathVariable long userId)
    {
        return responseService.getMyresponses(userId);
    }


    @GetMapping("/complaints")
    public List<Complaint> getAllComplaints()
    {
        return complaintService.getAllComplaints();
    }

    @DeleteMapping("/deleteComplaint/{id}")
    public void deleteComplaint(@PathVariable Long id){
        complaintService.deleteComplaint(id);
    }

    @GetMapping("/mycomplaints/{id}")
    public List<Complaint> getMyComplaints(@PathVariable long id) {
        List<Complaint> complaints = complaintService.retrieveComplaints(id);
        for (Complaint complaint : complaints) {
            Response response = responseService.getResponsesForComplaint(complaint.getIdComplaint());
            complaint.setResponse(response);
        }
        return complaints;
    }


    @GetMapping("/bystatus/{status}")
    public ResponseEntity<List<Complaint>> getComplaintsByStatus(@PathVariable Boolean status) {
        List<Complaint> complaints = complaintService.findComplaintsByStatus(status);
        return ResponseEntity.ok().body(complaints);
    }


    @GetMapping("/feedbacks")
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @DeleteMapping("/deleteFeedback/{id}")
    public void deleteFeedback(@PathVariable Long id){
        feedbackService.deleteFeedback(id);
    }

    @GetMapping("/filter")
    public List<Feedback> getAllFeedbacks(@RequestParam(name = "rate", required = false) Integer rate) {
        if (rate != null) {
            return feedbackService.getFeedbacksByRate(rate);
        } else {
            return feedbackService.getAllFeedbacks();
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<FeedbackStatistics> getFeedbackStatistics() {
        FeedbackStatistics statistics = feedbackService.calculateStatistics();
        statistics.setTotalFeedbacks(feedbackService.getTotalFeedbacks());
        return ResponseEntity.ok(statistics);
    }



}




