package com.ecomerce.sportscenter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idComplaint;
    @ManyToOne
    AppUser user;
    String titleComplaint;
    String descComplaint;
    boolean status;
    @JsonIgnore
    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL)
    private Response response;
}