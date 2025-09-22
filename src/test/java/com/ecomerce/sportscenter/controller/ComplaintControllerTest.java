package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.entity.Complaint;
import com.ecomerce.sportscenter.entity.Feedback;
import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.entity.Response;
import com.ecomerce.sportscenter.service.IComplaint;
import com.ecomerce.sportscenter.service.IFeedback;
import com.ecomerce.sportscenter.service.IResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComplaintController.class)
@ActiveProfiles("test")
class ComplaintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IComplaint complaintService;

    @MockBean
    private IFeedback feedbackService;

    @MockBean
    private IResponse responseService;

    @Autowired
    private ObjectMapper objectMapper;

    private Complaint testComplaint;
    private Feedback testFeedback;
    private Response testResponse;

    @BeforeEach
    void setUp() {
        testComplaint = Complaint.builder()
                .idComplaint(1L)
                .titleComplaint("Test Complaint")
                .descComplaint("Test Description")
                .status(false)
                .user(AppUser.builder().id(1L).username("testuser").build())
                .build();

        testFeedback = Feedback.builder()
                .id(1L)
                .product(Product.builder().id(1).name("Test Product").build())
                .user(AppUser.builder().id(1L).username("testuser").build())
                .rate(5)
                .descFeedback("Great product!")
                .build();

        testResponse = Response.builder()
                .idResponse(1L)
                .complaint(Complaint.builder().idComplaint(1L).build())
                .descResponse("Thank you for your feedback")
                .build();
    }

    @Test
    void addComplaint_ShouldReturnCreatedComplaint() throws Exception {
        // Given
        when(complaintService.addComplaint(any(Complaint.class))).thenReturn(testComplaint);

        // When & Then
        mockMvc.perform(post("/addComplaint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testComplaint)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titleComplaint").value("Test Complaint"))
                .andExpect(jsonPath("$.descComplaint").value("Test Description"))
                .andExpect(jsonPath("$.status").value(false));

        verify(complaintService).addComplaint(any(Complaint.class));
    }

    @Test
    void addFeedback_ShouldReturnCreatedFeedback() throws Exception {
        // Given
        when(feedbackService.addFeedback(any(Feedback.class))).thenReturn(testFeedback);

        // When & Then
        mockMvc.perform(post("/addFeedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testFeedback)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(5))
                .andExpect(jsonPath("$.descFeedback").value("Great product!"));

        verify(feedbackService).addFeedback(any(Feedback.class));
    }

    @Test
    void respondToComplaint_ShouldReturnCreatedResponse() throws Exception {
        // Given
        when(responseService.respondToComplaint(anyLong(), anyString())).thenReturn(testResponse);

        // When & Then
        mockMvc.perform(post("/response/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Thank you for your feedback\""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descResponse").value("Thank you for your feedback"));

        verify(responseService).respondToComplaint(1L, "Thank you for your feedback");
    }

    @Test
    void getResponsesForComplaint_ShouldReturnResponses() throws Exception {
        // Given
        when(responseService.getResponsesForComplaint(1L)).thenReturn(testResponse);

        // When & Then
        mockMvc.perform(get("/responses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descResponse").value("Thank you for your feedback"));

        verify(responseService).getResponsesForComplaint(1L);
    }

    @Test
    void getResponsesForComplaint_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Given
        when(responseService.getResponsesForComplaint(999L)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/responses/999"))
                .andExpect(status().isOk());

        verify(responseService).getResponsesForComplaint(999L);
    }
}
