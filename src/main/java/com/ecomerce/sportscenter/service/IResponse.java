package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.Response;

import java.util.List;

public interface IResponse {
    Response respondToComplaint(long complaintId, String responseText);
    Response getResponsesForComplaint (long id);
    List<Response> getMyresponses(long id);

}
